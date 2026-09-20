package com.javarush.poroshina.taskManager.service;

import com.javarush.poroshina.taskManager.model.dto.UserRequestDto;
import com.javarush.poroshina.taskManager.model.dto.UserResponseDto;
import com.javarush.poroshina.taskManager.model.entity.Role;
import com.javarush.poroshina.taskManager.model.entity.User;
import com.javarush.poroshina.taskManager.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.javarush.poroshina.taskManager.exception.InvalidCredentialsException;
import com.javarush.poroshina.taskManager.metrics.UserLoginEvent;
import com.javarush.poroshina.taskManager.security.JwtService;
import org.springframework.context.ApplicationEventPublisher;
import com.javarush.poroshina.taskManager.model.dto.AuthRequestDto;
import com.javarush.poroshina.taskManager.model.dto.AuthResponseDto;

import java.util.List;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final ApplicationEventPublisher eventPublisher;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            ApplicationEventPublisher eventPublisher
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public UserResponseDto register(UserRequestDto request) {
        if (userRepository.findByUsername(request.getUsername())
                != null) {
            throw new IllegalArgumentException(
                    "Username is already taken"
            );
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );
        user.setRole(Role.USER);

        User savedUser = userRepository.save(user);

        return new UserResponseDto(
                savedUser.getId(),
                savedUser.getUsername(),
                List.of(),
                List.of()
        );
    }

    public AuthResponseDto login(
            AuthRequestDto request
    ) {
        User user = userRepository.findByUsername(
                request.getUsername()
        );

        if (user == null
                || !passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        )) {
            throw new InvalidCredentialsException(
                    "Invalid username or password"
            );
        }

        String token = jwtService.createToken(
                user.getId(),
                user.getUsername()
        );

        eventPublisher.publishEvent(
                new UserLoginEvent(user.getId())
        );

        return new AuthResponseDto(token);
    }
}
