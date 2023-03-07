package com.example.SmsValidator.controller;

import com.example.SmsValidator.bean.provider.ProviderModemDisconnectCriteriaRequest;
import com.example.SmsValidator.bean.provider.ProviderModemDisconnectRequest;
import com.example.SmsValidator.exception.customexceptions.provider.ProviderSessionBusyException;
import com.example.SmsValidator.exception.customexceptions.provider.ProviderSessionNotFoundException;
import com.example.SmsValidator.exception.customexceptions.socket.ModemProviderSessionDoesNotExistException;
import com.example.SmsValidator.service.ProviderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/provider/")
@RequiredArgsConstructor
public class ProviderController {

    private final ProviderService providerService;

    @PostMapping(path = "start")
    public ResponseEntity<?> activate()
            throws ProviderSessionNotFoundException, ProviderSessionBusyException, ModemProviderSessionDoesNotExistException {
        return ResponseEntity.ok(providerService.activate());
    }

    @PostMapping(path = "stop")
    public ResponseEntity<?> stop()
            throws ProviderSessionNotFoundException, ProviderSessionBusyException, ModemProviderSessionDoesNotExistException {
        return ResponseEntity.ok(providerService.stop());
    }

    @PostMapping(path = "saveModems")
    public ResponseEntity<?> saveModems()
            throws ProviderSessionNotFoundException, ProviderSessionBusyException, ModemProviderSessionDoesNotExistException {
        return ResponseEntity.ok(providerService.saveModems());
    }

    @GetMapping(path = "getModems")
    public ResponseEntity<?> getModems() {
        return ResponseEntity.ok(providerService.getProvidersWorkingModems());
    }

//    @PutMapping(path = "disconnectModemsCriteria")
//    public ResponseEntity<?> disconnectModems(@RequestBody ProviderModemDisconnectCriteriaRequest request)
//            throws ProviderSessionNotFoundException, ProviderSessionBusyException, ModemProviderSessionDoesNotExistException, IOException {
//        return ResponseEntity.ok(providerService.disconnectModemsByCriteria(request));
//    }

    @PutMapping(path = "disconnectModems")
    public ResponseEntity<?> disconnectModem(@RequestBody ProviderModemDisconnectRequest request)
            throws ProviderSessionNotFoundException, ProviderSessionBusyException, ModemProviderSessionDoesNotExistException, IOException {
        return ResponseEntity.ok(providerService.disconnectModems(request));
    }

    @GetMapping(path = "getByCriteria")
    public ResponseEntity<?> getModemsByCriteria(@RequestParam int revenue, @RequestParam String services)
            throws ProviderSessionNotFoundException, ProviderSessionBusyException {
        return ResponseEntity.ok(providerService.getModemsByCriteria(revenue, services));
    }

    @GetMapping(path = "getTasks")
    public ResponseEntity<?> getTasks(@RequestParam Long modemId) {
        return ResponseEntity.ok(providerService.getTasksFromModemId(modemId));
    }
}
