package com.example.bankcards.service;

import com.example.bankcards.dto.CardCreateDTO;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.repository.CardRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    private final CardRepository repository;
    private final UserService userService;
    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Override
    public Card create(long ownerId, CardCreateDTO cardCreateDTO) {
        logger.info("Запущен метод create из CardService");

        return repository.save(Card.builder()
                .description(cardCreateDTO.getDescription())
                .numberEncrypted(encryptNumber(cardCreateDTO.getCardNumber()))
                .numberMasked(maskNumber(cardCreateDTO.getCardNumber()))
                .owner(userService.getById(ownerId))
                .entryDate(Instant.now())
                .expirationDate(calculateExpiration())
                .status(CardStatus.ACTIVE)
                .creditLimit(cardCreateDTO.getCreditLimit())
                .balance(BigDecimal.ZERO)
                .build());
    }

    // TODO нужно учитывать, что карта может быть EXPIRED и не проводить операции,
    //  а также нужно сделать метод продления карты

    @Override
    @Transactional
    public boolean blockCard(long cardId) {
        return repository.changeStatus(cardId, CardStatus.BLOCKED) > 0;
    }

    @Override
    @Transactional
    public boolean activateCard(long cardId) {
        return repository.changeStatus(cardId, CardStatus.ACTIVE) > 0;
    }

    @Override
    public void deleteCard(long cardId) {

    }

    @Override
    public List<Card> getAll(String username) {
        return List.of();
    }

    @Override
    public List<Card> getByStatus(String username) {
        return List.of();
    }

    @Override
    public List<Card> getByUsername(String username) {
        return List.of();
    }

    @Override
    public List<Card> getByUsernamePart(String usernamePart) {
        return List.of();
    }
// TODO сделать корректную обработку EntityNotFoundException
    @Override
    public Card getById(Long id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Карта id " + id + " отсутствует в базе данных."));
    }

    @Override
    public int expireCards() {
        return 0;
    }

    private static Instant calculateExpiration() {
        Instant now = Instant.now();
        LocalDateTime ldtNow = LocalDateTime.ofInstant(now, ZoneOffset.UTC);
        LocalDateTime futureLDT = ldtNow.plusYears(3);
        return futureLDT.toInstant(ZoneOffset.UTC);
    }

    private static String encryptNumber(String number) {
        if (!validateNumber(number)) {
            throw new IllegalArgumentException("Неправильный номер карты или неподходящий формат номера.");
        }
        return new BCryptPasswordEncoder().encode(number);
    }

    private static boolean validateNumber(String number) {
        String noSpacesNumber = number.replaceAll("\s+", "");
        return noSpacesNumber.length() == 16 && noSpacesNumber.matches("^\\d+$");
    }

    private static String maskNumber(String number) {
        String noSpacesNumber = number.replaceAll("\s+", "");
        return noSpacesNumber.substring(noSpacesNumber.length() - 4);
    }
}


// TODO тут должны быть методы:
//  - создание карты с нулевым балансом
//  - получение информации о карте
//  - изменение реквизитов карты (наверное, разрешить менять только описание и дату истечения срока карты)
//  - блокировки карты
//  - разблокировки карты
//  - удаления карты (только при отсутствии денежных операций по ней)
//  Как организовать проведение транзакций по карте?
//  Хранить операции по изменению статуса карты вместе с переводами?
//  Создать отдельную сущность в которой будут храниться изменения по карте?
//  Хранить все изменения по карте, включая создание и изменение параметров?
//  Тогда как хранить операции (денежные отдельно от неденежных?)
//  Зачем нужна таблица с картами, если карту можно воссоздать по операциям?
//  Может лучше хранить операции и по составному ключу (cardId и operationId) получать последнее состояние карты?
//  Как организовать транзакции? Может ли пользователь уйти в минус?
//  Может надо сделать транзакции в виде двойной записи (Дт и Кт)


