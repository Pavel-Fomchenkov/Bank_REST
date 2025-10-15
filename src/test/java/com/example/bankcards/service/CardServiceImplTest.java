package com.example.bankcards.service;

import com.example.bankcards.dto.CardCreateDTO;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import jakarta.persistence.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD) // Очищает контекст после каждого теста, иначе тесты мешают друг другу
class CardServiceImplTest {
    @Autowired
    private CardServiceImpl service;
    @Autowired
    private CardRepository repository;
    @Autowired
    private CardDeleteServiceImpl deleteService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder encoder;
    @Mock
    private UserService userService;

    @Test
    @WithMockUser(username = "testUser1", roles = {"ADMIN"})
    void testCreate() {
        User admin = prepareTestUser(1L, User.Role.ADMIN);
        User owner = prepareTestUser(2L, User.Role.USER);

        CardCreateDTO dto = new CardCreateDTO();
        dto.setDescription("testCard");
        dto.setCardNumber("1234567890123456");
        dto.setCreditLimit(BigDecimal.TEN);

        Card result = service.create(2L, dto);

        assertNotNull(result.getId());
        assertEquals(dto.getDescription(), result.getDescription());
        assertEquals("3456", result.getNumberMasked());
        assertNotNull(result.getNumberEncrypted());
        assertEquals(owner.getId(), result.getOwner().getId());
        assertEquals(Instant.now().truncatedTo(ChronoUnit.SECONDS),
                result.getEntryDate().truncatedTo(ChronoUnit.SECONDS));
        assertNotNull(result.getExpirationDate());
        assertEquals(Card.Status.ACTIVE, result.getStatus());
        assertEquals(dto.getCreditLimit(), result.getCreditLimit());
        assertEquals(BigDecimal.ZERO, result.getBalance());
    }

    @Test
    @WithMockUser(username = "testUser2", roles = {"USER"})
    void testNegativeCreate() {
        User owner = prepareTestUser(2L, User.Role.USER);

        CardCreateDTO dto = new CardCreateDTO();
        dto.setDescription("testCard");
        dto.setCardNumber("1234567890123456");
        dto.setCreditLimit(BigDecimal.TEN);

        assertThrows(AccessDeniedException.class, () -> service.create(2L, dto));
    }

    @Test
    @WithMockUser(username = "testUser1", roles = {"ADMIN"})
    void testCreateServiceCard() {
        prepareTestUser(1L, User.Role.ADMIN);
        Card result = service.createServiceCard("testServiceCard");

        assertNotNull(result.getId());
        assertEquals("testServiceCard", result.getDescription());
        assertEquals("0000", result.getNumberMasked());
        assertTrue(encoder.matches("0000000000000000", result.getNumberEncrypted()));
        assertEquals(1L, result.getOwner().getId());
        assertEquals(Instant.now().truncatedTo(ChronoUnit.SECONDS),
                result.getEntryDate().truncatedTo(ChronoUnit.SECONDS));
        assertNotNull(result.getExpirationDate());
        assertEquals(Card.Status.ACTIVE, result.getStatus());
        assertNotNull(result.getCreditLimit());
        assertEquals(BigDecimal.ZERO, result.getBalance());
    }

    @Test
    @WithMockUser(username = "testUser1", roles = {"ADMIN"})
    void testBlockCard() {
        User admin = prepareTestUser(1L, User.Role.ADMIN);
        User owner = prepareTestUser(2L, User.Role.USER);
        Card testCard = prepareTestCard(owner);

        boolean result = service.changeStatus(testCard.getId(), Card.Status.BLOCKED);
        assertTrue(result);

        Card cardFromBD = repository.findById(testCard.getId())
                .orElseThrow(() -> new EntityNotFoundException("Отсутствует карта в базе данных"));

        assertNotNull(cardFromBD.getId());
        assertEquals(Card.Status.BLOCKED, cardFromBD.getStatus());
    }

    @Test
    @WithMockUser(username = "testUser1", roles = {"ADMIN"})
    void testActivateCard() {
        User admin = prepareTestUser(1L, User.Role.ADMIN);
        User owner = prepareTestUser(2L, User.Role.USER);
        Card testCard = prepareTestCard(owner);
        assertTrue(service.changeStatus(testCard.getId(), Card.Status.BLOCKED));

        boolean result = service.changeStatus(testCard.getId(), Card.Status.ACTIVE);
        assertTrue(result);

        Card cardFromBD = repository.findById(testCard.getId())
                .orElseThrow(() -> new EntityNotFoundException("Отсутствует карта в базе данных"));

        assertNotNull(cardFromBD.getId());
        assertEquals(Card.Status.ACTIVE, cardFromBD.getStatus());
    }

    @Test
    @WithMockUser(username = "testUser1", roles = {"ADMIN"})
    void testDeleteCard() {
        User admin = prepareTestUser(1L, User.Role.ADMIN);
        User owner = prepareTestUser(2L, User.Role.USER);
        Card testCard = prepareTestCard(owner);
        assertNotNull(repository.findById(testCard.getId()));

        assertTrue(service.deleteCard(testCard.getId()));
        assertTrue(repository.findById(testCard.getId()).isEmpty());
    }

    @Test
    @WithMockUser(username = "testUser1", roles = {"ADMIN"})
    void testGetAll() {
        User admin = prepareTestUser(1L, User.Role.ADMIN);
        User owner1 = prepareTestUser(2L, User.Role.USER);
        User owner2 = prepareTestUser(3L, User.Role.USER);
        prepareTestCard(owner1);
        prepareTestCard(owner1);
        prepareTestCard(owner1);
        prepareTestCard(owner2);
        Pageable pageable = PageRequest.of(0, 10);

        Page<Card> cards = service.getAll(pageable);

        assertEquals(4, cards.getTotalElements());
    }

    @Test
    @WithMockUser(username = "testUser1", roles = {"ADMIN"})
    void getByStatus() {
        User admin = prepareTestUser(1L, User.Role.ADMIN);
        User owner1 = prepareTestUser(2L, User.Role.USER);
        prepareTestCard(owner1);
        prepareTestCard(owner1);
        Card cardToBeBlocked = prepareTestCard(owner1);
        service.changeStatus(cardToBeBlocked.getId(), Card.Status.BLOCKED);
        Pageable pageable = PageRequest.of(0, 10);

        Page<Card> cards = service.getByStatus(Card.Status.ACTIVE, pageable);
        assertEquals(2, cards.getTotalElements());

        Page<Card> blockedCards = service.getByStatus(Card.Status.BLOCKED, pageable);
        assertEquals(1, blockedCards.getTotalElements());
    }

    @Test
    @WithMockUser(username = "testUser1", roles = {"ADMIN"})
    void testGetByUsername() {
        User admin = prepareTestUser(1L, User.Role.ADMIN);
        User owner1 = prepareTestUser(2L, User.Role.USER);
        User owner2 = prepareTestUser(3L, User.Role.USER);
        prepareTestCard(owner1);
        prepareTestCard(owner1);
        prepareTestCard(owner1);
        prepareTestCard(owner2);
        Pageable pageable = PageRequest.of(0, 10);

        Page<Card> cards = service.getByUsername(owner1.getUsername(), pageable);
        assertEquals(3, cards.getTotalElements());
    }

//        @Test
//        void getByUsernamePart () {
//        }

    @Test
    @WithMockUser(username = "testUser1", roles = {"ADMIN"})
    void getById() {
        User admin = prepareTestUser(1L, User.Role.ADMIN);
        User owner1 = prepareTestUser(2L, User.Role.USER);
        prepareTestCard(owner1);
        prepareTestCard(owner1);
        Card cardToFind = prepareTestCard(owner1);

        assertEquals(cardToFind.getId(), service.getById(cardToFind.getId()).getId());
    }

    //        @Test
//        void expireCards () {
//        }
//
    @Test
    @WithMockUser(username = "testUser1", roles = {"ADMIN"})
    void testExpireCard() {
        User admin = prepareTestUser(1L, User.Role.ADMIN);
        User owner = prepareTestUser(2L, User.Role.USER);
        Card testCard = prepareTestCard(owner);
        service.changeStatus(testCard.getId(), Card.Status.EXPIRED);

        assertEquals(Card.Status.EXPIRED, service.getById(testCard.getId()).getStatus());
    }

    @Test
    @WithMockUser(username = "testUser1", roles = {"ADMIN"})
    void testProlongCard() {
        User admin = prepareTestUser(1L, User.Role.ADMIN);
        User owner = prepareTestUser(2L, User.Role.USER);
        Card testCard = prepareTestCard(owner);
        service.changeStatus(testCard.getId(), Card.Status.EXPIRED);

        service.prolongCard(testCard.getId(), 1000);

        assertTrue(service.getById(testCard.getId()).getExpirationDate()
                .isAfter(Instant.now().plus(999L, ChronoUnit.DAYS)));
    }

    @Test
    @WithMockUser(username = "testUser1", roles = {"ADMIN"})
    void executeTransaction() {
        User admin = prepareTestUser(1L, User.Role.ADMIN);
        User owner = prepareTestUser(2L, User.Role.USER);
        Card serviceCard = service.createServiceCard("testServiceCard");
        Card testCard = prepareTestCard(owner);

        service.executeTransaction(serviceCard.getId(), testCard.getId(), BigDecimal.valueOf(100.0));
        assertEquals(0, service.getById(testCard.getId()).getBalance().compareTo(BigDecimal.valueOf(100.0)));
    }

    private User prepareTestUser(long id, User.Role role) {
        String username = "testUser" + id;
        User user = new User(id, username, role,
                "testPassword", Instant.now(), new HashSet<>());
        userRepository.save(user);
        return user;
    }

    private Card prepareTestCard(User owner) {
        CardCreateDTO dto = new CardCreateDTO();
        dto.setDescription("testCard");
        dto.setCardNumber("1234 1234 1234 1234");
        dto.setCreditLimit(BigDecimal.valueOf(1L));
        return service.create(owner.getId(), dto);
    }
}