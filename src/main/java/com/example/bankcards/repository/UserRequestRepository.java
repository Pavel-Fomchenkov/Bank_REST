package com.example.bankcards.repository;

import com.example.bankcards.entity.UserRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRequestRepository extends JpaRepository<UserRequest, Long> {
    boolean existsByCardIdAndTypeAndResultIn(Long cardId, UserRequest.Type type, List<UserRequest.Result> results);
}