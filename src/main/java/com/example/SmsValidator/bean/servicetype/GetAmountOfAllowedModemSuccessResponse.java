package com.example.SmsValidator.bean.servicetype;

import com.example.SmsValidator.model.ServiceType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetAmountOfAllowedModemSuccessResponse extends GetAmountOfAllowedModemBaseResponse{

    private ServiceType service;
    private int amountOfModems;
}
