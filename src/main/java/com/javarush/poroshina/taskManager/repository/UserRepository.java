package com.javarush.poroshina.taskManager.repository;

import com.javarush.poroshina.taskManager.model.TaskStatus;
import com.javarush.poroshina.taskManager.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    List<User> findByUsernameContainingIgnoreCase(
            String username
    );

    @Query("""
            select distinct u
            from User u
            join u.tasks t
            where t.deleted = false
            """)
    List<User> findUsersWithAuthoredTasks();

    @Query("""
        select u
        from User u
        join u.tasks t
        where t.id = :taskId
          and t.deleted = false
        """)
    Optional<User> findUserByAuthoredTaskId(
            @Param("taskId") Long taskId
    );

    @Query("""
            select u
            from User u
            where not exists (
                select t.id
                from Task t
                where t.author = u
                  and t.deleted = false
            )
            """)
    List<User> findUsersWithoutAuthoredTasks();

    @Query("""
            select distinct u
            from User u
            join u.executedTasks t
            where t.deleted = false
            """)
    List<User> findUsersWithExecutedTasks();

    @Query("""
        select u
        from User u
        join u.executedTasks t
        where t.id = :taskId
          and t.deleted = false
        """)
    Optional<User> findUserByExecutedTaskId(
            @Param("taskId") Long taskId
    );

    @Query("""
            select u
            from User u
            where not exists (
                select t.id
                from Task t
                where t.executor = u
                  and t.deleted = false
            )
            """)
    List<User> findUsersWithoutExecutedTasks();

    @Query("""
            select distinct u
            from User u
            join u.executedTasks t
            where t.taskStatus = :status
              and t.deleted = false
            """)
    List<User> findUsersWithExecutedTasksByStatus(
            @Param("status") TaskStatus status
    );
}
