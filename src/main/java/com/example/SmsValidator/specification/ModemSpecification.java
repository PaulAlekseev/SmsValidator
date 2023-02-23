package com.example.SmsValidator.specification;

import com.example.SmsValidator.entity.ModemEntity;
import org.springframework.data.jpa.domain.Specification;

public class ModemSpecification {
    public static Specification<ModemEntity> hasImsi(String imsi) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get(ModemEntity_.))
        }
    }
}
