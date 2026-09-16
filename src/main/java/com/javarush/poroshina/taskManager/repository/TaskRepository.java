package com.javarush.poroshina.taskManager.repository;

import com.javarush.poroshina.taskManager.model.TaskStatus;
import com.javarush.poroshina.taskManager.model.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByAuthorId(Long authorId);

    List<Task> findByExecutorId(Long executorId);

    Optional<Task> findByIdAndDeletedFalse(Long id);

    // Все неудалённые задачи
    List<Task> findAllByDeletedFalse();

    // Автор конкретный пользователь
    List<Task> findByAuthor_IdAndDeletedFalse(Long authorId);

    // Исполнитель конкретный пользователь
    List<Task> findByExecutor_IdAndDeletedFalse(Long executorId);

    // Задачи без исполнителя
    List<Task> findByExecutorIsNullAndDeletedFalse();

    // Задачи определённого статуса
    List<Task> findByTaskStatusAndDeletedFalse(
            TaskStatus taskStatus
    );

    // Дата создания
    List<Task> findByCreatedAtGreaterThanEqualAndCreatedAtLessThanAndDeletedFalse(
            Instant start,
            Instant end
    );

    // Дата изменения
    List<Task> findByUpdatedAtGreaterThanEqualAndUpdatedAtLessThanAndDeletedFalse(
            Instant start,
            Instant end
    );

    // Дата завершения
    List<Task> findByCompletedAtGreaterThanEqualAndCompletedAtLessThanAndDeletedFalse(
            Instant start,
            Instant end
    );

    // Частичное совпадение в описании
    List<Task> findByDescriptionContainingIgnoreCaseAndDeletedFalse(
            String description
    );
}
