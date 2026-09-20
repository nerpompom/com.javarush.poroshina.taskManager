package com.javarush.poroshina.taskManager.metrics;

public class UserLoginEvent {

    private final Long userId;

    public UserLoginEvent(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }
}
