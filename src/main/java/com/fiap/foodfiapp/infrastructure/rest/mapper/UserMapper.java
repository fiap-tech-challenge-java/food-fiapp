package com.fiap.foodfiapp.infrastructure.rest.mapper;

import com.fiap.foodfiapp.core.domain.entity.User;
import com.fiap.foodfiapp.core.domain.entity.UserType;
import com.fiap.foodfiapp.model.CreateUserRequest;
import com.fiap.foodfiapp.model.UpdateUserRequest;
import com.fiap.foodfiapp.model.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.UUID;

@Mapper(uses = {AddressesMapper.class})
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "userTypeUuid", target = "userType", qualifiedByName = "uuidToUserType")
    @Mapping(source = "addresses", target = "address")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "isActive", constant = "true")
    User toUser(CreateUserRequest createUserRequest);

    @Mapping(source = "userTypeUuid", target = "userType", qualifiedByName = "uuidToUserType")
    @Mapping(source = "addresses", target = "address")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    User toUser(UpdateUserRequest updateUserRequest);

    @Mapping(source = "userType", target = "userType", qualifiedByName = "userTypeToName")
    @Mapping(source = "address", target = "addresses")
    UserResponse toUserResponse(User user);

    List<UserResponse> toUserResponseList(List<User> users);

    @Named("uuidToUserType")
    default UserType uuidToUserType(UUID uuid) {
        if (uuid == null) {
            return null;
        }
        UserType userType = new UserType();
        userType.setUuid(uuid);
        return userType;
    }

    @Named("userTypeToName")
    default String userTypeToName(UserType userType) {
        if (userType == null || userType.getName() == null) {
            return null;
        }
        return userType.getName();
    }
}