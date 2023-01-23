package com.example.SmsValidator.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Entity
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

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "modemEntity")
    private List<UsedServiceTypeEntity> usedServiceTypeEntityList;

    @OneToMany(cascade = CascadeType.DETACH, mappedBy = "modemEntity")
    private List<TaskEntity> taskEntity;

    @ManyToOne
    @JoinColumn(name = "modem_provider_session")
    private ModemProviderSessionEntity modemProviderSessionEntity;

    public ModemEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getIMSI() {
        return IMSI;
    }

    public void setIMSI(String IMSI) {
        this.IMSI = IMSI;
    }

    public List<UsedServiceTypeEntity> getUsedServiceTypeList() {
        return usedServiceTypeEntityList;
    }

    public void setUsedServiceTypeList(List<UsedServiceTypeEntity> usedServiceTypeEntityList) {
        this.usedServiceTypeEntityList = usedServiceTypeEntityList;
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

    public ModemProviderSessionEntity getModemProviderSessionEntity() {
        return modemProviderSessionEntity;
    }

    public String getICCID() {
        return ICCID;
    }

    public void setICCID(String ICCID) {
        this.ICCID = ICCID;
    }

    public Boolean getBusy() {
        return busy;
    }

    public void setBusy(Boolean busy) {
        this.busy = busy;
    }

    public void setModemProviderSessionEntity(ModemProviderSessionEntity modemProviderSessionEntity) {
        this.modemProviderSessionEntity = modemProviderSessionEntity;
    }

    public Date getReservedUntil() {
        return reservedUntil;
    }

    public void setReservedUntil(Date reservedUntil) {
        this.reservedUntil = reservedUntil;
    }
}
