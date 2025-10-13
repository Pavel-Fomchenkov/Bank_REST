package com.example.bankcards.service;

import com.example.bankcards.dto.CardCreateDTO;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class CardServiceImplTest {
    @Autowired
    private CardService service;
    @Autowired
    private CardRepository repository;
    private CardDeleteService deleteService;
    @Autowired
    private UserRepository userRepository;
    @Mock
    private UserService userService;

//    @BeforeEach
//    void setUp() {
//        this.service = new CardServiceImpl(repository, userService, deleteService);
//    }
//
//    @Test
//    @Order(3)
//    @WithMockUser(username = "admin_user", roles = {"ADMIN"})
//    void testCreate() {
//        long userId = 3L;
//        User user = this.prepareTestUser(userId, User.Role.USER);
//        when(userService.getById(userId)).thenReturn(user);
//
//        CardCreateDTO dto = new CardCreateDTO();
//        dto.setDescription("Description");
//        dto.setCardNumber("1234 1234 1234 1234");
//        dto.setCreditLimit(BigDecimal.valueOf(1000L));
//        Card card = service.create(userId, dto);
//
//        assertNotNull(card.getId());
//        assertEquals("Description", card.getDescription());
//        assertEquals("1234", card.getNumberMasked());
//        assertNotNull(card.getEntryDate());
//        assertNotNull(card.getExpirationDate());
//        assertEquals(Card.Status.ACTIVE, card.getStatus());
//        assertEquals(BigDecimal.valueOf(1000L), card.getCreditLimit());
//        assertEquals(BigDecimal.ZERO, card.getBalance());
//    }

//    private String numberEncrypted;
//    private String numberMasked;
//    private User owner;
//    private Instant entryDate;
//    private Instant expirationDate;
//    private Card.Status status;
//    private BigDecimal creditLimit;
//    private BigDecimal balance;


//    @Override
//    public Card create(long ownerId, CardCreateDTO cardCreateDTO) {
//        logger.info("Запущен метод create из CardService");
//
//        return repository.save(Card.builder()
//                .description(cardCreateDTO.getDescription())
//                .numberEncrypted(encryptNumber(cardCreateDTO.getCardNumber()))
//                .numberMasked(maskNumber(cardCreateDTO.getCardNumber()))
//                .owner(userService.getById(ownerId))
//                .entryDate(Instant.now())
//                .expirationDate(calculateExpiration())
//                .status(Card.Status.ACTIVE)
//                .creditLimit(cardCreateDTO.getCreditLimit())
//                .balance(BigDecimal.ZERO)
//                .build());
//    }

    @Test
    void createServiceCard() {
    }
//
//    @Test
//    @Order(6)
//    @Transactional
//    @WithMockUser(username = "testUser7", roles = {"USER"})
//    void testBlockCard() {
//        long userId = 7L;
//        User user = this.prepareTestUser(userId, User.Role.USER);
//        Card card = this.prepareTestCard(user);
//
//        assertTrue(service.blockCard(card.getId()));
//
//        Card updatedCard = repository.findById(card.getId()).get();
//        assertEquals(Card.Status.BLOCKED, updatedCard.getStatus());
//    }









/*    @Override
    @Transactional
    public boolean blockCard(long cardId) {
        return repository.changeStatus(cardId, Card.Status.BLOCKED) > 0;
    }*/


    @Test
    void activateCard() {
    }

    @Test
    void deleteCard() {
    }

    @Test
    void getAll() {
    }

    @Test
    void getByStatus() {
    }

    @Test
    void getByUsername() {
    }

    @Test
    void getByUsernamePart() {
    }

    @Test
    void getById() {
    }

    @Test
    void expireCards() {
    }

    @Test
    void expireCard() {
    }

    @Test
    void prolongCard() {
    }

    @Test
    void executeTransaction() {
    }

    private User prepareTestUser(long id, User.Role role) {
        String username = "testUser" + id;
        return userRepository.save(new User(id, username, role,
                "testPassword", Instant.now(), new HashSet<>()));
    }

    private Card prepareTestCard(User owner) {
        CardCreateDTO dto = new CardCreateDTO();
        dto.setDescription("testCard");
        dto.setCardNumber("1234 1234 1234 1234");
        dto.setCreditLimit(BigDecimal.valueOf(1L));
        when(userService.getById(owner.getId())).thenReturn(owner);
        return service.create(owner.getId(), dto);
    }

    @AfterEach
    void cleanup() {
        // Очистите данные после теста
        userRepository.deleteAll();
    }
}