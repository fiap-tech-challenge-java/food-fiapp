package com.fiap.foodfiapp.infrastructure.rest.mapper;

import com.fiap.foodfiapp.core.domain.entities.CreateUser;
import com.fiap.foodfiapp.core.domain.entities.User;
import com.fiap.foodfiapp.infrastructure.rest.dto.user.UserRequestDTO;
import com.fiap.foodfiapp.infrastructure.rest.dto.user.UserResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    CreateUser mapToCreateUser(UserRequestDTO dto);
    UserRequestDTO mapToUserRequestDTO(User user);

    UserResponseDTO mapToUserResponseDTO(User dto);
    List<UserResponseDTO> mapToUserResponseListDTO(List<User> users);
}

