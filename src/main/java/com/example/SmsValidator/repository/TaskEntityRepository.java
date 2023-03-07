package com.example.SmsValidator.repository;

import com.example.SmsValidator.entity.ModemEntity;
import com.example.SmsValidator.entity.ModemProviderSessionEntity;
import com.example.SmsValidator.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface TaskEntityRepository extends CrudRepository<TaskEntity, Long>, JpaSpecificationExecutor<TaskEntity> {
    @Transactional
    @Modifying
    @Query("update TaskEntity t set t.success = ?1 where t.id = ?2")
    int updateSuccessById(boolean success, Long id);

    TaskEntity findFirstById(Long id);

    boolean existsByIdAndReservedTrue(Long id);

    List<TaskEntity> findByUser_EmailAndReadyTrueOrderByIdDesc(String email);

    List<TaskEntity> findByUser_EmailAndReadyTrueAndDoneFalseOrderByIdDesc(String email);

    TaskEntity findByUser_IdAndDoneFalseAndReadyTrue(Long id);

    List<TaskEntity> findByUser_EmailOrderByIdDesc(String email);

    TaskEntity findByIdAndModemProviderSessionEntity(Long id, ModemProviderSessionEntity modemProviderSessionEntity);

    @Transactional
    @Modifying
    @Query("update TaskEntity t set t.done = ?1 where t.id = ?2 and t.modemProviderSessionEntity = ?3")
    int updateDoneByIdAndModemProviderSessionEntity(boolean done, Long id, ModemProviderSessionEntity modemProviderSessionEntity);

    TaskEntity findByIdAndModemProviderSessionEntity_Id(Long id, Long id1);

    @Query("""
            select (count(t) > 0) from TaskEntity t
            where t.id = ?1 and t.modemEntity.IMSI = ?2 and t.modemEntity.ICCID = ?3""")
    boolean existsByIdAndModemEntity_IMSIAndModemEntity_ICCID(Long id, String IMSI, String ICCID);

    @Transactional
    @Modifying
    @Query("update TaskEntity t set t.modemEntity = ?1, t.modemProviderSessionEntity = ?2 where t.id = ?3")
    int updateModemEntityAndModemProviderSessionEntityById(ModemEntity modemEntity, ModemProviderSessionEntity modemProviderSessionEntity, Long id);

    @Transactional
    @Modifying
    @Query("update TaskEntity t set t.ready = ?1 where t.id = ?2")
    int updateReadyById(boolean ready, Long id);

    @Query("select (count(t) > 0) from TaskEntity t where t.id = ?1 and t.modemEntity.phoneNumber = ?2")
    boolean existsByIdAndModemEntity_PhoneNumber(Long id, String phoneNumber);

    @Transactional
    @Modifying
    @Query("update TaskEntity t set t.modemEntity = ?1 where t.id = ?2")
    int updateModemEntityById(ModemEntity modemEntity, Long id);

    @Override
    Optional<TaskEntity> findById(Long aLong);
}