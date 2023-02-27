package com.example.SmsValidator.socket.container;

import com.example.SmsValidator.entity.ModemEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class DisconnectModemsInContainer {
    private List<ModemEntity> modems;
}
