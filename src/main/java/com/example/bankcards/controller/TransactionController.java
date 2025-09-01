package com.example.bankcards.controller;

import com.example.bankcards.dto.TransactionDTO;
import com.example.bankcards.service.TransactionService;
import com.example.bankcards.util.TransactionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/transaction")
public class TransactionController {
    private final TransactionMapper mapper;
    private final TransactionService service;

    @PostMapping("/putMoney")
    public ResponseEntity<TransactionDTO> makeTransaction(TransactionDTO transactionDTO) {
        return ResponseEntity.ok(mapper.mapToTransactionDTO(service.makeTransaction(transactionDTO)));
    }
}