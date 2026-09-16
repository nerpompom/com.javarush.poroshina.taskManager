package com.javarush.poroshina.taskManager.model.dto;

import java.util.List;

public class UserResponseDto {
    private final Long id;
    private final String username;
    private final List<Long> authoredTaskIds;
    private final List<Long> executedTaskIds;

    public UserResponseDto(
            Long id,
            String username,
            List<Long> authoredTaskIds,
            List<Long> executedTaskIds
    ) {
        this.id = id;
        this.username = username;
        this.authoredTaskIds = authoredTaskIds;
        this.executedTaskIds = executedTaskIds;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public List<Long> getAuthoredTaskIds() {
        return authoredTaskIds;
    }

    public List<Long> getExecutedTaskIds() {
        return executedTaskIds;
    }
}
