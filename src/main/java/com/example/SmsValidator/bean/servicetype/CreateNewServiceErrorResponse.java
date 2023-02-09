package com.example.SmsValidator.bean.servicetype;

import lombok.Data;

@Data
public class CreateNewServiceErrorResponse extends CreateNewServiceBaseResponse {

    private String errorMessage;

    public CreateNewServiceErrorResponse(String errorMessage) {
        this.ok = false;
        this.errorMessage = errorMessage;
    }
}
