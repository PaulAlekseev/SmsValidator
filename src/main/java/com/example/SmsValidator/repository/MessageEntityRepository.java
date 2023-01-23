package com.example.SmsValidator.repository;

import com.example.SmsValidator.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface MessageEntityRepository extends CrudRepository<MessageEntity, Long> {
}