package com.example.bankcards.service;

import com.example.bankcards.dto.CardCreateDTO;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface CardService {

    Card create(long ownerId, CardCreateDTO cardCreateDTO);

    Card createServiceCard(String description);

    /**
     * Удаляет карту с {@link Card} id=cardId если по карте не было операций {@link Transaction}
     *
     * @return результат удаления
     */
    boolean deleteCard(long cardId);

    Page<Card> getAll(Pageable pageable);

    Page<Card> getByStatus(Card.Status status, Pageable pageable);

    Page<Card> getByUsername(String username, Pageable pageable);

    List<Card> getByUsernamePart(String usernamePart);

    Card getById(Long id);

    /**
     * Устанавливает статус {@link Card.Status#EXPIRED EXPIRED} для всех карт у которых прошла {@link Card expirationDate}
     *
     * @return количество скорректированных карт
     */
    int expireCards();

    boolean changeStatus(long cardId, Card.Status status);

    Card prolongCard(Long id, int days);

    boolean executeTransaction(Long fromCardId, Long toCardId, BigDecimal amount);
}
//        • Администратор:
//    • Видит все карты
//        • Пользователь:
//    • Просматривает свои карты (поиск + пагинация)
//    • Запрашивает блокировку карты
//    • Делает переводы между своими картами
//    • Смотрит баланс