package com.example.SmsValidator.controller;

import com.example.SmsValidator.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping(path = "/api/v1/user/")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("getBalance")
    public ResponseEntity<?> getBalance(Principal principal) {
        return ResponseEntity.ok(userService.getBalance(principal.getName()));
    }

    @GetMapping("getTaskHistory")
    public ResponseEntity<?> getTaskHistory(Principal principal) {
        return ResponseEntity.ok(userService.getTaskHistory(principal));
    }

    @GetMapping("getActiveTasks")
    public ResponseEntity<?> getActiveTasks(Principal principal) {
        return ResponseEntity.ok(userService.getActiveTaskHistory(principal));
    }
}
