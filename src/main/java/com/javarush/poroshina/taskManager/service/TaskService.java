package com.javarush.poroshina.taskManager.service;

import com.javarush.poroshina.taskManager.exception.TaskNotFoundException;
import com.javarush.poroshina.taskManager.exception.UserNotFoundException;
import com.javarush.poroshina.taskManager.model.TaskStatus;
import com.javarush.poroshina.taskManager.model.dto.TaskRequest;
import com.javarush.poroshina.taskManager.model.dto.TaskResponse;
import com.javarush.poroshina.taskManager.model.entity.Task;
import com.javarush.poroshina.taskManager.model.entity.User;
import com.javarush.poroshina.taskManager.repository.TaskRepository;
import com.javarush.poroshina.taskManager.repository.UserRepository;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
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
    public TaskResponse getTaskResponseById(Long id) {
        return toResponse(getTaskById(id));
    }

    @Transactional(readOnly = true)
    public List<TaskResponse> getAllTaskResponses() {
        return taskRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public TaskResponse createTask(TaskRequest request) {
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
    public TaskResponse updateTask(Long id, TaskRequest request) {
        Task task = getTaskById(id);

        if (task.isDeleted()) {
            throw new IllegalStateException("Deleted task cannot be changed");
        }

        task.setDescription(request.getDescription());

        // createdAt, author и статус здесь не меняются.
        // updatedAt будет установлен через @PreUpdate.
        Task savedTask = taskRepository.save(task);

        return toResponse(savedTask);
    }

    @Transactional
    public void deleteTask(Long id) {
        Task task = getTaskById(id);

        if (task.isDeleted()) {
            throw new IllegalStateException("Task is already deleted");
        }

        task.setDeleted(true);
        task.setTaskStatus(TaskStatus.DELETED);
        task.setUpdatedAt(Instant.now());

        taskRepository.save(task);
    }

    @Transactional
    public TaskResponse takeTaskInProgress(Long taskId, Long executorId) {
        Task task = getTaskById(taskId);
        User executor = getUserById(executorId);

        if (task.isDeleted()) {
            throw new IllegalStateException(
                    "Deleted task cannot be taken in progress"
            );
        }

        if (task.getCompletedAt() != null) {
            throw new IllegalStateException(
                    "Completed task cannot be taken in progress"
            );
        }

        task.setExecutor(executor);
        task.setTaskStatus(TaskStatus.IN_PROGRESS);
        task.setUpdatedAt(Instant.now());

        Task savedTask = taskRepository.save(task);

        return toResponse(savedTask);
    }

    @Transactional
    public TaskResponse completeTask(Long id) {
        Task task = getTaskById(id);

        if (task.isDeleted()) {
            throw new IllegalStateException(
                    "Deleted task cannot be completed"
            );
        }

        if (task.getTaskStatus() != TaskStatus.IN_PROGRESS) {
            throw new IllegalStateException(
                    "Only task in progress can be completed"
            );
        }

        Instant now = Instant.now();

        task.setTaskStatus(TaskStatus.DONE);
        task.setCompletedAt(now);
        task.setUpdatedAt(now);

        Task savedTask = taskRepository.save(task);

        return toResponse(savedTask);
    }

    @Transactional
    public TaskResponse returnTaskToCreated(Long id) {
        Task task = getTaskById(id);

        if (task.isDeleted()) {
            throw new IllegalStateException(
                    "Deleted task cannot be returned to CREATED status"
            );
        }

        task.setTaskStatus(TaskStatus.CREATED);
        task.setCompletedAt(null);
        task.setExecutor(null);
        task.setUpdatedAt(Instant.now());

        Task savedTask = taskRepository.save(task);

        return toResponse(savedTask);
    }

    private Task getTaskById(Long id) {
        return taskRepository.findById(id)
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

    private TaskResponse toResponse(Task task) {
        User author = task.getAuthor();
        User executor = task.getExecutor();

        return new TaskResponse(
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
}
