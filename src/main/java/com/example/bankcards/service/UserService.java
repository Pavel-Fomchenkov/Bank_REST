package com.example.bankcards.service;

import com.example.bankcards.dto.ChangePasswordDTO;
import com.example.bankcards.dto.SignUpRequest;
import com.example.bankcards.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    User create(SignUpRequest request);

    User getByUsername(String username);

    User getById(Long id);

    User getCurrentUser();

    Boolean isAdminOrCurrentUser(String username);

    Page<User> getAll(Pageable pageable);

    void changeOwnPassword(ChangePasswordDTO passwordDTO);

    void changePassword(Long id, ChangePasswordDTO passwordDTO);

    void changeRole(Long id, User.Role role);
}
