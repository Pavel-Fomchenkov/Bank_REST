package com.example.bankcards.service;

import com.example.bankcards.dto.CardCreateDTO;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.InactiveCardException;
import com.example.bankcards.exception.InsufficientFundsException;
import com.example.bankcards.repository.CardRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Isolation;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
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
    @PersistenceContext
    private EntityManager entityManager;


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
                .status(Card.Status.ACTIVE)
                .creditLimit(cardCreateDTO.getCreditLimit())
                .balance(BigDecimal.ZERO)
                .build());
    }

    @Override
    public Card createServiceCard(String description) {
        logger.info("Запущен метод createServiceCard из CardService");
        return repository.save(Card.builder()
                .description(description)
                .numberEncrypted(encryptNumber("0000000000000000"))
                .numberMasked(maskNumber("0000000000000000"))
                .owner(userService.getByUsername(SecurityContextHolder.getContext().getAuthentication().getName()))
                .entryDate(Instant.now())
                .expirationDate(calculateExpiration())
                .status(Card.Status.ACTIVE)
                .creditLimit(new BigDecimal("99999999999999999.99"))
                .balance(BigDecimal.ZERO)
                .build());
    }

    // TODO нужно сделать метод продления карты

    @Override
    @Transactional
    public boolean blockCard(long cardId) {
        return repository.changeStatus(cardId, Card.Status.BLOCKED) > 0;
    }

    @Override
    @Transactional
    public boolean activateCard(long cardId) {
        return repository.changeStatus(cardId, Card.Status.ACTIVE) > 0;
    }

    @Override
    public void deleteCard(long cardId) {

    }

    @Override
    public Page<Card> getAll(Pageable pageable) {
        if(userService.getCurrentUser().getRole().equals(User.Role.ADMIN)) {
            return repository.findAll(pageable);
        }
        return Page.empty();
    }

    @Override
    public Page<Card> getByStatus(Card.Status status, Pageable pageable) {
        if(userService.getCurrentUser().getRole().equals(User.Role.ADMIN)) {
            return repository.findByStatus(status, pageable);
        }
        return Page.empty();
    }

    @Override
    public Page<Card> getByUsername(String username, Pageable pageable) {
        User currentUser = userService.getCurrentUser();
        if(currentUser.getUsername().equals(username) || currentUser.getRole().equals(User.Role.ADMIN)) {
            return repository.findByOwnerUsername(username, pageable);
        }
        return Page.empty();
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

    // TODO сделать корректный отлов исключений
    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public boolean executeTransaction(Long fromCardId, Long toCardId, BigDecimal amount) {
        logger.info("Запущен метод executeTransaction из CardService");
        User currentUser = userService.getCurrentUser();

        if (amount.compareTo(BigDecimal.ZERO) < 0 && !currentUser.getRole().equals(User.Role.ADMIN)) {
            return false;
        }

        Card fromCard = entityManager.find(Card.class, fromCardId, LockModeType.PESSIMISTIC_WRITE);
        Card toCard = entityManager.find(Card.class, toCardId, LockModeType.PESSIMISTIC_WRITE);

        if (!fromCard.getStatus().equals(Card.Status.ACTIVE) || !toCard.getStatus().equals(Card.Status.ACTIVE)){
            throw new InactiveCardException("Операция с устаревшей или заблокированной картой");
        }

        if (amount.compareTo(BigDecimal.ZERO) > 0 &&
                fromCard.getBalance().add(fromCard.getCreditLimit()).compareTo(amount) < 0) {
            throw new InsufficientFundsException("Недостаточно средств");
        } else if (amount.compareTo(BigDecimal.ZERO) < 0 &&
                toCard.getBalance().add(amount).compareTo(toCard.getCreditLimit().negate()) < 0) {
            throw new InsufficientFundsException("Недостаточно средств");
        }

        if (currentUser.getRole().equals(User.Role.ADMIN) || fromCard.getOwner().equals(currentUser)) {
            fromCard.setBalance(fromCard.getBalance().subtract(amount));
            toCard.setBalance(toCard.getBalance().add(amount));
            entityManager.persist(fromCard);
            entityManager.persist(toCard);
            return true;
        }
        return false;
    }
}


// TODO тут должны быть методы:
//  + создание карты с нулевым балансом
//  - получение информации о карте
//  - изменение реквизитов карты (наверное, разрешить менять только описание и дату истечения срока карты)
//  - блокировки карты
//  - разблокировки карты
//  - удаления карты (только при отсутствии денежных операций по ней)
//  Хранить все изменения по карте, включая создание и изменение параметров?
//  Зачем нужна таблица с картами, если карту можно воссоздать по операциям?
//  Может лучше хранить операции и по составному ключу (cardId и operationId) получать последнее состояние карты?
//  Как организовать транзакции? Может ли пользователь уйти в минус?


