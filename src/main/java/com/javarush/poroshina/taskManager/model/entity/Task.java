package com.javarush.poroshina.taskManager.model.entity;

import com.javarush.poroshina.taskManager.model.TaskStatus;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "task_status", nullable = false)
    private TaskStatus taskStatus;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "executor_id")
    private User executor;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "description_updated_by_id")
    private User descriptionUpdatedBy;

    @Column(name = "description_updated_at")
    private Instant descriptionUpdatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_updated_by_id")
    private User statusUpdatedBy;

    @Column(name = "status_updated_at")
    private Instant statusUpdatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "executor_updated_by_id")
    private User executorUpdatedBy;

    @Column(name = "executor_updated_at")
    private Instant executorUpdatedAt;

    @Column(name = "completed_at")
    private Instant completedAt;

    @Column(name = "is_deleted", nullable = false)
    private Boolean deleted = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deleted_by_id")
    private User deletedBy;

    @PrePersist
    private void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = Instant.now();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public User getExecutor() {
        return executor;
    }

    public void setExecutor(User executor) {
        this.executor = executor;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public User getDescriptionUpdatedBy() {
        return descriptionUpdatedBy;
    }

    public void setDescriptionUpdatedBy(User descriptionUpdatedBy) {
        this.descriptionUpdatedBy = descriptionUpdatedBy;
    }

    public Instant getDescriptionUpdatedAt() {
        return descriptionUpdatedAt;
    }

    public void setDescriptionUpdatedAt(Instant descriptionUpdatedAt) {
        this.descriptionUpdatedAt = descriptionUpdatedAt;
    }

    public User getStatusUpdatedBy() {
        return statusUpdatedBy;
    }

    public void setStatusUpdatedBy(User statusUpdatedBy) {
        this.statusUpdatedBy = statusUpdatedBy;
    }

    public Instant getStatusUpdatedAt() {
        return statusUpdatedAt;
    }

    public void setStatusUpdatedAt(Instant statusUpdatedAt) {
        this.statusUpdatedAt = statusUpdatedAt;
    }

    public User getExecutorUpdatedBy() {
        return executorUpdatedBy;
    }

    public void setExecutorUpdatedBy(User executorUpdatedBy) {
        this.executorUpdatedBy = executorUpdatedBy;
    }

    public Instant getExecutorUpdatedAt() {
        return executorUpdatedAt;
    }

    public void setExecutorUpdatedAt(Instant executorUpdatedAt) {
        this.executorUpdatedAt = executorUpdatedAt;
    }

    public Instant getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(Instant completedAt) {
        this.completedAt = completedAt;
    }

    public Boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public User getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(User deletedBy) {
        this.deletedBy = deletedBy;
    }
}
