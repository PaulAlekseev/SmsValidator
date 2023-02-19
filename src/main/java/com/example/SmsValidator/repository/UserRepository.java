package com.example.SmsValidator.repository;

import com.example.SmsValidator.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findFirstByTelegramId(Long telegramId);

    @EntityGraph(attributePaths = {"invoices"})
    User findFirstByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    Optional<User> findByEmail(String email);

    @Transactional
    @Modifying
    @Query("UPDATE User SET balance = balance + :amount WHERE id = :id")
    int topUpBalance(long id, int amount);

    @Override
    Optional<User> findById(Integer integer);
}
