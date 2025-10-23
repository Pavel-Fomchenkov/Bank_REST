package com.example.bankcards.service;

import com.example.bankcards.dto.TransactionCreateDTO;
import com.example.bankcards.entity.Transaction;
import com.example.bankcards.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final CardService cardService;
    private final UserService userService;
    private final TransactionRepository repositoty;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Transaction makeTransaction(TransactionCreateDTO transactionCreateDTO) {
        logger.info("Запущен метод makeTransaction из UserService");
        BigDecimal amount = transactionCreateDTO.getAmount();
        Transaction transaction = repositoty.save(
                Transaction.builder()
                        .fromCardId(transactionCreateDTO.getFromCardId())
                        .toCardId(transactionCreateDTO.getToCardId())
                        .amount(amount)
                        .initiatorId(userService.getCurrentUser().getId())
                        .transactionDate(Instant.now())
                        .description(transactionCreateDTO.getDescription())
                        .status(Transaction.Status.INCOMPLETE)
                        .build()
        );
        if (cardService.executeTransaction(transactionCreateDTO.getFromCardId(), transactionCreateDTO.getToCardId(), amount)) {
            transaction.setStatus(Transaction.Status.DONE);
            repositoty.save(transaction);
        }
        return transaction;
    }
}