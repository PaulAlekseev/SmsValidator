package com.example.SmsValidator.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/demo-controller")
public class DemoController {

    @GetMapping
    public ResponseEntity<String> sayHello(Principal principal) {
        String name = principal.getName();
        return ResponseEntity.ok(String.format("Hello, %s", name));
    }

}
