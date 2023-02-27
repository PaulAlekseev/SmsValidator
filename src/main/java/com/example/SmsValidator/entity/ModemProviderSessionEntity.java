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
public class ModemProviderSessionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private Boolean busy;
    private String socketId;
    private Boolean active;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(cascade = CascadeType.DETACH, mappedBy = "modemProviderSessionEntity", fetch = FetchType.LAZY)
    private List<TaskEntity> taskEntityList;

    @OneToMany(cascade = CascadeType.DETACH, mappedBy = "modemProviderSessionEntity", fetch = FetchType.LAZY)
    private List<ModemEntity> modemEntityList;
}
