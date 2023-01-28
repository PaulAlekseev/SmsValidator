package com.example.SmsValidator.bean.authentication;

import com.example.SmsValidator.bean.BaseResponse;
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
