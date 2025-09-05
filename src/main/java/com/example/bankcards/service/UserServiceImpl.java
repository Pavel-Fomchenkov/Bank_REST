package com.example.bankcards.service;

import com.example.bankcards.dto.SignUpRequest;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.AlreadyExistsException;
import com.example.bankcards.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Создание нового пользователя
     *
     * @param request SignUpRequest
     * @return user - User
     */
    @Override
    public User create(SignUpRequest request) {
        logger.info("Запущен метод create из UserService");
        if (repository.existsByUsername(request.getUsername())) {
            logger.error("Пользователь {} уже существует", request.getUsername());
            throw new AlreadyExistsException("Пользователь " + request.getUsername() + " уже существует");
        }
        return repository.save(User.builder()
                .username(request.getUsername())
                .passwordEncrypted(passwordEncoder.encode(request.getPassword()))
                .role(User.Role.USER)
                .entryDate(Instant.now())
                .build()
        );
    }

    /**
     * Получение пользователя по имени пользователя
     *
     * @return пользователь
     */
    // TODO убрать закомментированные строки, если и так все работает
    @Override
    @Transactional
    public User getByUsername(String username) {
        logger.info("Запущен метод getByUsername из UserService");
        if (!isAdminOrCurrentUser(username)) {
            throw new AccessDeniedException("Неверное имя или доступ запрещен");
        }
//        Hibernate.initialize(user.getRole());
//        Hibernate.initialize(user.getCards());
        return repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
    }

    /**
     * Получение пользователя по id пользователя
     *
     * @return пользователь
     */
    @Override
    public User getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));
    }

    /**
     * Получение текущего пользователя на основе контекста Spring Security
     *
     * @return текущий пользователь
     */
    @Override
    public User getCurrentUser() {
        logger.info("Запущен метод getCurrentUser из UserService");
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return repository.findByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException("Пользователь " + username + " не найден"));
    }

    @Override
    public boolean isAdminOrCurrentUser(String username) {
        User currentUser = getCurrentUser();
        return (currentUser.getRole().equals(User.Role.ADMIN) || currentUser.getUsername().equals(username));
    }

    @Override
    public Page<User> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

}
