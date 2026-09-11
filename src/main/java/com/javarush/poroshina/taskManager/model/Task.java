package com.javarush.poroshina.taskManager.model;

import jakarta.persistence.*;

import java.time.Instant;

//@Entity
//@Table(name = "tasks")
//public class Task {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    long id;
//
//    @Column(nullable = false)
//    String description;
//
//    @Enumerated(EnumType.STRING)
//    @Column(name = "task_status", nullable = false)
//    TaskStatus taskStatus;
//
//    @ManyToOne(fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name = "author", nullable = false)
//    private User author;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "executor")
//    private User executor;
//
//    @Column(name = "created_at", nullable = false, updatable = false)
//    private Instant createdAt;
//
//    @Column(name = "updated_at")
//    private Instant updatedAt;
//
//    @Column(name = "completed_at")
//    private Instant completedAt;
//
//    @Column(name = "is_deleted", nullable = false)
//    boolean deleted;
//
//    @PrePersist
//    private void onCreate() {
//        if (this.createdAt == null) {
//            this.createdAt = Instant.now();
//        }
//    }
//}
