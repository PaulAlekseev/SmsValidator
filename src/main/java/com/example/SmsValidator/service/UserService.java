package com.example.SmsValidator.service;

import com.example.SmsValidator.bean.user.UserTaskHistoryResponse;
import com.example.SmsValidator.entity.TaskEntity;
import com.example.SmsValidator.model.Task;
import com.example.SmsValidator.repository.TaskEntityRepository;
import com.example.SmsValidator.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final TaskEntityRepository taskEntityRepository;

    public double getBalance(String email) {
        return userRepository.findFirstByEmail(email).getBalance();
    }

    public UserTaskHistoryResponse getTaskHistory(Principal principal) {
        List<Task> tasks = taskEntityRepository.
                findByUser_EmailAndReadyTrueOrderByIdDesc(principal.getName())
                .stream()
                .map(Task::toModel)
                .toList();
        return new UserTaskHistoryResponse(tasks);
    }

    public UserTaskHistoryResponse getActiveTaskHistory(Principal principal) {
        List<Task> tasks = taskEntityRepository.
                findByUser_EmailAndReadyTrueAndDoneFalseOrderByIdDesc(principal.getName())
                .stream()
                .map(Task::toModel)
                .toList();
        return new UserTaskHistoryResponse(tasks);
    }
}
