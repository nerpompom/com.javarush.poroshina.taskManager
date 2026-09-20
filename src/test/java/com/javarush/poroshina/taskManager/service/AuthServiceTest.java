package com.javarush.poroshina.taskManager.service;

import com.javarush.poroshina.taskManager.exception.InvalidCredentialsException;
import com.javarush.poroshina.taskManager.metrics.UserLoginEvent;
import com.javarush.poroshina.taskManager.model.dto.AuthRequestDto;
import com.javarush.poroshina.taskManager.model.dto.AuthResponseDto;
import com.javarush.poroshina.taskManager.model.dto.UserRequestDto;
import com.javarush.poroshina.taskManager.model.dto.UserResponseDto;
import com.javarush.poroshina.taskManager.model.entity.Role;
import com.javarush.poroshina.taskManager.model.entity.User;
import com.javarush.poroshina.taskManager.repository.UserRepository;
import com.javarush.poroshina.taskManager.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private AuthService authService;

    @Test
    void shouldRegisterUserWithValidData() {
        // Arrange
        UserRequestDto request = new UserRequestDto();
        request.setUsername("new_user");
        request.setPassword("password123");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername("new_user");
        savedUser.setPassword("encoded-password");
        savedUser.setRole(Role.USER);

        when(userRepository.findByUsername("new_user"))
                .thenReturn(null);

        when(passwordEncoder.encode("password123"))
                .thenReturn("encoded-password");

        when(userRepository.save(any(User.class)))
                .thenReturn(savedUser);

        // Act
        UserResponseDto result =
                authService.register(request);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(
                "new_user",
                result.getUsername()
        );

        verify(userRepository)
                .findByUsername("new_user");

        verify(passwordEncoder)
                .encode("password123");

        verify(userRepository)
                .save(any(User.class));

        verify(eventPublisher, never())
                .publishEvent(any());
    }

    @Test
    void shouldThrowExceptionWhenUsernameAlreadyTaken() {
        // Arrange
        UserRequestDto request = new UserRequestDto();
        request.setUsername("existing_user");
        request.setPassword("password123");

        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setUsername("existing_user");

        when(userRepository.findByUsername("existing_user"))
                .thenReturn(existingUser);

        // Act
        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> authService.register(request)
                );

        // Assert
        assertEquals(
                "Username is already taken",
                exception.getMessage()
        );

        verify(userRepository)
                .findByUsername("existing_user");

        verify(userRepository, never())
                .save(any(User.class));

        verify(passwordEncoder, never())
                .encode(any());

        verifyNoInteractions(eventPublisher);
    }

    @Test
    void shouldLoginUserWithValidCredentials() {
        // Arrange
        AuthRequestDto request = new AuthRequestDto();
        request.setUsername("test_user");
        request.setPassword("password123");

        User user = new User();
        user.setId(1L);
        user.setUsername("test_user");
        user.setPassword("encoded-password");
        user.setRole(Role.USER);

        when(userRepository.findByUsername("test_user"))
                .thenReturn(user);

        when(passwordEncoder.matches(
                "password123",
                "encoded-password"
        )).thenReturn(true);

        when(jwtService.createToken(
                1L,
                "test_user"
        )).thenReturn("test-jwt-token");

        // Act
        AuthResponseDto result =
                authService.login(request);

        // Assert
        assertNotNull(result);
        assertEquals(
                "test-jwt-token",
                result.getToken()
        );

        verify(userRepository)
                .findByUsername("test_user");

        verify(passwordEncoder)
                .matches(
                        "password123",
                        "encoded-password"
                );

        verify(jwtService)
                .createToken(1L, "test_user");

        verify(eventPublisher)
                .publishEvent(any(UserLoginEvent.class));
    }

    @Test
    void shouldThrowExceptionWhenLoginCredentialsAreInvalid() {
        // Arrange
        AuthRequestDto request = new AuthRequestDto();
        request.setUsername("test_user");
        request.setPassword("wrong-password");

        User user = new User();
        user.setId(1L);
        user.setUsername("test_user");
        user.setPassword("encoded-password");

        when(userRepository.findByUsername("test_user"))
                .thenReturn(user);

        when(passwordEncoder.matches(
                "wrong-password",
                "encoded-password"
        )).thenReturn(false);

        // Act
        InvalidCredentialsException exception =
                assertThrows(
                        InvalidCredentialsException.class,
                        () -> authService.login(request)
                );

        // Assert
        assertEquals(
                "Invalid username or password",
                exception.getMessage()
        );

        verify(userRepository)
                .findByUsername("test_user");

        verify(passwordEncoder)
                .matches(
                        "wrong-password",
                        "encoded-password"
                );

        verify(jwtService, never())
                .createToken(any(), any());

        verifyNoInteractions(eventPublisher);
    }
}
