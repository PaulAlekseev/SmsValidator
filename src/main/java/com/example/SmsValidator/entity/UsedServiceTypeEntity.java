package com.example.SmsValidator.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Entity
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

    public UsedServiceTypeEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ServiceTypeEntity getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceTypeEntity serviceType) {
        this.serviceType = serviceType;
    }

    public ModemEntity getModemEntity() {
        return modemEntity;
    }

    public void setModemEntity(ModemEntity modemEntity) {
        this.modemEntity = modemEntity;
    }

    public List<TaskEntity> getTaskEntity() {
        return taskEntity;
    }

    public void setTaskEntity(List<TaskEntity> taskEntity) {
        this.taskEntity = taskEntity;
    }

    public int getTimesUsed() {
        return timesUsed;
    }

    public void setTimesUsed(int timesUsed) {
        this.timesUsed = timesUsed;
    }

    public Date getLastTimeUsed() {
        return lastTimeUsed;
    }

    public void setLastTimeUsed(Date lastTimeUsed) {
        this.lastTimeUsed = lastTimeUsed;
    }
}
