package com.javarush.poroshina.taskManager.controller;

import com.javarush.poroshina.taskManager.exception.InvalidCredentialsException;
import com.javarush.poroshina.taskManager.model.dto.AuthRequestDto;
import com.javarush.poroshina.taskManager.model.dto.AuthResponseDto;
import com.javarush.poroshina.taskManager.model.dto.UserRequestDto;
import com.javarush.poroshina.taskManager.model.dto.UserResponseDto;
import com.javarush.poroshina.taskManager.model.entity.User;
import com.javarush.poroshina.taskManager.repository.UserRepository;
import com.javarush.poroshina.taskManager.security.JwtService;
import com.javarush.poroshina.taskManager.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;

    public AuthController(
            JwtService jwtService,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthService authService
    ) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(@Valid @RequestBody UserRequestDto request) {
        UserResponseDto created = authService.register(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(created);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody AuthRequestDto request) {
        User user = userRepository.findByUsername(request.getUsername());

        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid username or password");
        }

        String token = jwtService.createToken(user.getId(), user.getUsername());

        return ResponseEntity.ok(
                new AuthResponseDto(token)
        );
    }
}
