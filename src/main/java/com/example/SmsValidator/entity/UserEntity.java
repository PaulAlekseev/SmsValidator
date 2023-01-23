package com.example.SmsValidator.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToMany(cascade = CascadeType.DETACH, mappedBy = "userEntity")
    private List<TaskEntity> taskEntityList;
}
