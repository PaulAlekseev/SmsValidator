package com.example.SmsValidator.specification;

import com.example.SmsValidator.entity.*;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public class ModemSpecification {
    public static Specification<ModemEntity> hasImsi(String imsi) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get(ModemEntity_.IMSI), imsi);
        };
    }

    public static Specification<ModemEntity> hasIccid(String iccid) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get(ModemEntity_.ICCID), iccid);
        };
    }

    public static Specification<ModemEntity> hasId(Long id) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get(ModemEntity_.ID), id);
        };
    }

    public static Specification<ModemEntity> withTasks() {
        return ((root, query, criteriaBuilder) -> {
            root.fetch("taskEntity", JoinType.LEFT);
            return null;
        });
    }

    public static Specification<ModemEntity> withUsedServiceType() {
        return ((root, query, criteriaBuilder) -> {
            root.fetch(ModemEntity_.USED_SERVICE_TYPE_ENTITY_LIST, JoinType.LEFT);
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
}
