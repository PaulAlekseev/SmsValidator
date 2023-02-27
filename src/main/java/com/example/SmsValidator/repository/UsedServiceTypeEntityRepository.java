package com.example.SmsValidator.repository;

import com.example.SmsValidator.entity.ModemEntity;
import com.example.SmsValidator.entity.ServiceTypeEntity;
import com.example.SmsValidator.entity.TaskEntity;
import com.example.SmsValidator.entity.UsedServiceTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface UsedServiceTypeEntityRepository extends CrudRepository<UsedServiceTypeEntity, Long>, JpaSpecificationExecutor<UsedServiceTypeEntity> {
    @Transactional
    @Modifying
    @Query("""
            update UsedServiceTypeEntity u set u.taskEntity = ?1, u.timesUsed = ?2, u.lastTimeUsed = ?3
            where u.modemEntity = ?4 and u.serviceType = ?5""")
    int updateTaskEntityAndTimesUsedAndLastTimeUsedByModemEntityAndServiceType(TaskEntity taskEntity, int timesUsed, Date lastTimeUsed, ModemEntity modemEntity, ServiceTypeEntity serviceType);
    @Transactional
    @Modifying
    @Query("update UsedServiceTypeEntity u set u.taskEntity = ?1 where u.modemEntity = ?2 and u.serviceType = ?3")
    int updateTaskEntityByModemEntityAndServiceType(TaskEntity taskEntity, ModemEntity modemEntity, ServiceTypeEntity serviceType);
    @Transactional
    @Modifying
    @Query("update UsedServiceTypeEntity u set u.timesUsed = ?1, u.lastTimeUsed = ?2 where u.taskEntity = ?3")
    int updateTimesUsedAndLastTimeUsedByTaskEntity(int timesUsed, Date lastTimeUsed, TaskEntity taskEntity);
    UsedServiceTypeEntity findFirstByServiceType_IdAndTimesUsedLessThanAndLastTimeUsedLessThanEqualAndModemEntity_ReservedUntilLessThanAndModemEntity_BusyFalseAndModemEntity_ModemProviderSessionEntity_ActiveTrueAndModemEntity_ModemProviderSessionEntity_BusyFalseOrderByIdDescTimesUsedDesc(Long id, int timesUsed, Date lastTimeUsed, Date reservedUntil);
    UsedServiceTypeEntity findFirstByModemEntity_IdAndServiceType_Id(Long id, Long id1);
    UsedServiceTypeEntity findFirstByTaskEntity_Id(Long id);
    long countByModemEntity_BusyFalseAndTimesUsedLessThanAndLastTimeUsedLessThanEqual(@Nullable int timesUsed, @Nullable Date lastTimeUsed);
    UsedServiceTypeEntity findFirstByServiceType_IdAndTimesUsedLessThanAndLastTimeUsedLessThanEqualAndModemEntity_ReservedUntilLessThanAndModemEntity_BusyFalseOrderByTimesUsedDesc(Long id, int timesUsed, Date lastTimeUsed, Date reservedUntil);
    List<UsedServiceTypeEntity> findByServiceType_IdAndTimesUsedLessThanAndLastTimeUsedLessThanEqualAndModemEntity_ReservedUntilGreaterThanAndModemEntity_BusyFalseOrderByTimesUsedDesc(Long id, int timesUsed, Date lastTimeUsed, Date reservedUntil);
    UsedServiceTypeEntity findFirstByServiceType_IdAndTimesUsedLessThanAndLastTimeUsedLessThanEqualAndModemEntity_BusyFalseAndModemEntity_ReservedUntilLessThanEqualOrderByTimesUsedDesc(Long id, int timesUsed, Date lastTimeUsed, Date reservedUntil);
    UsedServiceTypeEntity findFirstByServiceType_IdAndTimesUsedLessThanEqualAndModemEntity_BusyFalseAndModemEntity_ReservedUntilLessThanEqualOrderByTimesUsedDesc(Long id, int timesUsed, Date reservedUntil);
    UsedServiceTypeEntity findFirstByServiceType_IdAndTimesUsedLessThanAndLastTimeUsedLessThanEqualAndModemEntity_BusyFalseOrderByTimesUsedDesc(Long id, int timesUsed, Date lastTimeUsed);
    UsedServiceTypeEntity findByTaskEntity_ModemEntity_Id(Long id);
    UsedServiceTypeEntity findByTaskEntity_Id(Long id);
    @Query("""
            select (count(u) > 0) from UsedServiceTypeEntity u inner join u.taskEntity taskEntity
            where taskEntity.id = ?1 and taskEntity.modemProviderSessionEntity.id = ?2""")
    boolean existsByTaskEntity_IdAndTaskEntity_ModemProviderSessionEntity_Id(Long id, Long id1);
    UsedServiceTypeEntity findFirstByServiceType_IdAndTimesUsedLessThanAndLastTimeUsedLessThanEqualOrderByTimesUsedDesc(Long id, int timesUsed, Date lastTimeUsed);

    List<UsedServiceTypeEntity> findByServiceType_IdAndTimesUsedLessThanAndLastTimeUsedLessThanEqualAndModemEntity_ReservedUntilLessThanAndModemEntity_BusyFalseAndModemEntity_ModemProviderSessionEntity_ActiveTrueAndModemEntity_ModemProviderSessionEntity_BusyFalseOrderByIdDescTimesUsedDesc(Long id, int allowedAmount, Date newDate, Date from);
}