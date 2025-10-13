package com.example.bankcards.controller;

import com.example.bankcards.entity.User;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.security.JwtAuthenticationFilter;
import com.example.bankcards.security.JwtService;
import com.example.bankcards.security.JwtServiceImpl;
import com.example.bankcards.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.Instant;
import java.util.HashSet;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(UserController.class)
@Import({JwtAuthenticationFilter.class, JwtServiceImpl.class, UserRepository.class})
class CardControllerTest {

//    @Autowired
//    private MockMvc mockMvc;
//    @MockBean
//    private UserService userService;
//    @MockBean
//    private UserController userController;
//
//    private long userId;
//    @Autowired
//    private UserRepository userRepository;
//
//    @BeforeEach
//    void createTestUser() {
//        this.userId = userRepository.save(new User(2L, "TestUser", User.Role.USER, "testPassword", Instant.now(), new HashSet<>())).getId();
//    }

    @Test
    void getAll() {
    }

    @Test
    void getById() {
    }

    @Test
    void getByUsername() {
    }

    @Test
    void getByStatus() {
    }

    @Test
    void createCard() {
    }

    @Test
    void createServiceCard() {


    }

//    @Test
//    @WithMockUser(username = "admin", password = "secret", roles = {"ADMIN"})
//    void createServiceCardByAdmin() throws Exception {
//        mockMvc.perform(
//                        get("/api/cards/{cardId}", 2L)
//                                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk());
//    }


    @Test
    void blockCard() {
    }

    @Test
    void activateCard() {
    }

    @Test
    void expireCard() {
    }

    @Test
    void prolongCard() {
    }

    @Test
    void deleteCard() {
    }
}