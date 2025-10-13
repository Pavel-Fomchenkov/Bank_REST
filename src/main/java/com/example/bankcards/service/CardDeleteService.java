package com.example.bankcards.service;

/**
 * Separate service needed to avoid circular reference dependency between Card, User and Transaction services.
 */
public interface CardDeleteService {
    boolean canBeDeleted(Long cardId);
}
