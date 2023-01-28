package com.example.SmsValidator.service;

import com.example.SmsValidator.entity.ModemEntity;
import com.example.SmsValidator.entity.User;
import com.example.SmsValidator.repository.ModemEntityRepository;
import com.example.SmsValidator.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private UserRepository userRepository;
    private ModemEntityRepository modemEntityRepository;

    public int reserveModem() {
        return modemEntityRepository
    }

}
