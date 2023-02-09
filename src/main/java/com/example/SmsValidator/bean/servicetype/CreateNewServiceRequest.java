package com.example.SmsValidator.bean.servicetype;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
public class CreateNewServiceRequest {
    private String name;
    private int allowedAmount;
    private int daysBetween;
    private String messageRegex;
    private String senderRegex;
    private Long timeSeconds;
}
