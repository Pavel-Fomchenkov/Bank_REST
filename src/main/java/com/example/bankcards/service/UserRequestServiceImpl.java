package com.example.bankcards.service;

import com.example.bankcards.dto.UserRequestCreateDTO;
import com.example.bankcards.dto.UserRequestDTO;
import com.example.bankcards.entity.UserRequest;
import com.example.bankcards.repository.UserRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class UserRequestServiceImpl implements UserRequestService {
    private final UserRequestRepository repository;

    @Override
    public UserRequest createRequest(UserRequestCreateDTO request) {
        return null;
    }

    @Override
    public Page<UserRequest> getAll(Pageable pageable) {
        return null;
    }

    @Override
    public UserRequest getById(Long id) {
        return null;
    }

    @Override
    public Page<UserRequest> getByUsername(String username, Pageable pageable) {
        return null;
    }

    @Override
    public Page<UserRequest> getByEntryDate(Instant date, Pageable pageable) {
        return null;
    }

    @Override
    public UserRequest executeRequest(UserRequestDTO requestDTO, UserRequest.Result result) {
        return null;
    }
}
