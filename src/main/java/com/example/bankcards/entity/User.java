package com.example.bankcards.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.Set;

@Entity
@Table(name = "users")
@RequiredArgsConstructor
public class User {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "password_encrypted")
    private String passwordEncripted;

    @Column(name = "entry_date")
    private Instant entryDate;

    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
    @JsonProperty("cards")
    private Set<Card> cards;
}
