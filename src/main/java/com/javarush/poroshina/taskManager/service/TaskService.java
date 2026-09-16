package com.javarush.poroshina.taskManager.service;

import com.javarush.poroshina.taskManager.exception.TaskNotFoundException;
import com.javarush.poroshina.taskManager.exception.UserNotFoundException;
import com.javarush.poroshina.taskManager.model.TaskStatus;
import com.javarush.poroshina.taskManager.model.dto.TaskCreateRequestDto;
import com.javarush.poroshina.taskManager.model.dto.TaskResponseDto;
import com.javarush.poroshina.taskManager.model.dto.TaskUpdateRequestDto;
import com.javarush.poroshina.taskManager.model.entity.Task;
import com.javarush.poroshina.taskManager.model.entity.User;
import com.javarush.poroshina.taskManager.repository.TaskRepository;
import com.javarush.poroshina.taskManager.repository.UserRepository;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskService(
            TaskRepository taskRepository,
            UserRepository userRepository
    ) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public TaskResponseDto getTaskResponseById(Long id) {
        return toResponse(getTaskById(id));
    }

    @Transactional(readOnly = true)
    public List<TaskResponseDto> getAllTaskResponses() {
        return taskRepository.findAllByDeletedFalse()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public TaskResponseDto createTask(TaskCreateRequestDto request) {
        User author = getUserById(request.getAuthorId());

        Task task = new Task();
        task.setDescription(request.getDescription());
        task.setTaskStatus(TaskStatus.CREATED);
        task.setAuthor(author);
        task.setCreatedAt(Instant.now());
        task.setDeleted(false);
        task.setCompletedAt(null);
        task.setUpdatedAt(null);

        Task savedTask = taskRepository.save(task);

        return toResponse(savedTask);
    }

    @Transactional
    public TaskResponseDto updateTask(
            Long id,
            TaskUpdateRequestDto request
    ) {
        Task task = getTaskById(id);

        if (task.isDeleted()) {
            throw new TaskNotFoundException(
                    "Task not found with id: " + id
            );
        }

        TaskStatus newStatus = request.getTaskStatus();

        if (newStatus == TaskStatus.IN_PROGRESS
                && request.getExecutorId() == null) {
            throw new IllegalStateException(
                    "Executor id is required for IN_PROGRESS status"
            );
        }

        if (newStatus == TaskStatus.CREATED
                && request.getExecutorId() != null) {
            throw new IllegalStateException(
                    "Executor id must be null for CREATED status"
            );
        }

        if (newStatus == TaskStatus.DONE
                && request.getExecutorId() == null) {
            throw new IllegalStateException(
                    "Executor id is required for DONE status"
            );
        }

        if (newStatus == TaskStatus.DONE
                && task.getTaskStatus() != TaskStatus.IN_PROGRESS) {
            throw new IllegalStateException(
                    "Only task in progress can be completed"
            );
        }

        task.setDescription(request.getDescription());
        task.setTaskStatus(newStatus);

        if (newStatus == TaskStatus.CREATED) {
            task.setExecutor(null);
            task.setCompletedAt(null);
        }

        if (newStatus == TaskStatus.IN_PROGRESS) {
            User executor = getUserById(request.getExecutorId());

            task.setExecutor(executor);
            task.setCompletedAt(null);
        }

        if (newStatus == TaskStatus.DONE) {
            User executor = getUserById(request.getExecutorId());

            task.setExecutor(executor);
            task.setCompletedAt(Instant.now());
        }

        task.setUpdatedAt(Instant.now());

        Task savedTask = taskRepository.save(task);

        return toResponse(savedTask);
    }

    @Transactional
    public void deleteTask(Long id) {
        Task task = getTaskById(id);

        if (task.isDeleted()) {
            throw new TaskNotFoundException(
                    "Task not found with id: " + id
            );
        }

        task.setDeleted(true);
        task.setExecutor(null);
        task.setCompletedAt(null);
        task.setUpdatedAt(Instant.now());

        taskRepository.save(task);
    }

    private Task getTaskById(Long id) {
        return taskRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() ->
                        new TaskNotFoundException(
                                "Task not found with id: " + id
                        )
                );
    }

    private User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found with id: " + id
                        )
                );
    }

    private TaskResponseDto toResponse(Task task) {
        User author = task.getAuthor();
        User executor = task.getExecutor();

        return new TaskResponseDto(
                task.getId(),
                task.getDescription(),
                task.getTaskStatus(),
                author.getId(),
                author.getUsername(),
                executor != null ? executor.getId() : null,
                executor != null ? executor.getUsername() : null,
                task.getCreatedAt(),
                task.getUpdatedAt(),
                task.getCompletedAt(),
                task.isDeleted()
        );
    }

    private List<TaskResponseDto> toResponseList(
            List<Task> tasks
    ) {
        return tasks.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TaskResponseDto> getTasksByAuthorId(Long authorId) {
        getUserById(authorId);

        return toResponseList(
                taskRepository.findByAuthor_IdAndDeletedFalse(authorId)
        );
    }

    @Transactional(readOnly = true)
    public List<TaskResponseDto> getTasksByExecutorId(Long executorId) {
        getUserById(executorId);

        return toResponseList(
                taskRepository.findByExecutor_IdAndDeletedFalse(executorId)
        );
    }

    @Transactional(readOnly = true)
    public List<TaskResponseDto> getTasksWithoutExecutor() {
        return toResponseList(
                taskRepository.findByExecutorIsNullAndDeletedFalse()
        );
    }

    @Transactional(readOnly = true)
    public List<TaskResponseDto> getTasksByStatus(
            TaskStatus taskStatus
    ) {
        return toResponseList(
                taskRepository.findByTaskStatusAndDeletedFalse(
                        taskStatus
                )
        );
    }

    private Instant toStartOfDay(
            LocalDate date,
            ZoneId zoneId
    ) {
        return date
                .atStartOfDay(zoneId)
                .toInstant();
    }

    @Transactional(readOnly = true)
    public List<TaskResponseDto> getTasksByCreatedDate(
            LocalDate date,
            ZoneId zoneId
    ) {
        Instant start = toStartOfDay(date, zoneId);
        Instant end = toStartOfDay(date.plusDays(1), zoneId);

        return toResponseList(
                taskRepository
                        .findByCreatedAtGreaterThanEqualAndCreatedAtLessThanAndDeletedFalse(
                                start,
                                end
                        )
        );
    }

    @Transactional(readOnly = true)
    public List<TaskResponseDto> getTasksByUpdatedDate(
            LocalDate date,
            ZoneId zoneId
    ) {
        Instant start = toStartOfDay(date, zoneId);
        Instant end = toStartOfDay(date.plusDays(1), zoneId);

        return toResponseList(
                taskRepository
                        .findByUpdatedAtGreaterThanEqualAndUpdatedAtLessThanAndDeletedFalse(
                                start,
                                end
                        )
        );
    }

    @Transactional(readOnly = true)
    public List<TaskResponseDto> getTasksByCompletedDate(
            LocalDate date,
            ZoneId zoneId
    ) {
        Instant start = toStartOfDay(date, zoneId);
        Instant end = toStartOfDay(date.plusDays(1), zoneId);

        return toResponseList(
                taskRepository
                        .findByCompletedAtGreaterThanEqualAndCompletedAtLessThanAndDeletedFalse(
                                start,
                                end
                        )
        );
    }

    @Transactional(readOnly = true)
    public List<TaskResponseDto> searchTasksByDescription(
            String description
    ) {
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException(
                    "Description search value must not be blank"
            );
        }

        return toResponseList(
                taskRepository
                        .findByDescriptionContainingIgnoreCaseAndDeletedFalse(
                                description.trim()
                        )
        );
    }
}
