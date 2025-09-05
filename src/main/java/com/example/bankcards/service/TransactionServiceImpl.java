package com.example.bankcards.service;

import com.example.bankcards.dto.TransactionDTO;
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
    public Transaction makeTransaction(TransactionDTO transactionDTO) {
        logger.info("Запущен метод makeTransaction из UserService");
        BigDecimal amount = new BigDecimal(transactionDTO.getAmount());
        Transaction transaction = repositoty.save(
                Transaction.builder()
                        .fromCardId(transactionDTO.getFromCardId())
                        .toCardId(transactionDTO.getToCardId())
                        .amount(amount)
                        .initiatorId(userService.getCurrentUser().getId())
                        .transactionDate(Instant.now())
                        .description(transactionDTO.getDescription())
                        .status(Transaction.Status.INCOMPLETE)
                        .build()
        );
        if (cardService.executeTransaction(transactionDTO.getFromCardId(), transactionDTO.getToCardId(), amount)) {
            transaction.setStatus(Transaction.Status.DONE);
            repositoty.save(transaction);
        }
        return transaction;
    }
}