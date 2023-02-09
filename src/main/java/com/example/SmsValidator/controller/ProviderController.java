package com.example.SmsValidator.controller;

import com.example.SmsValidator.service.ProviderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/provider/")
@RequiredArgsConstructor
public class ProviderController {

    private final ProviderService providerService;

    @PostMapping(path = "start")
    public ResponseEntity<?> activate() {
        return ResponseEntity.ok(providerService.activate());
    }

    @PostMapping(path = "stop")
    public ResponseEntity<?> stop() {
        return ResponseEntity.ok(providerService.stop());
    }

    @PostMapping(path = "saveModems")
    public ResponseEntity<?> saveModems() {
        return ResponseEntity.ok(providerService.saveModems());
    }
}
