package com.javarush.poroshina.taskManager.service;

import com.javarush.poroshina.taskManager.exception.UserNotFoundException;
import com.javarush.poroshina.taskManager.model.dto.UserRequestDto;
import com.javarush.poroshina.taskManager.model.dto.UserResponseDto;
import com.javarush.poroshina.taskManager.model.entity.User;
import com.javarush.poroshina.taskManager.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //DTO для получения одного пользователя
    @Transactional(readOnly = true)
    public UserResponseDto getUserResponseById(Long id) {
        User user = getUserById(id);
        return new UserResponseDto(user.getId(), user.getUsername());
    }

    private User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    // если нужно и список DTO
    @Transactional(readOnly = true)
    public List<UserResponseDto> getAllUserResponses() {
        return userRepository.findAll()
                .stream()
                .map(u -> new UserResponseDto(u.getId(), u.getUsername()))
                .collect(Collectors.toList());
    }

    @Transactional
    public UserResponseDto createUser(UserRequestDto request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        User saved = userRepository.save(user);
        return new UserResponseDto(saved.getId(), saved.getUsername());
    }
}
