package com.example.bankcards.security;

import com.example.bankcards.entity.User;
import com.example.bankcards.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Получение пользователя по имени пользователя
     * <p>
     * Нужен для Spring Security
     *
     * @return {@link com.example.bankcards.security.CustomUserDetails}
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws EntityNotFoundException {
        logger.info("Запущен метод loadUserByUsername из UserDetailsService");
        User user = userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException("Пользователь " + username + " не найден"));
        return new CustomUserDetails(user);
    }
}
