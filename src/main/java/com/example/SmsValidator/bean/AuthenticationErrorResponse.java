package com.example.SmsValidator.bean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthenticationErrorResponse extends AuthenticationBaseResponse {

    private String errorMessage;

    public AuthenticationErrorResponse(String errorMessage) {
        this.ok = false;
        this.errorMessage = errorMessage;
    }
}
