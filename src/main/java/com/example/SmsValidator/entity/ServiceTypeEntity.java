package com.example.SmsValidator.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class ServiceTypeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;
    private Boolean active;

    private String messageRegex;
    private String senderRegex;
    private int allowedAmount;
    private int daysBetween;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "serviceType")
    private List<UsedServiceTypeEntity> usedServiceTypeEntityList;

    @OneToMany(cascade = CascadeType.DETACH, mappedBy = "serviceTypeEntity")
    private List<TaskEntity> taskEntity;

    public ServiceTypeEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public List<UsedServiceTypeEntity> getUsedServiceTypeEntityList() {
        return usedServiceTypeEntityList;
    }

    public void setUsedServiceTypeEntityList(List<UsedServiceTypeEntity> usedServiceTypeEntityList) {
        this.usedServiceTypeEntityList = usedServiceTypeEntityList;
    }

    public List<TaskEntity> getTaskEntity() {
        return taskEntity;
    }

    public void setTaskEntity(List<TaskEntity> taskEntity) {
        this.taskEntity = taskEntity;
    }

    public String getMessageRegex() {
        return messageRegex;
    }

    public void setMessageRegex(String messageRegex) {
        this.messageRegex = messageRegex;
    }

    public String getSenderRegex() {
        return senderRegex;
    }

    public void setSenderRegex(String senderRegex) {
        this.senderRegex = senderRegex;
    }

    public int getAllowedAmount() {
        return allowedAmount;
    }

    public void setAllowedAmount(int allowedAmount) {
        this.allowedAmount = allowedAmount;
    }

    public int getDaysBetween() {
        return daysBetween;
    }

    public void setDaysBetween(int daysBetween) {
        this.daysBetween = daysBetween;
    }
}
