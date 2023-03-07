package com.example.SmsValidator.dto;

import lombok.Data;

@Data
public class TaskDto {
    private Long id;
    private String serviceName;
    private int cost;
    private boolean done;
    private boolean success;
}
