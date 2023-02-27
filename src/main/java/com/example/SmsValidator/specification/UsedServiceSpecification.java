package com.example.SmsValidator.specification;

import com.example.SmsValidator.entity.*;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

public class UsedServiceSpecification {
    public static Specification<UsedServiceTypeEntity> hasServiceType_Id(Long serviceId) {
        return ((root, query, criteriaBuilder) -> {
            Join<UsedServiceTypeEntity, ServiceTypeEntity> join = root.join(UsedServiceTypeEntity_.SERVICE_TYPE);
            return criteriaBuilder.equal(join.get(ServiceTypeEntity_.ID), serviceId);
        });
    }

    public static Specification<UsedServiceTypeEntity> hasTimesUsedLessThenAllowed() {
        return ((root, query, criteriaBuilder) -> {
            Join<UsedServiceTypeEntity, ServiceTypeEntity> join = root.join(UsedServiceTypeEntity_.SERVICE_TYPE);
            return criteriaBuilder.lessThan(root.get(UsedServiceTypeEntity_.TIMES_USED), join.get(ServiceTypeEntity_.ALLOWED_AMOUNT));
        });
    }

}
