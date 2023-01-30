package com.example.SmsValidator.repository;

import java.util.Optional;

import com.example.SmsValidator.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {
    User findFirstByTelegramId(Long telegramId);
    User findFirstByEmail(String email);
  boolean existsByEmail(String email);
  boolean existsByUsername(String username);

  Optional<User> findByEmail(String email);

}
