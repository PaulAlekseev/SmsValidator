package com.example.SmsValidator.bean.refreshtoken;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenBaseRequest {
    private String refreshToken;
}
