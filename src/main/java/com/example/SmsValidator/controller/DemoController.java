package com.example.SmsValidator.controller;

import com.example.SmsValidator.service.ModemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/")
public class DemoController {

    @Autowired
    private ModemService modemService;

    @GetMapping(path = "demo")
    public ResponseEntity<String> sayHello(Principal principal) {
        String name = principal.getName();
        return ResponseEntity.ok(String.format("Hello, %s", name));
    }


}
