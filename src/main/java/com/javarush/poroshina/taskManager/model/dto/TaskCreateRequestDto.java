package com.javarush.poroshina.taskManager.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

//Заменить название на Task Create Request
public class TaskCreateRequestDto {

    @NotBlank(message = "Description must not be blank")
    @Size(max = 1000, message = "Description must be at most 1000 characters")
    private String description;

    public TaskCreateRequestDto() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
