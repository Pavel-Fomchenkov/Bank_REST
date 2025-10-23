package com.example.bankcards.repository;

import com.example.bankcards.entity.UserRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface UserRequestRepository extends JpaRepository<UserRequest, Long> {
    boolean existsByCardIdAndTypeAndResultIn(Long cardId, UserRequest.Type type, List<UserRequest.Result> results);

    Page<UserRequest> findByInitiatorId(long id, Pageable pageable);

    Page<UserRequest> findByEntryDateBetween(Instant dateFrom, Instant dateTo, Pageable pageable);
}