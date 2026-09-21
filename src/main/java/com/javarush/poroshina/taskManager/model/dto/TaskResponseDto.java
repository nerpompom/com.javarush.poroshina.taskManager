package com.javarush.poroshina.taskManager.model.dto;

import com.javarush.poroshina.taskManager.model.TaskStatus;
import java.time.Instant;

public class TaskResponseDto {

    private final Long id;
    private final String description;
    private final TaskStatus taskStatus;
    private final Long authorId;
    private final String authorUsername;
    private final Long executorId;
    private final String executorUsername;
    private final Instant createdAt;
    private final Long descriptionUpdatedById;
    private final String descriptionUpdatedByUsername;
    private final Instant descriptionUpdatedAt;
    private final Long statusUpdatedById;
    private final String statusUpdatedByUsername;
    private final Instant statusUpdatedAt;
    private final Long executorUpdatedById;
    private final String executorUpdatedByUsername;
    private final Instant executorUpdatedAt;
    private final Instant completedAt;
    private final Boolean deleted;

    public TaskResponseDto(
            Long id,
            String description,
            TaskStatus taskStatus,
            Long authorId,
            String authorUsername,
            Long executorId,
            String executorUsername,
            Instant createdAt,
            Long descriptionUpdatedById,
            String descriptionUpdatedByUsername,
            Instant descriptionUpdatedAt,
            Long statusUpdatedById,
            String statusUpdatedByUsername,
            Instant statusUpdatedAt,
            Long executorUpdatedById,
            String executorUpdatedByUsername,
            Instant executorUpdatedAt,
            Instant completedAt,
            Boolean deleted
    ) {
        this.id = id;
        this.description = description;
        this.taskStatus = taskStatus;
        this.authorId = authorId;
        this.authorUsername = authorUsername;
        this.executorId = executorId;
        this.executorUsername = executorUsername;
        this.createdAt = createdAt;
        this.descriptionUpdatedById = descriptionUpdatedById;
        this.descriptionUpdatedByUsername = descriptionUpdatedByUsername;
        this.descriptionUpdatedAt = descriptionUpdatedAt;
        this.statusUpdatedById = statusUpdatedById;
        this.statusUpdatedByUsername = statusUpdatedByUsername;
        this.statusUpdatedAt = statusUpdatedAt;
        this.executorUpdatedById = executorUpdatedById;
        this.executorUpdatedByUsername = executorUpdatedByUsername;
        this.executorUpdatedAt = executorUpdatedAt;
        this.completedAt = completedAt;
        this.deleted = deleted;
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public String getAuthorUsername() {
        return authorUsername;
    }

    public Long getExecutorId() {
        return executorId;
    }

    public String getExecutorUsername() {
        return executorUsername;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Long getDescriptionUpdatedById() {
        return descriptionUpdatedById;
    }

    public String getDescriptionUpdatedByUsername() {
        return descriptionUpdatedByUsername;
    }

    public Instant getDescriptionUpdatedAt() {
        return descriptionUpdatedAt;
    }

    public Long getStatusUpdatedById() {
        return statusUpdatedById;
    }

    public String getStatusUpdatedByUsername() {
        return statusUpdatedByUsername;
    }

    public Instant getStatusUpdatedAt() {
        return statusUpdatedAt;
    }

    public Long getExecutorUpdatedById() {
        return executorUpdatedById;
    }

    public String getExecutorUpdatedByUsername() {
        return executorUpdatedByUsername;
    }

    public Instant getExecutorUpdatedAt() {
        return executorUpdatedAt;
    }

    public Instant getCompletedAt() {
        return completedAt;
    }

    public Boolean getDeleted() {
        return deleted;
    }
}
