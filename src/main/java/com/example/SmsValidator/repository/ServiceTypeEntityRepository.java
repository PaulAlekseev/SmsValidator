package com.example.SmsValidator.repository;

import com.example.SmsValidator.entity.ServiceTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ServiceTypeEntityRepository extends CrudRepository<ServiceTypeEntity, Long> {
    ServiceTypeEntity findFirstById(Long id);
    List<ServiceTypeEntity> findByActiveTrue();
    List<ServiceTypeEntity> findByActive(Boolean active);
    ServiceTypeEntity findByTaskEntity_Id(Long id);

    Optional<ServiceTypeEntity> findById(Long id);
}