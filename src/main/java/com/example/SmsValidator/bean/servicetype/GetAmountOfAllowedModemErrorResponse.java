package com.example.SmsValidator.bean.servicetype;

import lombok.Data;

@Data
public class GetAmountOfAllowedModemErrorResponse extends GetAmountOfAllowedModemBaseResponse{
    private final String errorMessage;

    public GetAmountOfAllowedModemErrorResponse(String errorMessage) {
        this.errorMessage = errorMessage;
        this.ok = false;
    }
}
