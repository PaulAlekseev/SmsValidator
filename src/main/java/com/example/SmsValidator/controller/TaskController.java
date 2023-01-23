package com.example.SmsValidator.controller;

import com.example.SmsValidator.entity.TaskEntity;
import com.example.SmsValidator.model.Modem;
import com.example.SmsValidator.repository.ServiceTypeEntityRepository;
import com.example.SmsValidator.service.TaskService;
import com.example.SmsValidator.socket.SocketConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/task")
public class TaskController {
    @Autowired
    private TaskService taskService;


    @GetMapping("/createTask")
    public ResponseEntity createTask(@RequestParam Long serviceId,
                                     @RequestParam Long timeSeconds) {
        return ResponseEntity.ok(taskService.createTask(serviceId, timeSeconds));
    }

    @GetMapping("/getTask")
    public ResponseEntity getTask(@RequestParam Long taskId) {
        return ResponseEntity.ok(taskService.getTask(taskId));
    }

    @GetMapping("/getModems")
    private ResponseEntity getModems(@RequestParam Long serviceId){
        return ResponseEntity.ok(Modem.toModel(taskService.getAvailableModem(serviceId)));
    }
}