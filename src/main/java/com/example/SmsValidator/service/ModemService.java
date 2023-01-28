package com.example.SmsValidator.service;

import com.example.SmsValidator.entity.ModemEntity;
import com.example.SmsValidator.repository.ModemEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class ModemService {

    private ModemEntityRepository modemEntityRepository;

    public int reserveModem(ModemEntity modemEntity, Principal principal) {
        return modemEntityRepository.update
    }

}
