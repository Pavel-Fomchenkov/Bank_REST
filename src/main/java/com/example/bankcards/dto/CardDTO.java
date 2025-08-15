package com.example.bankcards.dto;

import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.entity.User;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
public class CardDTO {
    private Long id;

    private String description;

    private String numberMasked;

    private UserDTO owner;

    @Column(name = "entry_date")
    private Instant entryDate;

    @Column(name = "expiration_date")
    private Instant expirationDate;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private CardStatus status;

    @Column(name = "creditLimit")
    private BigDecimal creditLimit;

    @Column(name = "balance")
    private BigDecimal balance;


}
