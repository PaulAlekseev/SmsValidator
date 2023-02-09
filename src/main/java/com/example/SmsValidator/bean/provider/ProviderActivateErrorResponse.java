package com.example.SmsValidator.bean.provider;

import lombok.Data;

@Data
public class ProviderActivateErrorResponse extends ProviderActivateBaseResponse{
    private final String errorMessage;
    public ProviderActivateErrorResponse(String errorMessage) {
        this.ok = false;
        this.errorMessage = errorMessage;
    }
}
