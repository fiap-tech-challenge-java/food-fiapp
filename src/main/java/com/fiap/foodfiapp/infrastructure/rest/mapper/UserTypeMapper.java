package com.fiap.foodfiapp.infrastructure.rest.mapper;

import com.fiap.foodfiapp.core.domain.entity.UserType;
import com.fiap.foodfiapp.model.CreateUserTypeRequest;
import com.fiap.foodfiapp.model.UpdateUserTypeRequest;
import com.fiap.foodfiapp.model.UserTypeResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface UserTypeMapper {

    UserTypeMapper INSTANCE = Mappers.getMapper(UserTypeMapper.class);

    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    UserType toUserType(CreateUserTypeRequest createUserTypeRequest);

    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    UserType toUserType(UpdateUserTypeRequest updateUserTypeRequest);

    @Mapping(source = "active", target = "isActive")
    UserTypeResponse toUserTypeResponse(UserType userType);

    List<UserTypeResponse> toUserTypeResponseList(List<UserType> userTypes);
}