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
public class ModemProviderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String accessKey;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "modemProvider")
    private List<ModemProviderSessionEntity> modemProviderSessionEntityList;

}
