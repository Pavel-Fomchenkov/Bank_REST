package com.example.bankcards.dto;

import com.example.bankcards.entity.Card;
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

    private Instant entryDate;

    private Instant expirationDate;

    @Enumerated(EnumType.STRING)
    private Card.Status status;

    private String creditLimit;

    private String balance;


}
