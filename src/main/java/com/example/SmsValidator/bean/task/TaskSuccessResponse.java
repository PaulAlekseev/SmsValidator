package com.example.SmsValidator.bean.task;

import com.example.SmsValidator.model.Modem;
import com.example.SmsValidator.model.Task;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TaskSuccessResponse extends TaskBaseResponse{
    private final Modem modem;
    private final Task task;
}
