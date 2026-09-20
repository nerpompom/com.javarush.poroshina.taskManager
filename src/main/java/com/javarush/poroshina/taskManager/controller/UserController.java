package com.javarush.poroshina.taskManager.controller;

import com.javarush.poroshina.taskManager.model.TaskStatus;
import com.javarush.poroshina.taskManager.model.dto.UserResponseDto;
import com.javarush.poroshina.taskManager.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserResponseById(id));
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUserResponses());
    }

    @GetMapping("/with-authored-tasks")
    public ResponseEntity<List<UserResponseDto>> getUsersWithAuthoredTasks() {
        return ResponseEntity.ok(userService.getUsersWithAuthoredTasks());
    }

    @GetMapping("/by-authored-task/{taskId}")
    public ResponseEntity<UserResponseDto> getUserByAuthoredTask(@PathVariable Long taskId) {
        return ResponseEntity.ok(userService.getUserByAuthoredTaskId(taskId));
    }

    @GetMapping("/without-authored-tasks")
    public ResponseEntity<List<UserResponseDto>> getUsersWithoutAuthoredTasks() {
        return ResponseEntity.ok(userService.getUsersWithoutAuthoredTasks());
    }

    @GetMapping("/with-executed-tasks")
    public ResponseEntity<List<UserResponseDto>> getUsersWithExecutedTasks() {
        return ResponseEntity.ok(userService.getUsersWithExecutedTasks());
    }

    @GetMapping("/by-executed-task/{taskId}")
    public ResponseEntity<UserResponseDto> getUserByExecutedTask(@PathVariable Long taskId) {
        return ResponseEntity.ok(userService.getUserByExecutedTaskId(taskId));
    }

    @GetMapping("/without-executed-tasks")
    public ResponseEntity<List<UserResponseDto>> getUsersWithoutExecutedTasks() {
        return ResponseEntity.ok(userService.getUsersWithoutExecutedTasks());
    }

    @GetMapping("/with-tasks-by-status")
    public ResponseEntity<List<UserResponseDto>> getUsersWithTasksByStatus(@RequestParam TaskStatus status) {
        return ResponseEntity.ok(userService.getUsersWithTasksByStatus(status));
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserResponseDto>> searchUsers(@RequestParam String username) {
        return ResponseEntity.ok(userService.searchUsersByUsername(username));
    }
}
