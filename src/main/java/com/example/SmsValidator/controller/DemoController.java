package com.example.SmsValidator.controller;

import com.example.SmsValidator.service.ModemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
public class DemoController {

    private final ModemService modemService;

    @GetMapping(path = "demo")
    public ResponseEntity<String> sayHello(Principal principal) {
        String name = principal.getName();
        return ResponseEntity.ok(String.format("Hello, %s", name));
    }


}
