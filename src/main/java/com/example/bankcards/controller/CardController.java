package com.example.bankcards.controller;

import com.example.bankcards.dto.CardCreateDTO;
import com.example.bankcards.dto.CardDTO;
import com.example.bankcards.entity.Card;
import com.example.bankcards.service.CardService;
import com.example.bankcards.util.CardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/card")
public class CardController {
    private final CardMapper mapper;
    private final CardService service;

    @GetMapping(value = "/all", produces = {"application/json"})
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<CardDTO>> getAll(@RequestParam(defaultValue = "0") int pageNumber,
                                                @RequestParam(defaultValue = "10") int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return ResponseEntity.ok(service.getAll(pageable).stream()
                .map(mapper::mapToCardDTO).toList());
    }


    @GetMapping(value = "/{id}", produces = {"application/json"})
    public ResponseEntity<CardDTO> getById(@PathVariable long id) {
        Card card = service.getById(id);
        return ResponseEntity.ok(mapper.mapToCardDTO(card));
    }

    @GetMapping(value = "/name", produces = {"application/json"})
    public ResponseEntity<List<CardDTO>> getByUsername(@RequestParam(name = "name") String name,
                                                       @RequestParam(defaultValue = "0") int pageNumber,
                                                       @RequestParam(defaultValue = "10") int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return ResponseEntity.ok(service.getByUsername(name, pageable).stream()
                .map(mapper::mapToCardDTO).toList());
    }

    @GetMapping(value = "/status", produces = {"application/json"})
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<CardDTO>> getByStatus(@RequestParam(name = "status") Card.Status status,
                                                     @RequestParam(defaultValue = "0") int pageNumber,
                                                     @RequestParam(defaultValue = "10") int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return ResponseEntity.ok(service.getByStatus(status, pageable).stream()
                .map(mapper::mapToCardDTO).toList());
    }

    @PostMapping("/create/{ownerId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<CardDTO> createCard(@PathVariable(name = "ownerId") long ownerId, @RequestBody CardCreateDTO cardCreateDTO) {
        Card newCard = service.create(ownerId, cardCreateDTO);
        return ResponseEntity.ok(mapper.mapToCardDTO(newCard));
    }

    @PostMapping("/service")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<CardDTO> createServiceCard(@RequestParam(name = "description") String description) {
        Card newCard = service.createServiceCard(description);
        return ResponseEntity.ok(mapper.mapToCardDTO(newCard));
    }

    @PatchMapping("/status")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Boolean> changeStatus(@RequestParam(name = "cardId") long cardId, Card.Status status) {
        return ResponseEntity.ok(service.changeStatus(cardId, status));
    }

    @PatchMapping("/prolong")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<CardDTO> prolongCard(@RequestParam(name = "cardId") long cardId,
                                               @RequestParam(name = "days") int days) {
        return ResponseEntity.ok(mapper.mapToCardDTO(service.prolongCard(cardId, days)));
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Boolean> deleteCard(@RequestParam(name = "cardId") long cardId){
        return ResponseEntity.ok(service.deleteCard(cardId));
    }

}