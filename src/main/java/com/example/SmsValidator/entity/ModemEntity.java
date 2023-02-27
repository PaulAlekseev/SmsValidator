package com.example.SmsValidator.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class ModemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String phoneNumber;
    private String IMSI;
    private String ICCID;
    private Boolean busy = false;
    private String services;
//    private Date reservedUntil = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());

    private Date reservedUntil = new Date();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reserved_by")
    private User reservedBy;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "modemEntity")
    private List<UsedServiceTypeEntity> usedServiceTypeEntityList;

    @OneToMany(cascade = CascadeType.DETACH, mappedBy = "modemEntity")
    private Set<TaskEntity> taskEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modem_provider_session")
    private ModemProviderSessionEntity modemProviderSessionEntity;

    public String addService(String service) {
        if (!services.contains(service) ) services += service;
        return services;
    }
}
