package com.example.SmsValidator.repository;

import com.example.SmsValidator.entity.ModemEntity;
import com.example.SmsValidator.entity.ModemProviderSessionEntity;
import jdk.jfr.Name;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ModemEntityRepository extends CrudRepository<ModemEntity, Long>, JpaSpecificationExecutor<ModemEntity> {
    @Transactional
    @Modifying
    @Query("""
            update ModemEntity m set m.modemProviderSessionEntity = ?1
            where m.modemProviderSessionEntity = ?2 and m.IMSI in ?3""")
    int updateModemProviderSessionEntityByModemProviderSessionEntityAndIMSIIn(ModemProviderSessionEntity modemProviderSessionEntity, ModemProviderSessionEntity modemProviderSessionEntity1, Collection<String> IMSIS);
    @Transactional
    @Modifying
    @Query("""
            update ModemEntity m set m.modemProviderSessionEntity = ?1
            where m.IMSI = ?2 and m.modemProviderSessionEntity = ?3""")
    int updateModemProviderSessionEntityByIMSIAndModemProviderSessionEntity(ModemProviderSessionEntity modemProviderSessionEntity, String IMSI, ModemProviderSessionEntity modemProviderSessionEntity1);
    @Transactional
    @Modifying
    @Query("""
            update ModemEntity m set m.modemProviderSessionEntity = ?1
            where m.id in ?2 and m.modemProviderSessionEntity = ?3""")
    int updateModemProviderSessionEntityByIdInAndModemProviderSessionEntity(ModemProviderSessionEntity modemProviderSessionEntity, Collection<Long> ids, ModemProviderSessionEntity modemProviderSessionEntity1);

    @Transactional
    @Modifying
    @Query("""
            update ModemEntity m set m.modemProviderSessionEntity = ?1
            where m.modemProviderSessionEntity = ?2 and m.busy = false""")
    int updateModemProviderSessionEntityByModemProviderSessionEntityAndBusyFalse(ModemProviderSessionEntity modemProviderSessionEntity, ModemProviderSessionEntity modemProviderSessionEntity1);

    @Transactional
    @Modifying
    @Query("update ModemEntity m set m.modemProviderSessionEntity = ?1 where m.id = ?2")
    int updateModemProviderSessionEntityById(ModemProviderSessionEntity modemProviderSessionEntity, Long id);

    ModemEntity findByTaskEntity_IdAndModemProviderSessionEntity_BusyTrue(Long id);

    ModemEntity findFirstByBusyFalseAndModemProviderSessionEntity_BusyFalseAndModemProviderSessionEntity_ActiveTrueAndUsedServiceTypeEntityList_TimesUsedOrderByIdDesc(int timesUsed);

    long countByUsedServiceTypeEntityList_TimesUsedLessThanOrUsedServiceTypeEntityListEmpty(int timesUsed);

    @Query("""
            select m from ModemEntity m inner join m.usedServiceTypeEntityList usedServiceTypeEntityList
            where usedServiceTypeEntityList.serviceType.id = ?1 and usedServiceTypeEntityList.timesUsed < ?2""")
    List<ModemEntity> findByUsedServiceTypeEntityList_ServiceType_IdAndUsedServiceTypeEntityList_TimesUsedLessThan(Long id, int timesUsed);

    List<ModemEntity> findByTaskEntity_IdAndTaskEntity_TimeSecondsLessThan(Long id, Long timeSeconds);

    @Query("""
            select m from ModemEntity m left join m.usedServiceTypeEntityList usedServiceTypeEntityList
            where m.busy = false and m.usedServiceTypeEntityList is empty or usedServiceTypeEntityList.serviceType.id <> ?1""")
    List<ModemEntity> findByBusyFalseAndUsedServiceTypeEntityListEmptyOrUsedServiceTypeEntityList_ServiceType_IdNot(Long id);

//    @Query("""
//            select m from ModemEntity m inner join m.usedServiceTypeEntityList usedServiceTypeEntityList
//            where (m.busy = false
//            and m.modemProviderSessionEntity.busy = false
//            and m.modemProviderSessionEntity.active = true
//            and m.reservedUntil < :date
//            and usedServiceTypeEntityList is empty)
//            or
//            (m.busy = false
//            and m.modemProviderSessionEntity.busy = false
//            and m.modemProviderSessionEntity.active = true
//            and m.reservedUntil < :date
//            and m.usedServiceTypeEntityList.serviceType.id <> :serviceId)
//            order by m.id DESC""")
//    List<ModemEntity> findThing(@Param("serviceId") Long serviceId, @Param("date") Date reservedUntil, @Param("times") int amount);

    ModemEntity findFirstByBusyFalseAndModemProviderSessionEntity_BusyFalseAndModemProviderSessionEntity_ActiveTrueAndUsedServiceTypeEntityList_ServiceType_IdNotAndReservedUntilLessThanOrderByIdDesc(Long id, Date reservedUntil);

    ModemEntity findFirstByBusyFalseAndReservedUntilLessThanAndModemProviderSessionEntity_ActiveTrueAndModemProviderSessionEntity_BusyFalseAndUsedServiceTypeEntityListEmptyOrderByIdDesc(Date reservedUntil);

    @Query("""
            select m from ModemEntity m
            where m.reservedBy.id = ?1 and m.reservedUntil >= ?2
            order by m.reservedUntil DESC""")
    List<ModemEntity> findByReservedBy_IdAndReservedUntilGreaterThanEqualOrderByReservedUntilDesc(Long id, Date reservedUntil);

    @Transactional
    @Modifying
    @Query("update ModemEntity m set m.busy = ?1 where m.id = ?2")
    int updateBusyById(Boolean busy, Long id);

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

    ModemEntity findFirstById(Long modemId);

    @Override
    Optional<ModemEntity> findById(Long aLong);

}