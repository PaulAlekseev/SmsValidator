package com.example.SmsValidator.controller;

import com.example.SmsValidator.bean.reservemodem.ReserveModemRequest;
import com.example.SmsValidator.service.ModemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/modem/")
@RequiredArgsConstructor
public class ModemController {

    private final ModemService modemService;

    @PostMapping(path = "reserve")
    public ResponseEntity<?> reserveModem(Principal principal, @RequestBody ReserveModemRequest request) {
        return ResponseEntity.ok(modemService.reserveModem(principal, request.getReserveFor()));
    }

    @GetMapping(path = "getReserved")
    public ResponseEntity<?> getReservedModems(Principal principal) {
        return ResponseEntity.ok(modemService.getReservedModems(principal));
    }

}
