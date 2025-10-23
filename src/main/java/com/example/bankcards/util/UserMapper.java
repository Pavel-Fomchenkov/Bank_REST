package com.example.bankcards.util;

import com.example.bankcards.dto.ChangePasswordDTO;
import com.example.bankcards.dto.UserDTO;
import com.example.bankcards.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Named("mapToUserDTO")
    UserDTO mapToUserDTO(User user);
}

