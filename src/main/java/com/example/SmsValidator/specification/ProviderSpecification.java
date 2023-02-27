package com.example.SmsValidator.specification;

import com.example.SmsValidator.entity.ModemProviderSessionEntity;
import com.example.SmsValidator.entity.ModemProviderSessionEntity_;
import com.example.SmsValidator.entity.User;
import com.example.SmsValidator.entity.User_;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class ProviderSpecification {
    public static Specification<ModemProviderSessionEntity> hasUser_Username(String email) {
        return ((root, query, criteriaBuilder) -> {
            Join<ModemProviderSessionEntity, User> join = root.join(ModemProviderSessionEntity_.USER);
            return criteriaBuilder.equal(join.get(User_.EMAIL), email);
        });
    }

    public static Specification<ModemProviderSessionEntity> withModems(String email) {
        return ((root, query, criteriaBuilder) -> {
            root.fetch(ModemProviderSessionEntity_.MODEM_ENTITY_LIST);
            return null;
        });
    }

}

