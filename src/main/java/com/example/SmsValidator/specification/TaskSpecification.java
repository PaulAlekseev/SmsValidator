package com.example.SmsValidator.specification;

import com.example.SmsValidator.entity.*;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class TaskSpecification {

    public static Specification<TaskEntity> hasId(Long id) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(TaskEntity_.ID), id));
    }

    public static Specification<TaskEntity> hasProviderSession_Id(Long modemProviderSessionId) {
        return ((root, query, criteriaBuilder) -> {
            Join<ModemProviderSessionEntity, TaskEntity> join = root.join(TaskEntity_.MODEM_PROVIDER_SESSION_ENTITY);
            return criteriaBuilder.equal(join.get(ModemProviderSessionEntity_.ID), modemProviderSessionId);
        });
    }

    public static Specification<TaskEntity> hasProviderSession_Busy(Boolean busy) {
        return ((root, query, criteriaBuilder) -> {
            Join<ModemProviderSessionEntity, TaskEntity> join = root.join(TaskEntity_.MODEM_PROVIDER_SESSION_ENTITY);
            return criteriaBuilder.equal(join.get(ModemProviderSessionEntity_.BUSY), busy);
        });
    }

    public static Specification<TaskEntity> hasProviderSession_Active(Boolean active) {
        return ((root, query, criteriaBuilder) -> {
            Join<ModemProviderSessionEntity, TaskEntity> join = root.join(TaskEntity_.MODEM_PROVIDER_SESSION_ENTITY);
            return criteriaBuilder.equal(join.get(ModemProviderSessionEntity_.ACTIVE), active);
        });
    }

    public static Specification<TaskEntity> hasUser_Email(String email) {
        return ((root, query, criteriaBuilder) -> {
            Join<User, TaskEntity> join = root.join(TaskEntity_.USER);
            return criteriaBuilder.equal(join.get(User_.EMAIL), email);
        });
    }

    public static Specification<TaskEntity> hasProviderSession_User_Email(String email) {
        return ((root, query, criteriaBuilder) -> {
            Join<ModemProviderSessionEntity, TaskEntity> join = root.join(TaskEntity_.MODEM_PROVIDER_SESSION_ENTITY);
            Join<User, ModemProviderSessionEntity> userJoin = root.join(ModemProviderSessionEntity_.USER);
            return criteriaBuilder.equal(userJoin.get(User_.EMAIL), email);
        });
    }

    public static Specification<TaskEntity> withModemEntity() {
        return ((root, query, criteriaBuilder) -> {
            root.fetch(TaskEntity_.MODEM_ENTITY);
            return null;
        });
    }

    public static Specification<TaskEntity> hasModemEntity_Id(Long modemId) {
        return ((root, query, criteriaBuilder) -> {
            Join<ModemEntity, TaskEntity> modemJoin = root.join(TaskEntity_.MODEM_ENTITY);
            return criteriaBuilder.equal(modemJoin.get(ModemEntity_.ID), modemId);
        });
    }

    public static Specification<TaskEntity> withServiceType() {
        return ((root, query, criteriaBuilder) -> {
            root.fetch(TaskEntity_.SERVICE_TYPE_ENTITY);
            return null;
        });
    }
}
