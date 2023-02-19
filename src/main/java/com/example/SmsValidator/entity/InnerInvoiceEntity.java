package com.example.SmsValidator.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@Setter
@Getter
public class InnerInvoiceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    private int amount;
    private Boolean validated;
    private Date created;

    @OneToOne(mappedBy = "innerInvoice")
    private TaskEntity tasks;
}
