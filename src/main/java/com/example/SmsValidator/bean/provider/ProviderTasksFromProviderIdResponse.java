package com.example.SmsValidator.bean.provider;

import com.example.SmsValidator.bean.BaseResponse;
import com.example.SmsValidator.dto.TaskDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProviderTasksFromProviderIdResponse extends BaseResponse {
    private List<TaskDto> tasks;
}
