package com.example.SmsValidator.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class ModemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String phoneNumber;
    private String IMSI;
    private String ICCID;
    private Boolean busy = false;
    private Date reservedUntil = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());

    @ManyToOne
    @JoinColumn(name = "reserved_by")
    private User reservedBy;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "modemEntity")
    private List<UsedServiceTypeEntity> usedServiceTypeEntityList;

    @OneToMany(cascade = CascadeType.DETACH, mappedBy = "modemEntity")
    private List<TaskEntity> taskEntity;

    @ManyToOne
    @JoinColumn(name = "modem_provider_session")
    private ModemProviderSessionEntity modemProviderSessionEntity;
}
