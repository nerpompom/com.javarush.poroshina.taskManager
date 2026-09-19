package com.javarush.poroshina.taskManager.service;

import com.javarush.poroshina.taskManager.model.dto.UserRequestDto;
import com.javarush.poroshina.taskManager.model.dto.UserResponseDto;
import com.javarush.poroshina.taskManager.model.entity.Role;
import com.javarush.poroshina.taskManager.model.entity.User;
import com.javarush.poroshina.taskManager.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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
}
