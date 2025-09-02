package com.example.bankcards.dto;

import com.example.bankcards.entity.UserRequest;
import lombok.Data;

import java.time.Instant;

@Data
public class UserRequestAdditionalDTO {
    private Long id;
    private UserDTO initiator;
    private CardDTO card;
    private Instant entryDate;
    private Instant resultDate;
    private UserRequest.Type type;
    private UserRequest.Result result;
    private String comment;
    private UserDTO executor;
    private String resultComment;
}
