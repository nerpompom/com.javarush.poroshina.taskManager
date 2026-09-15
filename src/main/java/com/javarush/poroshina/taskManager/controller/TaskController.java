package com.javarush.poroshina.taskManager.controller;

import com.javarush.poroshina.taskManager.model.dto.TaskRequest;
import com.javarush.poroshina.taskManager.model.dto.TaskResponse;
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
    public ResponseEntity<TaskResponse> getTaskById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                taskService.getTaskResponseById(id)
        );
    }

    @GetMapping
    public ResponseEntity<List<TaskResponse>> getAllTasks() {
        return ResponseEntity.ok(
                taskService.getAllTaskResponses()
        );
    }

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(
            @Valid @RequestBody TaskRequest request
    ) {
        TaskResponse created = taskService.createTask(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(created);
    }

    //ОСТАНОВИЛАСЬ ТУТ
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody TaskRequest request
    ) {
        return ResponseEntity.ok(
                taskService.updateTask(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(
            @PathVariable Long id
    ) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/in-progress")
    public ResponseEntity<TaskResponse> takeTaskInProgress(
            @PathVariable Long id,
            @RequestParam Long executorId
    ) {
        return ResponseEntity.ok(
                taskService.takeTaskInProgress(id, executorId)
        );
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<TaskResponse> completeTask(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                taskService.completeTask(id)
        );
    }

    @PatchMapping("/{id}/reset")
    public ResponseEntity<TaskResponse> returnTaskToCreated(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                taskService.returnTaskToCreated(id)
        );
    }

}
