package com.example.bankcards.service;

import com.example.bankcards.dto.ChangePasswordDTO;
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
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
    @Override
    @Transactional
    public User getByUsername(String username) {
        logger.info("Запущен метод getByUsername из UserService");
        if (!isAdminOrCurrentUser(username)) {
            throw new AccessDeniedException("Неверное имя или доступ запрещен");
        }
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
        if (!getCurrentUser().getRole().equals(User.Role.ADMIN)) {
            throw new AccessDeniedException("Доступ ограничен");
        }
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с id " + id + " не найден"));
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
    public Boolean isAdminOrCurrentUser(String username) {
        User currentUser = getCurrentUser();
        return (currentUser.getRole().equals(User.Role.ADMIN) || currentUser.getUsername().equals(username));
    }

    @Override
    public Page<User> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public void changeOwnPassword(ChangePasswordDTO passwordDTO) {
        if (!passwordDTO.getNewPassword().equals(passwordDTO.getNewPasswordConfirmed())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Новый и подтвержденный пароли не совпадают.");
        }
        User currentUser = getCurrentUser();
        if (!passwordEncoder.matches(passwordDTO.getOldPassword(), currentUser.getPasswordEncrypted())) {
            throw new AccessDeniedException("Некорректный пароль");
        }
        User userFromDb = getByUsername(currentUser.getUsername());
        userFromDb.setPasswordEncrypted(passwordEncoder.encode(passwordDTO.getNewPassword()));
        repository.save(userFromDb);
    }

    @Override
    public void changePassword(Long id, ChangePasswordDTO passwordDTO) {
        if (!passwordDTO.getNewPassword().equals(passwordDTO.getNewPasswordConfirmed())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Новый и подтвержденный пароли не совпадают.");
        }
        User userFromDb = getById(id);
        userFromDb.setPasswordEncrypted(passwordEncoder.encode(passwordDTO.getNewPassword()));
        repository.save(userFromDb);
    }

    @Override
    public void changeRole(Long id, User.Role role) {
        User userFromDb = getById(id);
        userFromDb.setRole(role);
        repository.save(userFromDb);
    }
}
