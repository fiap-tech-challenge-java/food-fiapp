package com.fiap.foodfiapp.core.domain.port;

import com.fiap.foodfiapp.core.domain.entity.User;
import com.fiap.foodfiapp.core.domain.entity.UserType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserTypeRepository {
    UserType save(UserType userType);
    Optional<UserType> findById(UUID id);
    Optional<UserType> findByName(String name);
    List<UserType> findAll();
    void deleteById(UUID id);
}
