package com.example.SmsValidator.controller;

import com.example.SmsValidator.bean.servicetype.CreateNewServiceRequest;
import com.example.SmsValidator.service.ModemService;
import com.example.SmsValidator.service.ServiceTypeService;
import com.example.SmsValidator.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/admin/")
public class AdminController {

    private final TaskService taskService;
    private final ModemService modemService;
    private final ServiceTypeService serviceTypeService;

    @PostMapping(path = "createService")
    public ResponseEntity<?> createService(@RequestBody CreateNewServiceRequest serviceRequest) {
        return ResponseEntity.ok(serviceTypeService.createNewService(serviceRequest));
    }

    @PostMapping(path = "create")
    public ResponseEntity<?> createTask(@RequestParam Long telegramId,
                                        @RequestParam Long serviceId) {
        return ResponseEntity.ok(taskService.createTelegramTask(serviceId, telegramId));
    }

    @PostMapping(path = "createReserved")
    public ResponseEntity<?> createReservedTask(@RequestParam Long telegramId,
                                                @RequestParam Long modemId,
                                                @RequestParam Long serviceId) {
        return ResponseEntity.ok(taskService.createReservedTelegramTask(serviceId, modemId, telegramId));
    }

    @GetMapping(path = "getTask")
    public ResponseEntity<?> getTask(@RequestParam Long taskId) {
        return ResponseEntity.ok(taskService.getTask(taskId));
    }

    @PostMapping(path = "reserveModem")
    public ResponseEntity<?> reserveModem(@RequestParam Long telegramId,
                                          @RequestParam int daysToReserve) {
        return ResponseEntity.ok(modemService.reserveModem(telegramId, daysToReserve));
    }

    @GetMapping(path = "getReservedModem")
    public ResponseEntity<?> getReservedModems(@RequestParam Long telegramId) {
        return ResponseEntity.ok(modemService.getReservedModems(telegramId));
    }
}
