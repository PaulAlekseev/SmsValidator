package com.example.SmsValidator.repository;

import com.example.SmsValidator.entity.UsedServiceTypeEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.Date;

public interface UsedServiceTypeEntityRepository extends CrudRepository<UsedServiceTypeEntity, Long> {
    UsedServiceTypeEntity findFirstByServiceType_IdAndTimesUsedLessThanAndLastTimeUsedLessThanEqualAndModemEntity_BusyFalseOrderByTimesUsedDesc(Long id, int timesUsed, Date lastTimeUsed);
    UsedServiceTypeEntity findByTaskEntity_ModemEntity_Id(Long id);
    UsedServiceTypeEntity findByTaskEntity_Id(Long id);
    @Query("""
            select (count(u) > 0) from UsedServiceTypeEntity u inner join u.taskEntity taskEntity
            where taskEntity.id = ?1 and taskEntity.modemProviderSessionEntity.id = ?2""")
    boolean existsByTaskEntity_IdAndTaskEntity_ModemProviderSessionEntity_Id(Long id, Long id1);
    UsedServiceTypeEntity findFirstByServiceType_IdAndTimesUsedLessThanAndLastTimeUsedLessThanEqualOrderByTimesUsedDesc(Long id, int timesUsed, Date lastTimeUsed);
}