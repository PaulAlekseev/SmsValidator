package com.example.SmsValidator.service;

import com.example.SmsValidator.bean.user.UserInfoResponse;
import com.example.SmsValidator.bean.user.UserTaskHistoryResponse;
import com.example.SmsValidator.entity.User;
import com.example.SmsValidator.model.Task;
import com.example.SmsValidator.repository.TaskEntityRepository;
import com.example.SmsValidator.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final TaskEntityRepository taskEntityRepository;

    public UserInfoResponse getUserInfo(String username) {
        return new UserInfoResponse(
                username,
                userRepository.findFirstByEmail(username).getBalance()
        );
    }

    public UserTaskHistoryResponse getTaskHistory(String username) {
        List<Task> tasks = taskEntityRepository.
                findByUser_EmailAndReadyTrueOrderByIdDesc(username)
                .stream()
                .map(Task::toModel)
                .toList();
        return new UserTaskHistoryResponse(tasks);
    }

    public UserTaskHistoryResponse getActiveTaskHistory(String username) {
        List<Task> tasks = taskEntityRepository.
                findByUser_EmailAndReadyTrueAndDoneFalseOrderByIdDesc(username)
                .stream()
                .map(Task::toModel)
                .toList();
        return new UserTaskHistoryResponse(tasks);
    }

    public User loadByUsername(String username) throws UsernameNotFoundException{
        return userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
