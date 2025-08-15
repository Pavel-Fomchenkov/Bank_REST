package com.example.bankcards.dto;

import com.example.bankcards.entity.Role;
import lombok.Data;

import java.time.Instant;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private Role role;
    private Instant entryDate;
}
