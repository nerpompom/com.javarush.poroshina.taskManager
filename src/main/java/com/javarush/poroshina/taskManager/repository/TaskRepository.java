package com.javarush.poroshina.taskManager.repository;

import com.javarush.poroshina.taskManager.model.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByAuthorId(Long authorId);

    List<Task> findByExecutorId(Long executorId);
}
