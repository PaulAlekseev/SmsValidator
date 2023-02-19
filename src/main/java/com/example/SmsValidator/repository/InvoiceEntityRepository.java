package com.example.SmsValidator.repository;

import com.example.SmsValidator.entity.InvoiceEntity;
import com.example.SmsValidator.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface InvoiceEntityRepository extends JpaRepository<InvoiceEntity, Long> {
    @Transactional
    @Modifying
    @Query("""
            update InvoiceEntity i set i.validated = ?1
            where i.validated = false and i.id = ?2 and i.amount = ?3 and i.user = ?4""")
    int updateValidatedByValidatedFalseAndIdAndAmountAndUser(Boolean validated, Long id, int amount, User user);
    @Transactional
    @Modifying
    @Query("update InvoiceEntity i set i.validated = ?1 where i.validated = false and i.id = ?2 and i.user = ?3")
    int updateValidatedByValidatedFalseAndIdAndUser(Boolean validated, Long id, User user);
    @Transactional
    @Modifying
    @Query("update InvoiceEntity i set i.validated = ?1 where i.id = ?2 and i.user = ?3")
    int updateValidatedByIdAndUser(Boolean validated, Long id, User user);
}