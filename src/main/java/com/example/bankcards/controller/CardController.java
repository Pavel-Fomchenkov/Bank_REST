package com.example.bankcards.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/card")
public class CardController {

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
}