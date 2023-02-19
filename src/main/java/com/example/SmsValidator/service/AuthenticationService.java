package com.example.SmsValidator.service;

import com.example.SmsValidator.auth.JwtTokenProvider;
import com.example.SmsValidator.bean.authentication.*;
import com.example.SmsValidator.bean.refreshtoken.RefreshTokenBaseRequest;
import com.example.SmsValidator.bean.refreshtoken.RefreshTokenBaseResponse;
import com.example.SmsValidator.bean.refreshtoken.RefreshTokenFailResponse;
import com.example.SmsValidator.bean.refreshtoken.RefreshTokenSuccessResponse;
import com.example.SmsValidator.entity.Role;
import com.example.SmsValidator.entity.User;
import com.example.SmsValidator.exception.CustomException;
import com.example.SmsValidator.exception.customexceptions.user.UserNotFoundException;
import com.example.SmsValidator.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    public AuthenticationBaseResponse register(RegisterRequest request) {
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        if (checkIfUserExists(request.getUsername(), request.getEmail())) {
            AuthenticationErrorResponse response = new AuthenticationErrorResponse("User with this username already exists");

            return response;
        }
        repository.save(user);
        String jwtToken = tokenProvider.createAuthToken(user.getUsername(), user.getRole().name());
        String jwtRefreshToken = tokenProvider.createRefreshToken(user.getUsername(), user.getRole().name());
        AuthenticationSuccessResponse response = new AuthenticationSuccessResponse();
        response.setAuthToken(jwtToken);
        response.setRefreshToken(jwtRefreshToken);
        return response;
    }

    public AuthenticationBaseResponse authenticate(AuthenticationRequest request) throws Exception {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
            User user = repository
                    .findByEmail(request.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            String jwtToken = tokenProvider.createAuthToken(user.getUsername(), user.getRole().name());
            String jwtRefreshToken = tokenProvider.createRefreshToken(user.getUsername(), user.getRole().name());
            AuthenticationSuccessResponse response = new AuthenticationSuccessResponse();
            response.setRefreshToken(jwtRefreshToken);
            response.setAuthToken(jwtToken);
            return response;
    }

    public RefreshTokenBaseResponse refreshToken(RefreshTokenBaseRequest request) {
        try {
            String refreshToken = request.getRefreshToken();
            String username = tokenProvider.getUserName(refreshToken);
            User user = repository.findByEmail(username).get();
            String newAuthToken = tokenProvider.createAuthToken(username, user.getRole().name());
            return new RefreshTokenSuccessResponse(newAuthToken);
        } catch (Exception e) {
            return new RefreshTokenFailResponse(e.getLocalizedMessage());
        }
    }

    private boolean checkIfUserExists(String username, String email) {
        return repository.existsByEmail(email) || repository.existsByUsername(username);
    }
}
