package com.example.bankcards.entity;

import jakarta.persistence.*;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "cards")
@RequiredArgsConstructor
public class Card {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "number_encrypted")
    private String numberEncrypted;

    @Column(name = "number_masked")
    private String numberMasked;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User owner;

    @Column(name = "entry_date")
    private Instant entryDate;

    @Column(name = "expiration_date")
    private Instant expirationDate;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private CardStatus status;

    @Column(name = "balance")
    private BigDecimal balance;
}
