package com.example.bankcards.controller;

import com.example.bankcards.dto.ChangePasswordDTO;
import com.example.bankcards.dto.UserDTO;
import com.example.bankcards.service.UserService;
import com.example.bankcards.util.UserMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService service;
    private final UserMapper mapper;

    @GetMapping(value = "/all", produces = {"application/json"})
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<UserDTO>> getAll(@RequestParam(defaultValue = "0") int pageNumber,
                                                @RequestParam(defaultValue = "10") int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return ResponseEntity.ok(service.getAll(pageable).stream()
                .map(mapper::mapToUserDTO).toList());
    }

    @GetMapping(value = "/name", produces = {"application/json"})
    public ResponseEntity<UserDTO> getByUsername(String username) {
        return ResponseEntity.ok(mapper.mapToUserDTO(service.getByUsername(username)));
    }

    @GetMapping(value = "/{id}", produces = {"application/json"})
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserDTO> getById(Long id) {
        return ResponseEntity.ok(mapper.mapToUserDTO(service.getById(id)));
    }

    @PatchMapping(value = "/changePassword", produces = {"application/json"})
    public ResponseEntity<Void> changePassword(@Valid @RequestBody ChangePasswordDTO passwordDTO) {
        service.changeOwnPassword(passwordDTO);
        return ResponseEntity.ok().build();
    }

    @PatchMapping(value = "/changePassword/{id}", produces = {"application/json"})
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> changePassword(@PathVariable(name = "id") Long id, @Valid @RequestBody ChangePasswordDTO passwordDTO) {
        service.changePassword(id, passwordDTO);
        return ResponseEntity.ok().build();
    }
}
