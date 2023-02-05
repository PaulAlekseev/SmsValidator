package com.example.SmsValidator.controller;

import com.example.SmsValidator.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @GetMapping("getInfo")
    public ResponseEntity<?> getBalance() {
        return ResponseEntity.ok(userService.getUserInfo(SecurityContextHolder.getContext().getAuthentication().getName()));
    }

    @GetMapping("getTaskHistory")
    public ResponseEntity<?> getTaskHistory() {
        return ResponseEntity.ok(userService.getTaskHistory(SecurityContextHolder.getContext().getAuthentication().getName()));
    }

    @GetMapping("getActiveTasks")
    public ResponseEntity<?> getActiveTasks() {
        return ResponseEntity.ok(userService.getActiveTaskHistory(SecurityContextHolder.getContext().getAuthentication().getName()));
    }
}
