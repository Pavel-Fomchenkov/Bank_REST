package com.example.bankcards.util;

import com.example.bankcards.dto.CardDTO;
import com.example.bankcards.dto.UserDTO;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CardMapper {
    String MASK = "**** **** **** ";
    CardMapper INSTANCE = Mappers.getMapper(CardMapper.class);

    @Named("mapToCardDTO")
    @Mapping(target = "owner", expression = "java(UserMapper.INSTANCE.mapToUserDTO(card.getOwner()))")
    @Mapping(target = "numberMasked", expression = "java(MASK + card.getNumberMasked())")
    @Mapping(target = "creditLimit", expression = "java(card.getCreditLimit().toString())")
    @Mapping(target = "balance", expression = "java(card.getBalance().toString())")
    CardDTO mapToCardDTO(Card card);
}
