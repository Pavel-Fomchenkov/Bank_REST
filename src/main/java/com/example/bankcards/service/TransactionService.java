package com.example.bankcards.service;

import com.example.bankcards.dto.TransactionCreateDTO;
import com.example.bankcards.entity.Transaction;

public interface TransactionService {
    Transaction makeTransaction(TransactionCreateDTO transactionCreateDTO);
}
