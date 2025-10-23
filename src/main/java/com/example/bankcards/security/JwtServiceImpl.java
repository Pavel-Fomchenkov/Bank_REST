package com.example.bankcards.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.lang.Function;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtServiceImpl implements JwtService {
    @Value("${jwt.secret}")
    private String jwtSigningKey;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Извлечение имени пользователя из токена
     *
     * @param token токен
     * @return имя пользователя
     */
    @Override
    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Генерация JWT токена
     *
     * @param userDetails данные пользователя
     * @return токен
     */
    @Override
    public String generateToken(UserDetails userDetails) {
        logger.info("Запущен метод generateToken из JwtService");
        if (!(userDetails instanceof CustomUserDetails user)) {
            throw new IllegalArgumentException("UserDetails must be an instance of CustomUserDetails");
        }
        return Jwts.builder()
                .id(user.user().getId().toString())
                .subject(user.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 100000 * 60 * 24))
                .signWith(getSigningKey()).compact();
    }

    /**
     * Проверка токена на валидность, должна включать проверку подписи токена
     *
     * @param token       токен
     * @param userDetails данные пользователя
     * @return true, если токен валиден
     */
    //  TODO При этом проверка токена проверяется в методе doFilterInternal и требует UserDetails,
    //   что вероятно приводит к запросам в базу данных при каждой операции пользователя
    //   Нормально ли это, перегружает базу данных или обеспечивает безопасность?

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        logger.info("Запущен метод isTokenValid из JwtService");
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token) && verifySignature(token));
    }

    /**
     * Извлечение данных из токена
     *
     * @param token           токен
     * @param claimsResolvers функция извлечения данных
     * @param <T>             тип данных
     * @return данные
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
        final Claims claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }

    /**
     * Проверка токена на просроченность
     *
     * @param token токен
     * @return true, если токен просрочен
     */
    private boolean isTokenExpired(String token) {
        logger.info("Запущен метод isTokenExpired из JwtService");
        return extractExpiration(token).before(new Date());
    }

    /**
     * Извлечение даты истечения токена
     *
     * @param token токен
     * @return дата истечения
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Извлечение всех данных из токена
     *
     * @param token токен
     * @return данные
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Получение ключа для подписи токена
     *
     * @return ключ
     */
    private SecretKey getSigningKey() {
        logger.info("Запущен метод getSigningKey из JwtService");
        byte[] keyBytes = jwtSigningKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private boolean verifySignature(String token) {
        logger.info("Запущен метод verifySignature из JwtService");
        try {
            Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(jwtSigningKey.getBytes(StandardCharsets.UTF_8)))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            logger.info("Invalid JWT signature.");
            return false;
        }
    }
}
