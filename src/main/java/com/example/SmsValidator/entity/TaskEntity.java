package com.example.SmsValidator.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class TaskEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private boolean ready;
    private boolean done;
    private Long timeSeconds;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private ServiceTypeEntity serviceTypeEntity;

    @ManyToOne
    @JoinColumn(name = "modem_id")
    private ModemEntity modemEntity;

    @ManyToOne
    @JoinColumn(name = "used_service_id")
    private UsedServiceTypeEntity usedServiceTypeEntity;

    @ManyToOne
    @JoinColumn(name = "modem_provider_session_id")
    private ModemProviderSessionEntity modemProviderSessionEntity;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "taskEntity")
    private List<MessageEntity> messages;

    public TaskEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public Long getTimeSeconds() {
        return timeSeconds;
    }

    public void setTimeSeconds(Long timeSeconds) {
        this.timeSeconds = timeSeconds;
    }

    public ServiceTypeEntity getServiceTypeEntity() {
        return serviceTypeEntity;
    }

    public void setServiceTypeEntity(ServiceTypeEntity serviceTypeEntity) {
        this.serviceTypeEntity = serviceTypeEntity;
    }

    public ModemEntity getModemEntity() {
        return modemEntity;
    }

    public void setModemEntity(ModemEntity modemEntity) {
        this.modemEntity = modemEntity;
    }

    public UsedServiceTypeEntity getUsedServiceTypeEntity() {
        return usedServiceTypeEntity;
    }

    public void setUsedServiceTypeEntity(UsedServiceTypeEntity usedServiceTypeEntity) {
        this.usedServiceTypeEntity = usedServiceTypeEntity;
    }

    public ModemProviderSessionEntity getModemProviderSessionEntity() {
        return modemProviderSessionEntity;
    }

    public void setModemProviderSessionEntity(ModemProviderSessionEntity modemProviderSessionEntity) {
        this.modemProviderSessionEntity = modemProviderSessionEntity;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public List<MessageEntity> getMessages() {
        return messages;
    }

    public void setMessages(List<MessageEntity> messages) {
        this.messages = messages;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
