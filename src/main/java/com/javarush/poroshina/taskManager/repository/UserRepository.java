package com.javarush.poroshina.taskManager.repository;

import com.javarush.poroshina.taskManager.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
