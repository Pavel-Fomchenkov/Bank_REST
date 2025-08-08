package com.example.bankcards.service;

import com.example.bankcards.dto.SignUpRequest;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.UserNotFoundException;
import com.example.bankcards.exception.UsernameAlreadyExistsException;
import com.example.bankcards.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

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
            throw new UsernameAlreadyExistsException("Пользователь " + request.getUsername() + " уже существует");
        }
        return repository.save(User.builder()
                .username(request.getUsername())
                .passwordEncrypted(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
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
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
    }

    /**
     * Получение текущего пользователя
     *
     * @return текущий пользователь
     */
    @Override
    public User getCurrentUser() {
        // Получение имени пользователя из контекста Spring Security
        logger.info("Запущен метод getCurrentUser из UserService");
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return getByUsername(username);
    }
}
