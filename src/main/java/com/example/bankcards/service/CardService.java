package com.example.bankcards.service;

import com.example.bankcards.dto.CardCreateDTO;
import com.example.bankcards.entity.Card;

import java.util.List;

public interface CardService {

    Card create(long ownerId, CardCreateDTO cardCreateDTO);

    boolean blockCard(long cardId);

    boolean activateCard(long cardId);

    void deleteCard(long cardId);

    List<Card> getAll(String username);

    List<Card> getByStatus(String username);

    List<Card> getByUsername(String username);

    List<Card> getByUsernamePart(String usernamePart);

    Card getById(Long id);

    int expireCards();
}
//        • Администратор:
//    • Видит все карты
//        • Пользователь:
//    • Просматривает свои карты (поиск + пагинация)
//    • Запрашивает блокировку карты
//    • Делает переводы между своими картами
//    • Смотрит баланс