package com.example.bankcards.service;

import com.example.bankcards.dto.TransactionDTO;
import com.example.bankcards.entity.Transaction;

public interface TransactionService {
    Transaction makeTransaction(TransactionDTO transactionDTO);
}
