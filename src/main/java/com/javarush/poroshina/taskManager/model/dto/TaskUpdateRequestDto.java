package com.javarush.poroshina.taskManager.model.dto;

import com.javarush.poroshina.taskManager.config.AppConstants;
import com.javarush.poroshina.taskManager.model.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class TaskUpdateRequestDto {
    @NotBlank(message = AppConstants.BLANK_DESCRIPTION_MASSAGE)
    @Size(max = 1000, message = AppConstants.BIG_DESCRIPTION_MASSAGE)
    private String description;

    @NotNull(message = AppConstants.NULL_STATUS_MESSAGE)
    private TaskStatus taskStatus;

    private Long executorId;

    public TaskUpdateRequestDto() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public Long getExecutorId() {
        return executorId;
    }

    public void setExecutorId(Long executorId) {
        this.executorId = executorId;
    }

}
