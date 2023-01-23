package com.example.SmsValidator.service;

import com.example.SmsValidator.entity.ModemEntity;
import com.example.SmsValidator.entity.ModemProviderSessionEntity;
import com.example.SmsValidator.repository.ModemEntityRepository;
import com.example.SmsValidator.repository.ModemProviderSessionEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ModemService {

    @Autowired
    private ModemProviderSessionEntityRepository modemProviderSessionEntityRepository;

    @Autowired
    private ModemEntityRepository modemEntityRepository;

}
