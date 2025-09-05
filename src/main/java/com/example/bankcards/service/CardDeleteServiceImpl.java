package com.example.bankcards.service;

import com.example.bankcards.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CardDeleteServiceImpl implements CardDeleteService {
    private final TransactionRepository repository;

    @Override
    public boolean canBeDeleted(Long cardId) {
        return !repository.existsByFromCardIdOrToCardId(cardId, cardId);
    }
}
