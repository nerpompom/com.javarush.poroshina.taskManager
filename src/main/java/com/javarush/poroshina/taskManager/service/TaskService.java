package com.javarush.poroshina.taskManager.service;

import com.javarush.poroshina.taskManager.config.AppConstants;
import com.javarush.poroshina.taskManager.exception.TaskNotFoundException;
import com.javarush.poroshina.taskManager.exception.UserNotFoundException;
import com.javarush.poroshina.taskManager.metrics.TaskCreatedEvent;
import com.javarush.poroshina.taskManager.model.TaskStatus;
import com.javarush.poroshina.taskManager.model.dto.TaskCreateRequestDto;
import com.javarush.poroshina.taskManager.model.dto.TaskResponseDto;
import com.javarush.poroshina.taskManager.model.dto.TaskUpdateRequestDto;
import com.javarush.poroshina.taskManager.model.entity.Task;
import com.javarush.poroshina.taskManager.model.entity.User;
import com.javarush.poroshina.taskManager.repository.TaskRepository;
import com.javarush.poroshina.taskManager.repository.UserRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    public TaskService(
            TaskRepository taskRepository,
            UserRepository userRepository,
            ApplicationEventPublisher eventPublisher
    ) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.eventPublisher = eventPublisher;
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
        User author = getCurrentUser();
        Instant now = Instant.now();

        Task task = new Task();
        task.setDescription(request.getDescription());
        task.setTaskStatus(TaskStatus.CREATED);
        task.setAuthor(author);
        task.setCreatedAt(now);
        task.setDescriptionUpdatedBy(author);
        task.setDescriptionUpdatedAt(now);
        task.setStatusUpdatedBy(author);
        task.setStatusUpdatedAt(now);
        task.setDeleted(false);
        task.setCompletedAt(null);

        Task savedTask = taskRepository.save(task);

        eventPublisher.publishEvent(new TaskCreatedEvent());
        return toResponse(savedTask);
    }

    @Transactional
    public TaskResponseDto updateTask(Long id, TaskUpdateRequestDto request) {
        Task task = getTaskById(id);

        if (task.isDeleted()) {
            throw new TaskNotFoundException(AppConstants.TASK_NOT_FOUND_MESSAGE + id);
        }

        if (request.getDescription() == null && request.getTaskStatus() == null && request.getExecutorId() == null) {
            throw new IllegalStateException(AppConstants.EMPTY_TASK_UPDATE_MESSAGE);
        }

        User currentUser = getCurrentUser();
        Instant now = Instant.now();
        TaskStatus currentStatus = task.getTaskStatus();
        TaskStatus targetStatus = request.getTaskStatus() != null ? request.getTaskStatus() : currentStatus;
        boolean statusChanging = request.getTaskStatus() != null && request.getTaskStatus() != currentStatus;

        if (targetStatus == TaskStatus.DONE && currentStatus != TaskStatus.IN_PROGRESS && currentStatus != TaskStatus.DONE) {
            throw new IllegalStateException(AppConstants.ONLY_IN_PROGRESS_TASK_CAN_BE_COMPLETED_MESSAGE);
        }

        if (request.getDescription() != null) {
            if (request.getDescription().isBlank()) {
                throw new IllegalStateException(AppConstants.BLANK_DESCRIPTION_MASSAGE);
            }

            if (!Objects.equals(task.getDescription(), request.getDescription())) {
                task.setDescription(request.getDescription());
                task.setDescriptionUpdatedBy(currentUser);
                task.setDescriptionUpdatedAt(now);
            }
        }

        if (statusChanging) {
            applyStatusTransition(task, currentStatus, targetStatus, request.getExecutorId(), currentUser, now);
        } else {
            applyExecutorChange(task, targetStatus, request.getExecutorId(), currentUser, now);
        }

        Task savedTask = taskRepository.save(task);

        return toResponse(savedTask);
    }

    @Transactional
    public void deleteTask(Long id) {
        Task task = getTaskById(id);

        if (task.isDeleted()) {
            throw new TaskNotFoundException(AppConstants.TASK_NOT_FOUND_MESSAGE + id);
        }

        User currentUser = getCurrentUser();

        task.setDeleted(true);
        task.setDeletedBy(currentUser);
        task.setExecutor(null);
        task.setCompletedAt(null);
        taskRepository.save(task);
    }

    private void applyStatusTransition(
            Task task,
            TaskStatus currentStatus,
            TaskStatus targetStatus,
            Long requestedExecutorId,
            User currentUser,
            Instant now
    ) {
        task.setTaskStatus(targetStatus);
        task.setStatusUpdatedBy(currentUser);
        task.setStatusUpdatedAt(now);

        if (targetStatus == TaskStatus.CREATED) {
            if (requestedExecutorId != null) {
                throw new IllegalStateException(AppConstants.EXECUTOR_ID_MUST_BE_NULL_FOR_CREATED_MESSAGE);
            }

            if (task.getExecutor() != null) {
                task.setExecutor(null);
                task.setExecutorUpdatedBy(currentUser);
                task.setExecutorUpdatedAt(now);
            }

            task.setCompletedAt(null);
            return;
        }

        User executor = resolveExecutorForAssignedStatus(task, requestedExecutorId);

        if (!sameExecutor(task.getExecutor(), executor)) {
            task.setExecutor(executor);
            task.setExecutorUpdatedBy(currentUser);
            task.setExecutorUpdatedAt(now);
        }

        if (targetStatus == TaskStatus.IN_PROGRESS) {
            task.setCompletedAt(null);
        }

        if (targetStatus == TaskStatus.DONE && currentStatus != TaskStatus.DONE) {
            task.setCompletedAt(now);
        }
    }

    private void applyExecutorChange(
            Task task,
            TaskStatus targetStatus,
            Long requestedExecutorId,
            User currentUser,
            Instant now
    ) {
        if (requestedExecutorId == null) {
            return;
        }

        if (targetStatus == TaskStatus.CREATED) {
            throw new IllegalStateException(AppConstants.EXECUTOR_ID_MUST_BE_NULL_FOR_CREATED_MESSAGE);
        }

        User executor = getUserById(requestedExecutorId);

        if (!sameExecutor(task.getExecutor(), executor)) {
            task.setExecutor(executor);
            task.setExecutorUpdatedBy(currentUser);
            task.setExecutorUpdatedAt(now);
        }
    }

    private User resolveExecutorForAssignedStatus(Task task, Long requestedExecutorId) {
        if (requestedExecutorId != null) {
            return getUserById(requestedExecutorId);
        }

        if (task.getExecutor() == null) {
            throw new IllegalStateException(
                    task.getTaskStatus() == TaskStatus.DONE
                            ? AppConstants.EXECUTOR_ID_REQUIRED_FOR_DONE_MESSAGE
                            : AppConstants.EXECUTOR_ID_REQUIRED_FOR_IN_PROGRESS_MESSAGE
            );
        }

        return task.getExecutor();
    }

    private boolean sameExecutor(User currentExecutor, User newExecutor) {
        Long currentId = currentExecutor != null ? currentExecutor.getId() : null;
        Long newId = newExecutor != null ? newExecutor.getId() : null;

        return Objects.equals(currentId, newId);
    }

    private Task getTaskById(Long id) {
        return taskRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new TaskNotFoundException(AppConstants.TASK_NOT_FOUND_MESSAGE + id));
    }

    private User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(AppConstants.USER_NOT_FOUND_MESSAGE + id));
    }

    private TaskResponseDto toResponse(Task task) {
        User author = task.getAuthor();
        User executor = task.getExecutor();
        User descriptionUpdatedBy = task.getDescriptionUpdatedBy();
        User statusUpdatedBy = task.getStatusUpdatedBy();
        User executorUpdatedBy = task.getExecutorUpdatedBy();

        return new TaskResponseDto(
                task.getId(),
                task.getDescription(),
                task.getTaskStatus(),
                author.getId(),
                author.getUsername(),
                executor != null ? executor.getId() : null,
                executor != null ? executor.getUsername() : null,
                task.getCreatedAt(),
                descriptionUpdatedBy != null ? descriptionUpdatedBy.getId() : null,
                descriptionUpdatedBy != null ? descriptionUpdatedBy.getUsername() : null,
                task.getDescriptionUpdatedAt(),
                statusUpdatedBy != null ? statusUpdatedBy.getId() : null,
                statusUpdatedBy != null ? statusUpdatedBy.getUsername() : null,
                task.getStatusUpdatedAt(),
                executorUpdatedBy != null ? executorUpdatedBy.getId() : null,
                executorUpdatedBy != null ? executorUpdatedBy.getUsername() : null,
                task.getExecutorUpdatedAt(),
                task.getCompletedAt(),
                task.isDeleted()
        );
    }

    private List<TaskResponseDto> toResponseList(List<Task> tasks) {
        return tasks.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TaskResponseDto> getTasksByAuthorId(Long authorId) {
        getUserById(authorId);

        return toResponseList(taskRepository.findByAuthor_IdAndDeletedFalse(authorId));
    }

    @Transactional(readOnly = true)
    public List<TaskResponseDto> getTasksByExecutorId(Long executorId) {
        getUserById(executorId);

        return toResponseList(taskRepository.findByExecutor_IdAndDeletedFalse(executorId));
    }

    @Transactional(readOnly = true)
    public List<TaskResponseDto> getTasksWithoutExecutor() {
        return toResponseList(taskRepository.findByExecutorIsNullAndDeletedFalse());
    }

    @Transactional(readOnly = true)
    public List<TaskResponseDto> getTasksByStatus(TaskStatus taskStatus) {
        return toResponseList(taskRepository.findByTaskStatusAndDeletedFalse(taskStatus));
    }

    private Instant toStartOfDay(LocalDate date, ZoneId zoneId) {
        return date
                .atStartOfDay(zoneId)
                .toInstant();
    }

    @Transactional(readOnly = true)
    public List<TaskResponseDto> getTasksByCreatedDate(LocalDate date, ZoneId zoneId) {
        Instant start = toStartOfDay(date, zoneId);
        Instant end = toStartOfDay(date.plusDays(1), zoneId);

        return toResponseList(taskRepository.findByCreatedAtGreaterThanEqualAndCreatedAtLessThanAndDeletedFalse(start, end));
    }

    @Transactional(readOnly = true)
    public List<TaskResponseDto> getTasksByUpdatedDate(LocalDate date, ZoneId zoneId
    ) {
        Instant start = toStartOfDay(date, zoneId);
        Instant end = toStartOfDay(date.plusDays(1), zoneId);

        return toResponseList(taskRepository.findUpdatedBetweenAndDeletedFalse(start, end));
    }

    @Transactional(readOnly = true)
    public List<TaskResponseDto> getTasksByCompletedDate(LocalDate date, ZoneId zoneId) {
        Instant start = toStartOfDay(date, zoneId);
        Instant end = toStartOfDay(date.plusDays(1), zoneId);

        return toResponseList(taskRepository.findByCompletedAtGreaterThanEqualAndCompletedAtLessThanAndDeletedFalse(start, end));
    }

    @Transactional(readOnly = true)
    public List<TaskResponseDto> searchTasksByDescription(String description) {
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException(AppConstants.BLANK_DESCRIPTION_SEARCH_MESSAGE);
        }

        return toResponseList(taskRepository.findByDescriptionContainingIgnoreCaseAndDeletedFalse(description.trim()));
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException(AppConstants.CURRENT_USER_NOT_AUTHENTICATED_MESSAGE);
        }

        String username = authentication.getName();

        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UserNotFoundException(AppConstants.AUTHENTICATED_USER_NOT_FOUND_MESSAGE);
        }

        return user;
    }
}
