package com.example.bankcards.util;

import com.example.bankcards.dto.UserRequestAdditionalDTO;
import com.example.bankcards.dto.UserRequestDTO;
import com.example.bankcards.entity.UserRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {UserMapper.class, CardMapper.class})
public interface UserRequestMapper {

    @Named("mapToUserRequestDTO")
    @Mapping(target = "initiatorId", expression = "java(userRequest.getInitiator().getId())")
    @Mapping(target = "cardId", expression = "java(userRequest.getCard().getId())")
    @Mapping(target = "executorId", expression = "java(userRequest.getExecutor().getId())")
    UserRequestDTO mapToUserRequestDTO(UserRequest userRequest);

    @Named(("mapToUserRequestAdditionalDTO"))
    @Mapping(target = "initiator", expression = "java(UserMapper.INSTANCE.mapToUserDTO(userRequest.getInitiator()))")
    @Mapping(target = "card", expression = "java(CardMapper.INSTANCE.mapToCardDTO(userRequest.getCard()))")
    @Mapping(target = "executor", expression = "java(UserMapper.INSTANCE.mapToUserDTO(userRequest.getExecutor()))")
    UserRequestAdditionalDTO mapToUserRequestAdditionalDTO(UserRequest userRequest);
}
