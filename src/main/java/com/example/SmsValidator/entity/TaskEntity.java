package com.example.SmsValidator.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class TaskEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String serviceName;
    private int cost;
    private boolean ready;
    private boolean done;
    private Long timeSeconds;
    private boolean reserved;
    private boolean success;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id")
    private ServiceTypeEntity serviceTypeEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modem_id")
    private ModemEntity modemEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "used_service_id")
    private UsedServiceTypeEntity usedServiceTypeEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modem_provider_session_id")
    private ModemProviderSessionEntity modemProviderSessionEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "taskEntity")
    private List<MessageEntity> messages;
}
