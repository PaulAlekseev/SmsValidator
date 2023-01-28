package com.example.SmsValidator.bean.user;

import com.example.SmsValidator.bean.BaseResponse;
import com.example.SmsValidator.model.Task;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class UserTaskHistoryResponse extends BaseResponse {
    private List<Task> tasks;
}
