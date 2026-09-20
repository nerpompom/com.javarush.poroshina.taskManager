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
    private final Instant updatedAt;
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
            Instant updatedAt,
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
        this.updatedAt = updatedAt;
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

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getCompletedAt() {
        return completedAt;
    }

    public Boolean getDeleted() {
        return deleted;
    }
}
