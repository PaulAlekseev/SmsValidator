package com.example.SmsValidator.bean.provider;

import com.example.SmsValidator.entity.ModemEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProviderModemDisconnectRequest {
    private List<ModemEntity> modem;
}
