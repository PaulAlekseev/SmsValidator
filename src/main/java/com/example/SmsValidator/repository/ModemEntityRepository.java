package com.example.SmsValidator.repository;

import com.example.SmsValidator.entity.MessageEntity;
import com.example.SmsValidator.entity.ModemEntity;
import com.example.SmsValidator.entity.ModemProviderSessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Date;
import java.util.List;

public interface ModemEntityRepository extends CrudRepository<ModemEntity, Long> {
    ModemEntity findFirstByBusyFalseAndReservedUntilLessThanAndModemProviderSessionEntity_ActiveTrueAndModemProviderSessionEntity_BusyFalseAndUsedServiceTypeEntityListEmptyOrderByIdDesc(Date reservedUntil);
    ModemEntity findByBusyFalseAndReservedUntilLessThanAndModemProviderSessionEntity_ActiveTrueAndUsedServiceTypeEntityListEmpty(Date reservedUntil);
    ModemEntity findFirstById(Long id);
    ModemEntity findFirstByBusyFalseAndTaskEntity_ModemProviderSessionEntity_BusyFalseAndModemProviderSessionEntity_ActiveTrueAndUsedServiceTypeEntityListEmptyAndReservedUntilLessThanOrderByIdDesc(Date reservedUntil);
    List<ModemEntity> findByReservedBy_IdAndReservedUntilGreaterThanEqualOrderByReservedUntilDesc(Long id, Date reservedUntil);
    ModemEntity findFirstByReservedUntilLessThanEqualAndUsedServiceTypeEntityListEmptyOrderByIdDesc(Date reservedUntil);
    List<ModemEntity> findByReservedBy_IdAndReservedUntilLessThanOrderByReservedUntilDesc(Long id, Date reservedUntil);
    @Transactional
    @Modifying
    @Query("update ModemEntity m set m.busy = ?1 where m.id = ?2")
    int updateBusyById(Boolean busy, Long id);
    ModemEntity findByTaskEntity_Messages(MessageEntity messages);
    ModemEntity findFirstByBusyFalseAndModemProviderSessionEntity_BusyFalseAndModemProviderSessionEntity_ActiveTrueAndUsedServiceTypeEntityListEmptyOrderByIdAsc();

    @Transactional
    @Modifying
    @Query("""
            update ModemEntity m set m.modemProviderSessionEntity = ?1
            where m.modemProviderSessionEntity = ?2 and m.busy = true""")
    int updateModemProviderSessionEntityByModemProviderSessionEntityAndBusyTrue(ModemProviderSessionEntity modemProviderSessionEntity, ModemProviderSessionEntity modemProviderSessionEntity1);

    @Transactional
    @Modifying
    @Query("update ModemEntity m set m.modemProviderSessionEntity = ?1, m.busy = ?2 where m.phoneNumber = ?3")
    int updateModemProviderSessionEntityAndBusyByPhoneNumber(ModemProviderSessionEntity modemProviderSessionEntity, Boolean busy, String phoneNumber);

    ModemEntity findByIMSIAndICCID(String IMSI, String ICCID);

    ModemEntity findByIMSI(String IMSI);

    ModemEntity findByTaskEntity_Id(Long id);

    @Transactional
    @Modifying
    @Query("""
            update ModemEntity m set m.modemProviderSessionEntity = ?1
            where m.modemProviderSessionEntity = ?2 and m.IMSI = ?3""")
    int updateModemProviderSessionEntityByModemProviderSessionEntityAndIMSI(ModemProviderSessionEntity modemProviderSessionEntity, ModemProviderSessionEntity modemProviderSessionEntity1, String IMSI);

    List<ModemEntity> findByIMSIIn(Collection<String> IMSIS);

    @Transactional
    @Modifying
    @Query("update ModemEntity m set m.busy = ?1 where m.phoneNumber in ?2")
    int updateBusyByPhoneNumberIn(Boolean busy, Collection<String> phoneNumbers);

    List<ModemEntity> findByModemProviderSessionEntityNullAndPhoneNumberIn(Collection<String> phoneNumbers);

    @Transactional
    @Modifying
    @Query("update ModemEntity m set m.modemProviderSessionEntity = ?1 where m.modemProviderSessionEntity = ?2")
    int updateModemProviderSessionEntityByModemProviderSessionEntity(ModemProviderSessionEntity modemProviderSessionEntity, ModemProviderSessionEntity modemProviderSessionEntity1);

    @Transactional
    @Modifying
    @Query("update ModemEntity m set m.modemProviderSessionEntity = ?1 where m.modemProviderSessionEntity is not null")
    int updateModemProviderSessionEntityByModemProviderSessionEntityNotNull(ModemProviderSessionEntity modemProviderSessionEntity);

    @Transactional
    @Modifying
    @Query("update ModemEntity m set m.busy = ?1 where m.phoneNumber = ?2 and m.modemProviderSessionEntity = ?3")
    int updateBusyByPhoneNumberAndModemProviderSessionEntity(Boolean busy, String phoneNumber, ModemProviderSessionEntity modemProviderSessionEntity);

    @Transactional
    @Modifying
    @Query("""
            update ModemEntity m set m.modemProviderSessionEntity = ?1
            where m.phoneNumber in ?2 and m.modemProviderSessionEntity is null""")
    int updateModemProviderSessionEntityByPhoneNumberInAndModemProviderSessionEntityNull(ModemProviderSessionEntity modemProviderSessionEntity, Collection<String> phoneNumbers);

    List<ModemEntity> findByPhoneNumberIn(Collection<String> phoneNumbers);
}