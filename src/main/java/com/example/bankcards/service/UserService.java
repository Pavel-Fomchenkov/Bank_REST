package com.example.bankcards.service;

import com.example.bankcards.dto.SignUpRequest;
import com.example.bankcards.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;

public interface UserService {
    User create(SignUpRequest request);

    User getByUsername(String username);

    User getById(Long id);

    User getCurrentUser();

    boolean isAdminOrCurrentUser(String username);

    Page<User> getAll(Pageable pageable);
}
