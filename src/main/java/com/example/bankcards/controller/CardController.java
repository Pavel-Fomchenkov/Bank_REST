package com.example.bankcards.controller;

import com.example.bankcards.dto.CardCreateDTO;
import com.example.bankcards.dto.CardDTO;
import com.example.bankcards.entity.Card;
import com.example.bankcards.service.CardService;
import com.example.bankcards.util.CardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/card")
public class CardController {
    private final CardMapper mapper;
    private final CardService cardService;

    @GetMapping
    public ResponseEntity<String> getFirst() {
        System.out.println("first");
        return ResponseEntity.ok("first");
    }

    @GetMapping("/two")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> getSecond() {
        return ResponseEntity.ok("second");
    }

    @GetMapping(value = "/{id}", produces = {"application/json"})
    public ResponseEntity<CardDTO> getById(@PathVariable long id) {
        Card card = cardService.getById(id);
        return ResponseEntity.ok(mapper.mapToCardDTO(card));
    }

    @PostMapping("/create/{ownerId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<CardDTO> createCard(@PathVariable(name = "ownerId") long ownerId, @RequestBody CardCreateDTO cardCreateDTO) {
        Card newCard = cardService.create(ownerId, cardCreateDTO);
        return ResponseEntity.ok(mapper.mapToCardDTO(newCard));
    }

    @PatchMapping("/block")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Boolean> blockCard(@RequestParam(name = "cardId") long cardId) {
        return ResponseEntity.ok(cardService.blockCard(cardId));
    }

    @PatchMapping("/activate")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Boolean> activateCard(@RequestParam(name = "cardId") long cardId) {
        return ResponseEntity.ok(cardService.activateCard(cardId));
    }

}