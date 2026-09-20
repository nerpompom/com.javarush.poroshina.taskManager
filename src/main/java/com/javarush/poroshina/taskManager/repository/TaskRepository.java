package com.javarush.poroshina.taskManager.repository;

import com.javarush.poroshina.taskManager.model.TaskStatus;
import com.javarush.poroshina.taskManager.model.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {

    Optional<Task> findByIdAndDeletedFalse(Long id);

    List<Task> findAllByDeletedFalse();

    List<Task> findByAuthor_IdAndDeletedFalse(Long authorId);

    List<Task> findByExecutor_IdAndDeletedFalse(Long executorId);

    List<Task> findByExecutorIsNullAndDeletedFalse();

    List<Task> findByTaskStatusAndDeletedFalse(TaskStatus taskStatus);

    List<Task> findByCreatedAtGreaterThanEqualAndCreatedAtLessThanAndDeletedFalse(Instant start, Instant end);

    List<Task> findByUpdatedAtGreaterThanEqualAndUpdatedAtLessThanAndDeletedFalse(Instant start, Instant end);

    List<Task> findByCompletedAtGreaterThanEqualAndCompletedAtLessThanAndDeletedFalse(Instant start, Instant end);

    List<Task> findByDescriptionContainingIgnoreCaseAndDeletedFalse(String description);
}
