package com.javarush.poroshina.taskManager.service;

import com.javarush.poroshina.taskManager.exception.UserNotFoundException;
import com.javarush.poroshina.taskManager.model.TaskStatus;
import com.javarush.poroshina.taskManager.model.dto.UserResponseDto;
import com.javarush.poroshina.taskManager.model.entity.Task;
import com.javarush.poroshina.taskManager.model.entity.User;
import com.javarush.poroshina.taskManager.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    //DTO для получения одного пользователя
    @Transactional(readOnly = true)
    public UserResponseDto getUserResponseById(Long id) {
        User user = getUserById(id);
        return toResponse(user);
    }

    private User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    // если нужно и список DTO
    @Transactional(readOnly = true)
    public List<UserResponseDto> getAllUserResponses() {
        return userRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private UserResponseDto toResponse(User user) {
        List<Long> authoredTaskIds = user.getTasks()
                .stream()
                .filter(task -> !task.isDeleted())
                .map(Task::getId)
                .collect(Collectors.toList());

        List<Long> executedTaskIds = user.getExecutedTasks()
                .stream()
                .filter(task -> !task.isDeleted())
                .map(Task::getId)
                .collect(Collectors.toList());

        return new UserResponseDto(
                user.getId(),
                user.getUsername(),
                authoredTaskIds,
                executedTaskIds
        );
    }

    private List<UserResponseDto> toResponseList(
            List<User> users
    ) {
        return users.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UserResponseDto> getUsersWithAuthoredTasks() {
        return toResponseList(
                userRepository.findUsersWithAuthoredTasks()
        );
    }

    @Transactional(readOnly = true)
    public UserResponseDto getUserByAuthoredTaskId(Long taskId) {
        User user = userRepository.findUserByAuthoredTaskId(taskId)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "Author not found for task with id: " + taskId
                        )
                );

        return toResponse(user);
    }

    @Transactional(readOnly = true)
    public List<UserResponseDto> getUsersWithoutAuthoredTasks() {
        return toResponseList(
                userRepository.findUsersWithoutAuthoredTasks()
        );
    }

    @Transactional(readOnly = true)
    public List<UserResponseDto> getUsersWithExecutedTasks() {
        return toResponseList(
                userRepository.findUsersWithExecutedTasks()
        );
    }

    @Transactional(readOnly = true)
    public UserResponseDto getUserByExecutedTaskId(
            Long taskId
    ) {
        User user = userRepository.findUserByExecutedTaskId(taskId)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "Executor not found for task with id: "
                                        + taskId
                        )
                );

        return toResponse(user);
    }

    @Transactional(readOnly = true)
    public List<UserResponseDto> getUsersWithoutExecutedTasks() {
        return toResponseList(
                userRepository.findUsersWithoutExecutedTasks()
        );
    }

    @Transactional(readOnly = true)
    public List<UserResponseDto> getUsersWithTasksByStatus(
            TaskStatus status
    ) {
        return toResponseList(
                userRepository.findUsersWithExecutedTasksByStatus(status)
        );
    }

    @Transactional(readOnly = true)
    public List<UserResponseDto> searchUsersByUsername(
            String username
    ) {
        return toResponseList(
                userRepository.findByUsernameContainingIgnoreCase(username)
        );
    }
}
