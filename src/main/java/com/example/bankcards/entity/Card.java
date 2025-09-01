package com.example.bankcards.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "cards")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Setter
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

    @Setter
    @Column(name = "expiration_date")
    private Instant expirationDate;

    @Setter
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Setter
    @Column(name = "creditLimit")
    private BigDecimal creditLimit;

    @Setter
    @Column(name = "balance")
    private BigDecimal balance;

    public enum Status {
        ACTIVE,
        BLOCKED,
        EXPIRED
    }

}
