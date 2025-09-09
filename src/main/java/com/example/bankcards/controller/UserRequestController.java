package com.example.bankcards.controller;

import com.example.bankcards.dto.UserRequestAdditionalDTO;
import com.example.bankcards.dto.UserRequestCreateDTO;
import com.example.bankcards.dto.UserRequestDTO;
import com.example.bankcards.entity.UserRequest;
import com.example.bankcards.service.UserRequestService;
import com.example.bankcards.util.UserRequestMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/request")
@RequiredArgsConstructor
public class UserRequestController {
    private final UserRequestService service;
    private final UserRequestMapper mapper;

    @PostMapping("/send")
    public ResponseEntity<UserRequestDTO> createRequest(UserRequestCreateDTO request) {
        return ResponseEntity.ok(mapper.mapToUserRequestDTO(service.createRequest(request)));
    }

    @GetMapping(value = "/all", produces = {"application/json"})
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<UserRequestDTO>> getAll(@RequestParam(defaultValue = "0") int pageNumber,
                                                       @RequestParam(defaultValue = "10") int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return ResponseEntity.ok(service.getAll(pageable).stream()
                .map(mapper::mapToUserRequestDTO).toList());
    }

    @GetMapping(value = "/{id}", produces = {"application/json"})
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserRequestAdditionalDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.mapToUserRequestAdditionalDTO(service.getById(id)));
    }

    @GetMapping(value = "/name", produces = {"application/json"})
    public ResponseEntity<List<UserRequestDTO>> getByUsername(@RequestParam(name = "name") String name,
                                                              @RequestParam(defaultValue = "0") int pageNumber,
                                                              @RequestParam(defaultValue = "10") int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return ResponseEntity.ok(service.getByUsername(name, pageable).stream()
                .map(mapper::mapToUserRequestDTO).toList());
    }

    @GetMapping(value = "/date", produces = {"application/json"})
    public ResponseEntity<List<UserRequestDTO>> getByEntryDate(@RequestParam Instant date,
                                                               @RequestParam(defaultValue = "0") int pageNumber,
                                                               @RequestParam(defaultValue = "10") int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return ResponseEntity.ok(service.getByEntryDate(date, pageable).stream()
                .map(mapper::mapToUserRequestDTO).toList());
    }

    @PatchMapping(value = "/execute", produces = {"application/json"})
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserRequestAdditionalDTO> executeRequest(UserRequestDTO requestDTO, UserRequest.Result result) {
        return ResponseEntity.ok(mapper.mapToUserRequestAdditionalDTO(service.executeRequest(requestDTO, result)));
    }

}
