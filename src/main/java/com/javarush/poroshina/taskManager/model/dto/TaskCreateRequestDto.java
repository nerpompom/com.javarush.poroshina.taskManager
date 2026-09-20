package com.javarush.poroshina.taskManager.model.dto;

import com.javarush.poroshina.taskManager.config.AppConstants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class TaskCreateRequestDto {

    @NotBlank(message = AppConstants.BLANK_DESCRIPTION_MASSAGE)
    @Size(max = 1000, message = AppConstants.BIG_DESCRIPTION_MASSAGE)
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
