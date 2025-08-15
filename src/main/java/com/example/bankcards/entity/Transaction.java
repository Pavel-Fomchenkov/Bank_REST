package com.example.bankcards.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "transactions")
@RequiredArgsConstructor
@Getter
@Setter
public class Transaction {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "from_card_id", nullable = false)
    private Long fromCardId;

    @Column(name = "to_card_id", nullable = false)
    private Long toCardId;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "initiator_id", nullable = false)
    private Long initiatorId;

    @Column(name = "transaction_date", nullable = false)
    private Instant transactionDate;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionStatus status = TransactionStatus.INCOMPLETE;
}
