package com.example.bankcards.dto;

import com.example.bankcards.entity.Transaction;
import lombok.Data;

import java.time.Instant;

@Data
public class TransactionDTO {
    private Long id;
    private Long fromCardId;
    private Long toCardId;
    private String amount;
    private Long initiatorId;
    private Instant transactionDate;
    private String description;
    private Transaction.Status status;
}
