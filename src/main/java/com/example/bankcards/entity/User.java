package com.example.bankcards.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "password_encrypted", nullable = false)
    private String passwordEncrypted;

    @Column(name = "entry_date", nullable = false)
    private Instant entryDate;

    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
    @JsonProperty("cards")
    private Set<Card> cards = new HashSet<>();

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", role=" + role +
                ", passwordEncrypted='" + passwordEncrypted + '\'' +
                ", entryDate=" + entryDate +
                ", cards=" + cards +
                '}';
    }

    public enum Role {
        USER,
        ADMIN
    }
}
