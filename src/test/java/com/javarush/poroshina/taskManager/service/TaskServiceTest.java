package com.javarush.poroshina.taskManager.service;

import com.javarush.poroshina.taskManager.exception.TaskNotFoundException;
import com.javarush.poroshina.taskManager.metrics.TaskCreatedEvent;
import com.javarush.poroshina.taskManager.model.TaskStatus;
import com.javarush.poroshina.taskManager.model.dto.TaskCreateRequestDto;
import com.javarush.poroshina.taskManager.model.dto.TaskResponseDto;
import com.javarush.poroshina.taskManager.model.dto.TaskUpdateRequestDto;
import com.javarush.poroshina.taskManager.model.entity.Task;
import com.javarush.poroshina.taskManager.model.entity.User;
import com.javarush.poroshina.taskManager.repository.TaskRepository;
import com.javarush.poroshina.taskManager.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private TaskService taskService;

    private User author;
    private Task task;

    @BeforeEach
    void setUp() {
        author = createUser(1L, "author");
        task = createTask(10L, "Test task", TaskStatus.CREATED, author);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldReturnTaskById() {
        when(taskRepository.findByIdAndDeletedFalse(10L)).thenReturn(Optional.of(task));
        TaskResponseDto result = taskService.getTaskResponseById(10L);

        assertNotNull(result);
        assertEquals(10L, result.getId());
        assertEquals("Test task", result.getDescription());
        assertEquals(TaskStatus.CREATED, result.getTaskStatus());
        assertEquals(1L, result.getAuthorId());
        assertEquals("author", result.getAuthorUsername());

        verify(taskRepository).findByIdAndDeletedFalse(10L);
    }

    @Test
    void shouldThrowExceptionWhenTaskDoesNotExist() {
        when(taskRepository.findByIdAndDeletedFalse(999L)).thenReturn(Optional.empty());

        TaskNotFoundException exception = assertThrows(TaskNotFoundException.class, () -> taskService.getTaskResponseById(999L));

        assertEquals("Task not found with id: 999", exception.getMessage());

        verify(taskRepository).findByIdAndDeletedFalse(999L);
    }

    @Test
    void shouldCreateTaskWithCurrentUserAsAuthor() {
        setAuthenticatedUser("author");

        TaskCreateRequestDto request = new TaskCreateRequestDto();

        request.setDescription("New task");

        when(userRepository.findByUsername("author")).thenReturn(author);

        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> {
            Task savedTask = invocation.getArgument(0);
            savedTask.setId(20L);
            return savedTask;
        });

        TaskResponseDto result = taskService.createTask(request);

        assertNotNull(result);
        assertEquals(20L, result.getId());
        assertEquals("New task", result.getDescription());
        assertEquals(TaskStatus.CREATED, result.getTaskStatus());
        assertEquals(1L, result.getAuthorId());
        assertEquals("author", result.getAuthorUsername());

        ArgumentCaptor<Task> taskCaptor = ArgumentCaptor.forClass(Task.class);

        verify(taskRepository).save(taskCaptor.capture());

        Task savedTask = taskCaptor.getValue();

        assertEquals("New task", savedTask.getDescription());
        assertEquals(TaskStatus.CREATED, savedTask.getTaskStatus());
        assertEquals(author, savedTask.getAuthor());
        assertEquals(false, savedTask.isDeleted());

        verify(eventPublisher).publishEvent(any(TaskCreatedEvent.class));
    }

    @Test
    void shouldUpdateTaskToInProgressAndThenToDone() {
        User executor = createUser(2L, "executor");

        TaskUpdateRequestDto inProgressRequest = new TaskUpdateRequestDto();

        inProgressRequest.setDescription("Task in progress");
        inProgressRequest.setTaskStatus(TaskStatus.IN_PROGRESS);
        inProgressRequest.setExecutorId(2L);

        when(taskRepository.findByIdAndDeletedFalse(10L)).thenReturn(Optional.of(task));
        when(userRepository.findById(2L)).thenReturn(Optional.of(executor));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TaskResponseDto inProgressResult = taskService.updateTask(10L, inProgressRequest);

        assertEquals(TaskStatus.IN_PROGRESS, inProgressResult.getTaskStatus());
        assertEquals("Task in progress", inProgressResult.getDescription());
        assertEquals(2L, inProgressResult.getExecutorId());
        assertEquals("executor", inProgressResult.getExecutorUsername());
        assertNotNull(inProgressResult.getUpdatedAt());
        assertEquals(null, inProgressResult.getCompletedAt());

        TaskUpdateRequestDto doneRequest = new TaskUpdateRequestDto();

        doneRequest.setDescription("Task completed");
        doneRequest.setTaskStatus(TaskStatus.DONE);
        doneRequest.setExecutorId(2L);

        TaskResponseDto doneResult = taskService.updateTask(10L, doneRequest);

        assertEquals(TaskStatus.DONE, doneResult.getTaskStatus());
        assertEquals("Task completed", doneResult.getDescription());
        assertEquals(2L, doneResult.getExecutorId());
        assertEquals("executor", doneResult.getExecutorUsername());
        assertNotNull(doneResult.getUpdatedAt());
        assertNotNull(doneResult.getCompletedAt());

        verify(taskRepository, org.mockito.Mockito.times(2)).findByIdAndDeletedFalse(10L);
        verify(userRepository, org.mockito.Mockito.times(2)).findById(2L);
        verify(taskRepository, org.mockito.Mockito.times(2)).save(any(Task.class));
    }

    private void setAuthenticatedUser(String username) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, null, List.of());

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private User createUser(Long id, String username) {
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setTasks(List.of());
        user.setExecutedTasks(List.of());

        return user;
    }

    private Task createTask(Long id, String description, TaskStatus status, User author
    ) {
        Task task = new Task();
        task.setId(id);
        task.setDescription(description);
        task.setTaskStatus(status);
        task.setAuthor(author);
        task.setCreatedAt(Instant.now());
        task.setDeleted(false);
        task.setCompletedAt(null);
        task.setUpdatedAt(null);

        return task;
    }
}
