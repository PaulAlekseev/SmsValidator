package com.example.SmsValidator.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
public class UsedServiceTypeEntity {

    private int timesUsed = 1;
    private Date lastTimeUsed = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "service_id")
    @NotNull
    private ServiceTypeEntity serviceType;

    @ManyToOne
    @JoinColumn(name = "modem_id")
    @NotNull
    private ModemEntity modemEntity;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "usedServiceTypeEntity")
    private List<TaskEntity> taskEntity;
}
