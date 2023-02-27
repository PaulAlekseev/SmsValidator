package com.example.SmsValidator.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ModemDto {
    private Long id;
    private String phoneNumber;
    private String IMSI;
    private boolean busy;
    private Date reservedUntil;
    private ModemProviderSessionDto modemProviderSessionEntity;
}
