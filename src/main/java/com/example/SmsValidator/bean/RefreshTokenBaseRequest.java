package com.example.SmsValidator.bean;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenBaseRequest {
    private String refreshToken;
}
