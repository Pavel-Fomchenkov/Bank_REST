package com.example.bankcards.service;

import com.example.bankcards.dto.SignUpRequest;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.AlreadyExistsException;
import com.example.bankcards.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository repository;
    @Mock
    private PasswordEncoder passwordEncoder;

    private UserService service;

    @BeforeEach
    void setUp() {      // creating service with mocks
        this.service = new UserServiceImpl(repository, passwordEncoder);
    }

    @Test
    void testCreateNewUser() {
        SignUpRequest request = new SignUpRequest("test_user", "test_password");
        when(repository.save(any(User.class))).thenReturn(User.builder().username("test_user").build());
        User createdUser = service.create(request);
        assertEquals("test_user", createdUser.getUsername());
    }

    @Test
    void testCreateExistingUser() {
        SignUpRequest request = new SignUpRequest("test_user", "test_password");
        when(repository.existsByUsername("test_user")).thenReturn(true);
        assertThrows(AlreadyExistsException.class, () -> service.create(request));
    }

    @Test
    void testGetByUsernameSuccess() {
        setupAuthAndContext("test_user", Collections.emptyList());
        User expectedUser = User.builder().username("test_user").role(User.Role.USER).build();
        when(repository.findByUsername("test_user")).thenReturn(Optional.of(expectedUser));
        assertEquals(service.getByUsername("test_user"), expectedUser);
    }

    @Test
    void testGetByUsernameAdminSuccess() {
        setupAuthAndContext("admin", List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));
        User expectedUser = User.builder().username("test_user").build();
        when(repository.findByUsername("admin")).thenReturn(Optional.of(User.builder().username("admin").role(User.Role.ADMIN).build()));
        when(repository.findByUsername("test_user")).thenReturn(Optional.of(expectedUser));
        assertEquals(service.getByUsername("test_user"), expectedUser);
    }

    @Test
    void testGetByUsernameNotFound() {
        setupAuthAndContext("admin", List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));
        assertThrows(UsernameNotFoundException.class, () -> service.getByUsername("nonexistent_user"));
    }

    @Test
    void testGetByUsernameAccessDenied() {
        setupAuthAndContext("user", List.of(new SimpleGrantedAuthority("ROLE_USER")));
        when(repository.findByUsername("user")).thenReturn(Optional.of(User.builder().username("user").role(User.Role.USER).build()));
        assertThrows(AccessDeniedException.class, () -> service.getByUsername("some_user")); // Проверяем выброс исключения
    }

    @Test
    void testGetByIdSuccess() {
        setupAuthAndContext("admin", List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));
        when(repository.findByUsername("admin")).thenReturn(Optional.of(User.builder().username("admin").role(User.Role.ADMIN).build()));
        User expectedUser = User.builder().username("user").build();
        when(repository.findById(1L)).thenReturn(Optional.of(expectedUser));
        assertEquals(service.getById(1L), expectedUser);
    }


    @Test
    void testGetByIdNotFound() {
        setupAuthAndContext("admin", List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));
        when(repository.findByUsername("admin")).thenReturn(Optional.of(User.builder().username("admin").role(User.Role.ADMIN).build()));
        when(repository.findById(-1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> service.getById(-1L));

    }

    @Test
    void testGetByIdAccessDenied() {
        setupAuthAndContext("user", List.of(new SimpleGrantedAuthority("ROLE_USER")));
        when(repository.findByUsername("user")).thenReturn(Optional.of(User.builder().username("user").role(User.Role.USER).build()));
        assertThrows(AccessDeniedException.class, () -> service.getById(5L));
    }

    @Test
    void testIsAdminOrCurrentUserCurrent() {
        setupAuthAndContext("user", List.of(new SimpleGrantedAuthority("ROLE_USER")));
        when(repository.findByUsername("user")).thenReturn(Optional.of(User.builder().username("user").role(User.Role.USER).build()));
        assertTrue(service.isAdminOrCurrentUser("user"));
    }

    @Test
    void testIsAdminOrCurrentUserAdmin() {
        setupAuthAndContext("admin", List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));
        when(repository.findByUsername("admin")).thenReturn(Optional.of(User.builder().username("admin").role(User.Role.ADMIN).build()));
        assertTrue(service.isAdminOrCurrentUser("admin"));
    }

    @Test
    void testIsAdminOrCurrentUserAnother() {
        setupAuthAndContext("user", List.of(new SimpleGrantedAuthority("ROLE_USER")));
        when(repository.findByUsername("user")).thenReturn(Optional.of(User.builder().username("admin").role(User.Role.USER).build()));
        assertFalse(service.isAdminOrCurrentUser("another_user"));
    }

    private void setupAuthAndContext(String username, List<GrantedAuthority> authorities) {
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn(username);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                "",
                authorities
        );
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }
}