package com.example.SmsValidator.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Entity
public class ModemProviderSessionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private Boolean busy;
    private String socketId;
    private Boolean active;

    @ManyToOne
    @JoinColumn(name = "modem_provider")
    private ModemProviderEntity modemProvider;

    @OneToMany(cascade = CascadeType.DETACH, mappedBy = "modemProviderSessionEntity")
    private List<TaskEntity> taskEntityList;

    @OneToMany(cascade = CascadeType.DETACH, mappedBy = "modemProviderSessionEntity")
    private List<ModemEntity> modemEntityList;

    public ModemProviderSessionEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getBusy() {
        return busy;
    }

    public void setBusy(Boolean busy) {
        this.busy = busy;
    }

    public String getSocketId() {
        return socketId;
    }

    public void setSocketId(String socketId) {
        this.socketId = socketId;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public ModemProviderEntity getModemProvider() {
        return modemProvider;
    }

    public void setModemProvider(ModemProviderEntity modemProvider) {
        this.modemProvider = modemProvider;
    }

    public List<TaskEntity> getTaskEntityList() {
        return taskEntityList;
    }

    public void setTaskEntityList(List<TaskEntity> taskEntityList) {
        this.taskEntityList = taskEntityList;
    }

    public List<ModemEntity> getModemEntityList() {
        return modemEntityList;
    }

    public void setModemEntityList(List<ModemEntity> modemEntityList) {
        this.modemEntityList = modemEntityList;
    }
}
