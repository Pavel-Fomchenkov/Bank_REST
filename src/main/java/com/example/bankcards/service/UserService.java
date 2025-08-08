package com.example.bankcards.service;

import com.example.bankcards.dto.SignUpRequest;
import com.example.bankcards.entity.User;

public interface UserService {
    User create(SignUpRequest request);

    User getByUsername(String username);

    User getById(Long id);

    User getCurrentUser();
}
