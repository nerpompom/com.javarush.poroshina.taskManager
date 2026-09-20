package com.javarush.poroshina.taskManager.model.dto;

import com.javarush.poroshina.taskManager.config.AppConstants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserRequestDto {
    @NotBlank(message = AppConstants.BLANK_USERNAME_MESSAGE)
    @Size(min = 3, message = AppConstants.SMALL_USERNAME_MESSAGE)
    private String username;

    @NotBlank(message = AppConstants.BLANK_PASSWORD_MESSAGE)
    @Size(min = 3, message = AppConstants.SMALL_PASSWORD_MESSAGE)
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
