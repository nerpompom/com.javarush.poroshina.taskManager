package com.javarush.poroshina.taskManager.service;

import com.javarush.poroshina.taskManager.exception.UserNotFoundException;
import com.javarush.poroshina.taskManager.model.dto.UserResponseDto;
import com.javarush.poroshina.taskManager.model.entity.Task;
import com.javarush.poroshina.taskManager.model.entity.User;
import com.javarush.poroshina.taskManager.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldReturnUserById() {
        // Arrange
        Long userId = 1L;

        User user = new User();
        user.setId(userId);
        user.setUsername("test_user");
        user.setTasks(List.of());
        user.setExecutedTasks(List.of());

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        // Act
        UserResponseDto result =
                userService.getUserResponseById(userId);

        // Assert
        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals(
                "test_user",
                result.getUsername()
        );
        assertEquals(
                List.of(),
                result.getAuthoredTaskIds()
        );
        assertEquals(
                List.of(),
                result.getExecutedTaskIds()
        );

        verify(userRepository)
                .findById(userId);
    }

    @Test
    void shouldThrowExceptionWhenUserDoesNotExist() {
        // Arrange
        Long userId = 999L;

        when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        // Act and Assert
        UserNotFoundException exception =
                assertThrows(
                        UserNotFoundException.class,
                        () -> userService.getUserResponseById(userId)
                );

        assertEquals(
                "User not found with id: 999",
                exception.getMessage()
        );

        verify(userRepository)
                .findById(userId);
    }

    @Test
    void shouldFindUsersByUsername() {
        // Arrange
        String searchText = "ann";

        User firstUser = new User();
        firstUser.setId(1L);
        firstUser.setUsername("anna");
        firstUser.setTasks(List.of());
        firstUser.setExecutedTasks(List.of());

        User secondUser = new User();
        secondUser.setId(2L);
        secondUser.setUsername("joanna");
        secondUser.setTasks(List.of());
        secondUser.setExecutedTasks(List.of());

        when(userRepository
                .findByUsernameContainingIgnoreCase(searchText))
                .thenReturn(List.of(firstUser, secondUser));

        // Act
        List<UserResponseDto> result =
                userService.searchUsersByUsername(searchText);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());

        assertEquals(
                1L,
                result.get(0).getId()
        );
        assertEquals(
                "anna",
                result.get(0).getUsername()
        );

        assertEquals(
                2L,
                result.get(1).getId()
        );
        assertEquals(
                "joanna",
                result.get(1).getUsername()
        );

        verify(userRepository)
                .findByUsernameContainingIgnoreCase(searchText);
    }

    @Test
    void shouldReturnEmptyListWhenUsernameSearchHasNoMatches() {
        // Arrange
        String searchText = "unknown";

        when(userRepository
                .findByUsernameContainingIgnoreCase(searchText))
                .thenReturn(List.of());

        // Act
        List<UserResponseDto> result =
                userService.searchUsersByUsername(searchText);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());

        verify(userRepository)
                .findByUsernameContainingIgnoreCase(searchText);
    }
}
