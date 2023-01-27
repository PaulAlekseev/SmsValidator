package com.example.SmsValidator.bean;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public abstract class RefreshTokenBaseResponse {
    protected boolean ok = true;
}
