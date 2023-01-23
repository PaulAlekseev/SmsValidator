package com.example.SmsValidator.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class ModemProviderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String accessKey;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "modemProvider")
    private List<ModemProviderSessionEntity> modemProviderSessionEntityList;

    public ModemProviderEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public List<ModemProviderSessionEntity> getModemProviderSessionEntityList() {
        return modemProviderSessionEntityList;
    }

    public void setModemProviderSessionEntityList(List<ModemProviderSessionEntity> modemProviderSessionEntityList) {
        this.modemProviderSessionEntityList = modemProviderSessionEntityList;
    }
}
