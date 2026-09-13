package com.javarush.poroshina.taskManager.model.dto;

//Надо реализовать также чтобы можно было видеть список задач пользователя, где он автор и где исполнитель

public class UserResponse {
    private final Long id;
    private final String username;

    public UserResponse(Long id, String username) {
        this.id = id;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public Long getId() {
        return id;
    }
}
