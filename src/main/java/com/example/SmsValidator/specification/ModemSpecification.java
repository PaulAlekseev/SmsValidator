package com.example.SmsValidator.specification;

import com.example.SmsValidator.entity.*;
import com.example.SmsValidator.specification.extra.Order;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class ModemSpecification {
    public static Specification<ModemEntity> hasImsi(String imsi) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(ModemEntity_.IMSI), imsi);
    }

    public static Specification<ModemEntity> hasIccid(String iccid) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(ModemEntity_.ICCID), iccid);
    }

    public static Specification<ModemEntity> hasBusy(Boolean busy) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(ModemEntity_.BUSY), busy));
    }

    public static Specification<ModemEntity> hasId(Long id) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(ModemEntity_.ID), id);
    }

    public static Specification<ModemEntity> hasModemProviderSessionEntity_Busy(Boolean busy) {
        return ((root, query, criteriaBuilder) -> {
            Join<ModemProviderSessionEntity, ModemEntity> join = root
                    .join(ModemEntity_.MODEM_PROVIDER_SESSION_ENTITY);
            return criteriaBuilder.equal(join.get(ModemProviderSessionEntity_.BUSY), busy);
        });
    }

    public static Specification<ModemEntity> hasModemProviderSessionEntity_Active(Boolean active) {
        return ((root, query, criteriaBuilder) -> {
            Join<ModemProviderSessionEntity, ModemEntity> join = root
                    .join(ModemEntity_.MODEM_PROVIDER_SESSION_ENTITY);
            return criteriaBuilder.equal(join.get(ModemProviderSessionEntity_.ACTIVE), active);
        });
    }

    public static Specification<ModemEntity> withTasks() {
        return ((root, query, criteriaBuilder) -> {
            root.fetch(ModemEntity_.TASK_ENTITY, JoinType.INNER);
            return null;
        });
    }

    public static Specification<ModemEntity> orderBy(Order order, String by) {
        return ((root, query, criteriaBuilder) -> {
            switch (order) {
                case ASC -> query.orderBy(criteriaBuilder.asc(root.get(by)));
                case DESC -> query.orderBy(criteriaBuilder.desc(root.get(by)));
            }
            return null;
        });
    }

    public static Specification<ModemEntity> withTimesUsedLessThanOrEqual(int timesUsed) {
        return ((root, query, criteriaBuilder) -> {
            Join<TaskEntity, ModemEntity> join = root
                    .join(ModemEntity_.TASK_ENTITY);
            Predicate taskIsSuccess = criteriaBuilder.isTrue(join.get(TaskEntity_.SUCCESS));
            Predicate finalPredicate = criteriaBuilder.
                    lessThanOrEqualTo(criteriaBuilder.sum(criteriaBuilder.<Integer>selectCase().when(taskIsSuccess, 1).otherwise(0)), timesUsed);

            query.groupBy(root.get("id")).having(finalPredicate);
            return null;
        });
    }

    public static Specification<ModemEntity> withTimesUsedGreaterThanOrEqual(int timesUsed) {
        return ((root, query, criteriaBuilder) -> {
            Join<TaskEntity, ModemEntity> join = root
                    .join(ModemEntity_.MODEM_PROVIDER_SESSION_ENTITY);
            Predicate taskIsSuccess = criteriaBuilder.isTrue(join.get(TaskEntity_.SUCCESS));
            Predicate finalPredicate = criteriaBuilder.
                    greaterThanOrEqualTo(criteriaBuilder.sum(criteriaBuilder.<Integer>selectCase().when(taskIsSuccess, 1).otherwise(0)), timesUsed);

            query.groupBy(root.get("id")).having(finalPredicate);
            return null;
        });
    }

    public static Specification<ModemEntity> hasModemProviderSessionId(Long id) {
        return ((root, query, criteriaBuilder) -> {
            Join<ModemProviderSessionEntity, ModemEntity> join = root
                    .join(ModemEntity_.MODEM_PROVIDER_SESSION_ENTITY);
            return criteriaBuilder.equal(join.get(ModemProviderSessionEntity_.ID), id);
        });
    }

    public static Specification<ModemEntity> hasModemProviderSession_User_Email(String email) {
        return ((root, query, criteriaBuilder) -> {
            Join<ModemProviderSessionEntity, ModemEntity> join = root
                    .join(ModemEntity_.MODEM_PROVIDER_SESSION_ENTITY);
            Join<User, ModemProviderSessionEntity> userJoin = join
                    .join(ModemProviderSessionEntity_.USER);
            return criteriaBuilder.equal(userJoin.get(User_.EMAIL), email);
        });
    }

    public static Specification<ModemEntity> hasRevenueMoreThan(int amount) {
        return ((root, query, criteriaBuilder) -> {
            Join<TaskEntity, ModemEntity> join = root.join(ModemEntity_.TASK_ENTITY);
            Predicate taskIsSuccess = criteriaBuilder.isTrue(join.get(TaskEntity_.SUCCESS));
            Predicate finalPredicate = criteriaBuilder.
                    lessThanOrEqualTo(criteriaBuilder.sum(criteriaBuilder.<Integer>selectCase().when(taskIsSuccess, join.get(TaskEntity_.COST)).otherwise(0)), amount);
            query.groupBy(root.get("id")).having(finalPredicate);
            return null;
        });
    }
}
