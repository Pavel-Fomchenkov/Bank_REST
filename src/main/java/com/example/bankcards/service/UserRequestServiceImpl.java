package com.example.bankcards.service;

import com.example.bankcards.dto.UserRequestCreateDTO;
import com.example.bankcards.dto.UserRequestDTO;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.UserRequest;
import com.example.bankcards.exception.AlreadyExistsException;
import com.example.bankcards.repository.UserRequestRepository;
import com.example.bankcards.util.TimeZones;
import jakarta.persistence.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

// TODO нужно придумать как создать первого админа
//  реализовать смену роли
//  реализовать смену пароля

// TODO данные в базе хранятся в формате OffsetDateTime


@Service
@RequiredArgsConstructor
public class UserRequestServiceImpl implements UserRequestService {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final UserRequestRepository repository;
    private final UserService userService;
    private final CardService cardService;

    @Override
    public UserRequest createRequest(UserRequestCreateDTO request) {
        logger.info("Запущен метод createRequest из UserRequestService");

        if (existsActual(request.getCardId(), request.getType())) {
            throw new AlreadyExistsException("Незавершенный запрос типа: " + request.getType().name() + " уже существует");
        }
        return repository.save(UserRequest.builder()
                .initiator(userService.getCurrentUser())
                .card(cardService.getById(request.getCardId()))
                .entryDate(Instant.now())
                .type(request.getType())
                .result(UserRequest.Result.RECEIVED)
                .comment(request.getComment())
                .build());
    }

    @Override
    public Page<UserRequest> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public UserRequest getById(Long id) {
        logger.info("Запущен метод getById из UserRequestService");
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Запрос id " + id + " не найден"));
    }

    @Override
    public Page<UserRequest> getByUsername(String username, Pageable pageable) {
        if (userService.isAdminOrCurrentUser(username)) {
            long id = userService.getByUsername(username).getId();
            return repository.findByInitiatorId(id, pageable);
        }
        return Page.empty();
    }

    @Override
    public Page<UserRequest> getByEntryDate(LocalDate dateFrom, LocalDate dateTo, TimeZones timeZone, Pageable pageable) {
        Instant start = timeZone.atStartOfDay(dateFrom);
        Instant endDate = timeZone.atStartOfDay(dateTo.plusDays(1));
        return repository.findByEntryDateBetween(start, endDate, pageable);
    }

    @Override
    public UserRequest executeRequest(UserRequestDTO requestDTO, UserRequest.Result result, String comment) {
        UserRequest userRequest = getById(requestDTO.getId());
        userRequest.setResultComment(comment);
        userRequest.setResult(result);
        userRequest.setExecutor(userService.getCurrentUser());
        userRequest.setResultDate(Instant.now());
        if (result.equals(UserRequest.Result.COMPLETED)) {
            switch (userRequest.getType()) {
                case BLOCK -> cardService.changeStatus(userRequest.getCard().getId(), Card.Status.BLOCKED);
                case UNBLOCK -> cardService.changeStatus(userRequest.getCard().getId(), Card.Status.ACTIVE);
            }
        }
        return repository.save(userRequest);
    }

    private boolean existsActual(Long cardId, UserRequest.Type type) {
        List<UserRequest.Result> results = Arrays.asList(
                UserRequest.Result.RECEIVED,
                UserRequest.Result.INCOMPLETED,
                UserRequest.Result.SUSPENDED
        );
        return repository.existsByCardIdAndTypeAndResultIn(cardId, type, results);
    }

}
