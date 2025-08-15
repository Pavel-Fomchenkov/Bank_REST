package com.example.bankcards.util;

import com.example.bankcards.dto.CardDTO;
import com.example.bankcards.entity.Card;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class CardMapper {

    @Autowired
    protected UserMapper userMapper;
    protected static final String MASK = "**** **** **** ";

    @Named("mapToCardDTO")
    @Mapping(target = "owner", expression = "java(userMapper.mapToUserDTO(card.getOwner()))")
    @Mapping(target = "numberMasked", expression = "java(MASK + card.getNumberMasked())")
    public abstract CardDTO mapToCardDTO(Card card);
}
