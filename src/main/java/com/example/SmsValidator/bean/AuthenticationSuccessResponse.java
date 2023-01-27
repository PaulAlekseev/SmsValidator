package com.example.SmsValidator.bean;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AuthenticationSuccessResponse extends AuthenticationBaseResponse {
    private String authToken;
    private String refreshToken;
}
