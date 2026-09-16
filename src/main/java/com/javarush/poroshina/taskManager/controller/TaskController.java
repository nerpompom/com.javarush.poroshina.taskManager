package com.javarush.poroshina.taskManager.controller;

import com.javarush.poroshina.taskManager.model.dto.TaskCreateRequestDto;
import com.javarush.poroshina.taskManager.model.dto.TaskResponseDto;
import com.javarush.poroshina.taskManager.model.dto.TaskUpdateRequestDto;
import com.javarush.poroshina.taskManager.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDto> getTaskById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                taskService.getTaskResponseById(id)
        );
    }

    @GetMapping
    public ResponseEntity<List<TaskResponseDto>> getAllTasks() {
        return ResponseEntity.ok(
                taskService.getAllTaskResponses()
        );
    }

    @PostMapping
    public ResponseEntity<TaskResponseDto> createTask(
            @Valid @RequestBody TaskCreateRequestDto request
    ) {
        TaskResponseDto created = taskService.createTask(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDto> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody TaskUpdateRequestDto request
    ) {
        return ResponseEntity.ok(
                taskService.updateTask(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(
            @PathVariable Long id
    ) {
        taskService.deleteTask(id);

        return ResponseEntity
                .ok("Task was deleted successfully");
    }

}
