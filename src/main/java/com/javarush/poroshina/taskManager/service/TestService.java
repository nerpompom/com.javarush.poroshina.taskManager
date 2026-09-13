package com.javarush.poroshina.taskManager.service;

import com.javarush.poroshina.taskManager.model.Task;
import com.javarush.poroshina.taskManager.model.User;
import com.javarush.poroshina.taskManager.repository.TaskRepository;
import com.javarush.poroshina.taskManager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    public void testDatabase() {
        User user = userRepository.findByUsername("test_1");
        System.out.println("User: " + user.getUsername());

        List<Task> tasks = taskRepository.findByAuthorId(user.getId());
        tasks.forEach(task -> System.out.println("Task: " + task.getDescription()));

        List<Task> executed_tasks = taskRepository.findByExecutorId(user.getId());
        executed_tasks.forEach(task -> System.out.println("Task: " + task.getDescription()));
    }
}
