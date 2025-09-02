package com.example.bankcards.repository;

import com.example.bankcards.entity.Card;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    @Modifying
    @Query("UPDATE Card c SET c.status = :newStatus WHERE c.id = :id")
    int changeStatus(@Param("id") long cardId, @Param("newStatus") Card.Status newStatus);

    Page<Card> findByOwnerUsername(String username, Pageable pageable);

    Page<Card> findByStatus(Card.Status status, Pageable pageable);
}
