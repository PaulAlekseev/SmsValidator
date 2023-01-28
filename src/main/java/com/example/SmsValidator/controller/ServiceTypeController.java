package com.example.SmsValidator.controller;

import com.example.SmsValidator.service.ServiceTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/service")
@RequiredArgsConstructor
public class ServiceTypeController {

    private ServiceTypeService serviceTypeService;

    @GetMapping(path = "getAllowed")
    public ResponseEntity<?> getAllowedServices() {
        return ResponseEntity.ok(serviceTypeService.getActiveServices());
    }

    @GetMapping(path = "getAllowedModems")
    public ResponseEntity<?> getAllowedModems(@RequestAttribute Long serviceId) {
        return ResponseEntity.ok(serviceTypeService.getAllowedModems(serviceId));
    }
}
