package com.example.SmsValidator.controller;

import com.example.SmsValidator.exception.CustomException;
import com.example.SmsValidator.model.Modem;
import com.example.SmsValidator.repository.ModemEntityRepository;
import com.example.SmsValidator.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.Date;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/v1/task/")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;
    private final ModemEntityRepository modemEntityRepository;


    @PostMapping("create")
    public ResponseEntity<?> createTask(
            Principal principal,
            @RequestParam Long serviceId) throws CustomException, IOException {
        return ResponseEntity.ok(taskService.createTask(serviceId, principal));
    }

    @GetMapping("get")
    public ResponseEntity<?> getTask(@RequestParam Long taskId) {
        return ResponseEntity.ok(taskService.getTask(taskId));
    }

    @PostMapping("createReserved")
    public ResponseEntity<?> createReservedTask(Principal principal,
                                                @RequestParam Long serviceId,
                                                @RequestParam Long modemId) {
        return ResponseEntity.ok(taskService.createReservedTask(serviceId, modemId, principal));
    }

//    @GetMapping(path = "test")
//    public ResponseEntity<?> test(Principal principal,
//                                                @RequestParam Long serviceId,
//                                  @RequestParam int amount) {
//        return ResponseEntity.ok(
//                modemEntityRepository.count(serviceId, new Date(), amount)
//                        .stream().map(Modem::toModel).collect(Collectors.toList())
//        );
//    }

//    @GetMapping("/getModems")
//    private ResponseEntity getModems(@RequestParam Long serviceId){
//        return ResponseEntity.ok(Modem.toModel(taskService.getAvailableModem(serviceId)));
//    }
}