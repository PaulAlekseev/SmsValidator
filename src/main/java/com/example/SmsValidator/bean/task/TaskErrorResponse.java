package com.example.SmsValidator.bean.task;

import com.example.SmsValidator.entity.TaskEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class TaskErrorResponse extends TaskBaseResponse{
    private final String errorMessage;
    public TaskErrorResponse(String errorMessage) {
        this.ok = false;
        this.errorMessage = errorMessage;
    }
}
