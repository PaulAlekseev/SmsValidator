package com.example.SmsValidator.bean.servicetype;

import com.example.SmsValidator.model.ServiceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class GetAmountOfAllowedModemSuccessResponse extends GetAmountOfAllowedModemBaseResponse{

    private final ServiceType service;
    private final int amount;
}
