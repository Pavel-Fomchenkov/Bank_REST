package com.example.bankcards.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "user_requests")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User initiator;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id", nullable = false)
    private Card card;

    @Column(name = "entry_date", nullable = false)
    private Instant entryDate;

    @Setter
    @Column(name = "result_date", nullable = true)
    private Instant resultDate;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private Type type;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "result", nullable = true)
    private Result result;

    @Setter
    @Column(name = "comments", nullable = false)
    private String comment;

    @Setter
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "executor_id", nullable = true)
    private User executor;

    @Setter
    @Column(name = "result_comments", nullable = true)
    private String resultComment;

    public enum Type {
        BLOCK,
        UNBLOCK
    }

    public enum Result {
        RECEIVED,
        INCOMPLETED,
        SUSPENDED,
        COMPLETED,
        REJECTED
    }
}