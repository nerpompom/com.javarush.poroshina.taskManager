package com.javarush.poroshina.taskManager.controller;

import com.javarush.poroshina.taskManager.model.dto.UserRequestDto;
import com.javarush.poroshina.taskManager.model.dto.UserResponseDto;
import com.javarush.poroshina.taskManager.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserRequestDto request) {
        UserResponseDto created = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    //ОСТАНОВИЛАСЬ ТУТ
    @GetMapping("/with-authored-tasks")
    public ResponseEntity<List<UserResponseDto>>
    getUsersWithAuthoredTasks() {
        return ResponseEntity.ok(
                userService.getUsersWithAuthoredTasks()
        );
    }

    @GetMapping("/by-authored-task/{taskId}")
    public ResponseEntity<List<UserResponseDto>>
    getUsersByAuthoredTask(
            @PathVariable Long taskId
    ) {
        return ResponseEntity.ok(
                userService.getUsersByAuthoredTaskId(taskId)
        );
    }

    @GetMapping("/without-authored-tasks")
    public ResponseEntity<List<UserResponseDto>>
    getUsersWithoutAuthoredTasks() {
        return ResponseEntity.ok(
                userService.getUsersWithoutAuthoredTasks()
        );
    }

    @GetMapping("/with-executed-tasks")
    public ResponseEntity<List<UserResponseDto>>
    getUsersWithExecutedTasks() {
        return ResponseEntity.ok(
                userService.getUsersWithExecutedTasks()
        );
    }

    @GetMapping("/by-executed-task/{taskId}")
    public ResponseEntity<List<UserResponseDto>>
    getUsersByExecutedTask(
            @PathVariable Long taskId
    ) {
        return ResponseEntity.ok(
                userService.getUsersByExecutedTaskId(taskId)
        );
    }

    @GetMapping("/without-executed-tasks")
    public ResponseEntity<List<UserResponseDto>>
    getUsersWithoutExecutedTasks() {
        return ResponseEntity.ok(
                userService.getUsersWithoutExecutedTasks()
        );
    }

    @GetMapping("/with-in-progress-tasks")
    public ResponseEntity<List<UserResponseDto>>
    getUsersWithInProgressTasks() {
        return ResponseEntity.ok(
                userService.getUsersWithInProgressTasks()
        );
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserResponseDto>> searchUsers(
            @RequestParam String username
    ) {
        return ResponseEntity.ok(
                userService.searchUsersByUsername(username)
        );
    }

}
