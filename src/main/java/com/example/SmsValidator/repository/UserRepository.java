package com.example.SmsValidator.repository;

import java.util.Optional;

import com.example.SmsValidator.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
  boolean existsByEmail(String email);
  boolean existsByUsername(String username);

  Optional<User> findByEmail(String email);

}
