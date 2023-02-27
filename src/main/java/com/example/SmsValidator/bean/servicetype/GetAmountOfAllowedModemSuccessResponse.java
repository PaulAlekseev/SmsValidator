package com.example.SmsValidator.bean.servicetype;

import com.example.SmsValidator.model.ServiceType;
import lombok.*;

@Data
@Builder
public class GetAmountOfAllowedModemSuccessResponse extends GetAmountOfAllowedModemBaseResponse{

    private final String service;
    private final int amount;
}
