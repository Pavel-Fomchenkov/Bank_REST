package com.example.bankcards.dto;

import com.example.bankcards.entity.UserRequest;
import lombok.Data;

import java.time.Instant;

@Data
public class UserRequestDTO {
    private Long id;
    private Long initiatorId;
    private CardIdOwnerIdDTO cardIdOwnerIdDTO;
    private Instant entryDate;
    private Instant resultDate;
    private UserRequest.Type type;
    private UserRequest.Result result;
    private String comment;
    private Long executorId;
    private String resultComment;
}
