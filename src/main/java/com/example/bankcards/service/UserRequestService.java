package com.example.bankcards.service;

import com.example.bankcards.dto.UserRequestCreateDTO;
import com.example.bankcards.dto.UserRequestDTO;
import com.example.bankcards.entity.UserRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;

public interface UserRequestService {
    UserRequest createRequest(UserRequestCreateDTO request);

    Page<UserRequest> getAll(Pageable pageable);

    UserRequest getById(Long id);

    Page<UserRequest> getByUsername(String username, Pageable pageable);

    Page<UserRequest> getByEntryDate(Instant date, Pageable pageable);

    UserRequest executeRequest(UserRequestDTO requestDTO, UserRequest.Result result);
}
