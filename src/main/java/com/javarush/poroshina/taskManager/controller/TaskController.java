package com.javarush.poroshina.taskManager.controller;

import com.javarush.poroshina.taskManager.model.TaskStatus;
import com.javarush.poroshina.taskManager.model.dto.TaskCreateRequestDto;
import com.javarush.poroshina.taskManager.model.dto.TaskResponseDto;
import com.javarush.poroshina.taskManager.model.dto.TaskUpdateRequestDto;
import com.javarush.poroshina.taskManager.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZoneId;
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

    //ОСТАНОВИЛАСЬ ТУТ
    @GetMapping("/by-author/{authorId}")
    public ResponseEntity<List<TaskResponseDto>> getTasksByAuthor(
            @PathVariable Long authorId
    ) {
        return ResponseEntity.ok(
                taskService.getTasksByAuthorId(authorId)
        );
    }

    @GetMapping("/by-executor/{executorId}")
    public ResponseEntity<List<TaskResponseDto>> getTasksByExecutor(
            @PathVariable Long executorId
    ) {
        return ResponseEntity.ok(
                taskService.getTasksByExecutorId(executorId)
        );
    }

    @GetMapping("/without-executor")
    public ResponseEntity<List<TaskResponseDto>> getTasksWithoutExecutor() {
        return ResponseEntity.ok(
                taskService.getTasksWithoutExecutor()
        );
    }

    @GetMapping("/by-status/{status}")
    public ResponseEntity<List<TaskResponseDto>> getTasksByStatus(
            @PathVariable TaskStatus status
    ) {
        return ResponseEntity.ok(
                taskService.getTasksByStatus(status)
        );
    }

    @GetMapping("/by-created-date")
    public ResponseEntity<List<TaskResponseDto>> getTasksByCreatedDate(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date
    ) {
        return ResponseEntity.ok(
                taskService.getTasksByCreatedDate(
                        date,
                        ZoneId.systemDefault()
                )
        );
    }

    @GetMapping("/by-updated-date")
    public ResponseEntity<List<TaskResponseDto>> getTasksByUpdatedDate(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date
    ) {
        return ResponseEntity.ok(
                taskService.getTasksByUpdatedDate(
                        date,
                        ZoneId.systemDefault()
                )
        );
    }

    @GetMapping("/by-completed-date")
    public ResponseEntity<List<TaskResponseDto>> getTasksByCompletedDate(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date
    ) {
        return ResponseEntity.ok(
                taskService.getTasksByCompletedDate(
                        date,
                        ZoneId.systemDefault()
                )
        );
    }

    @GetMapping("/search")
    public ResponseEntity<List<TaskResponseDto>> searchTasks(
            @RequestParam String description
    ) {
        return ResponseEntity.ok(
                taskService.searchTasksByDescription(description)
        );
    }

}
