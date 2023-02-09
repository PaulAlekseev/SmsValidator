package com.example.SmsValidator.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class ModemProviderSessionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private Boolean busy;
    private String socketId;
    private Boolean active;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(cascade = CascadeType.DETACH, mappedBy = "modemProviderSessionEntity")
    private List<TaskEntity> taskEntityList;

    @OneToMany(cascade = CascadeType.DETACH, mappedBy = "modemProviderSessionEntity")
    private List<ModemEntity> modemEntityList;
}
