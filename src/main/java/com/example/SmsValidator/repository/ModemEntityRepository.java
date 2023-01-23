package com.example.SmsValidator.repository;

import com.example.SmsValidator.entity.ModemEntity;
import com.example.SmsValidator.entity.ModemProviderSessionEntity;
import com.example.SmsValidator.entity.ServiceTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

public interface ModemEntityRepository extends JpaRepository<ModemEntity, Long> {
    ModemEntity findFirstByBusyFalseAndModemProviderSessionEntity_BusyFalseAndModemProviderSessionEntity_ActiveTrueAndUsedServiceTypeEntityListEmptyOrderByIdAsc();
    boolean existsByUsedServiceTypeEntityList_IdAndUsedServiceTypeEntityList_TimesUsedLessThan(Long id, int timesUsed);
    @Query("""
            select m from ModemEntity m left join m.usedServiceTypeEntityList usedServiceTypeEntityList
            where m.busy = false and m.modemProviderSessionEntity.busy = false and m.modemProviderSessionEntity.active = true and usedServiceTypeEntityList.serviceType.id is null or usedServiceTypeEntityList.serviceType.id = ?1 and usedServiceTypeEntityList.timesUsed < ?2""")
    List<ModemEntity> findByBusyFalseAndModemProviderSessionEntity_BusyFalseAndModemProviderSessionEntity_ActiveTrueAndUsedServiceTypeEntityList_ServiceType_IdNullOrUsedServiceTypeEntityList_ServiceType_IdAndUsedServiceTypeEntityList_TimesUsedLessThan(Long id, int timesUsed);
    List<ModemEntity> findByBusyFalseAndModemProviderSessionEntity_ActiveTrueAndModemProviderSessionEntity_BusyFalseAndUsedServiceTypeEntityList_ServiceType_NameAndUsedServiceTypeEntityList_TimesUsedLessThanOrderByUsedServiceTypeEntityList_TimesUsedDesc(String name, int timesUsed);
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
    @Transactional
    @Modifying
    @Query("update ModemEntity m set m.modemProviderSessionEntity = ?1 where m.phoneNumber = ?2")
    int updateModemProviderSessionEntityByPhoneNumber(ModemProviderSessionEntity modemProviderSessionEntity, String phoneNumber);
    @Transactional
    @Modifying
    @Query("""
            update ModemEntity m set m.modemProviderSessionEntity = ?1, m.busy = ?2
            where m.phoneNumber = ?3 and m.modemProviderSessionEntity is null""")
    int updateModemProviderSessionEntityAndBusyByPhoneNumberAndModemProviderSessionEntityNull(ModemProviderSessionEntity modemProviderSessionEntity, Boolean busy, String phoneNumber);
    ModemEntity findByIMSIAndICCID(String IMSI, String ICCID);
    @Transactional
    @Modifying
    @Query("""
            update ModemEntity m set m.modemProviderSessionEntity = ?1
            where m.phoneNumber = ?2 and m.modemProviderSessionEntity is null""")
    int updateModemProviderSessionEntityByPhoneNumberAndModemProviderSessionEntityNull(ModemProviderSessionEntity modemProviderSessionEntity, String phoneNumber);
    ModemEntity findByIMSI(String IMSI);
    ModemEntity findByTaskEntity_Id(Long id);
    @Transactional
    @Modifying
    @Query("""
            update ModemEntity m set m.modemProviderSessionEntity = ?1
            where m.modemProviderSessionEntity = ?2 and m.IMSI = ?3""")
    int updateModemProviderSessionEntityByModemProviderSessionEntityAndIMSI(ModemProviderSessionEntity modemProviderSessionEntity, ModemProviderSessionEntity modemProviderSessionEntity1, String IMSI);
    @Transactional
    @Modifying
    @Query("""
            update ModemEntity m set m.modemProviderSessionEntity = ?1
            where m.modemProviderSessionEntity = ?2 and m.phoneNumber = ?3""")
    int updateModemProviderSessionEntityByModemProviderSessionEntityAndPhoneNumber(ModemProviderSessionEntity modemProviderSessionEntity, ModemProviderSessionEntity modemProviderSessionEntity1, String phoneNumber);
    List<ModemEntity> findByIMSIIn(Collection<String> IMSIS);
    List<ModemEntity> findByBusyFalseAndModemProviderSessionEntity_ActiveTrueAndModemProviderSessionEntity_BusyFalseAndUsedServiceTypeEntityList_ServiceType_NameNot(String name);
    List<ModemEntity> findByBusyFalseAndModemProviderSessionEntity_ActiveTrueAndModemProviderSessionEntity_BusyFalseAndUsedServiceTypeEntityListEmpty();
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