package com.fiap.foodfiapp.core.application.usecases.user;

import com.fiap.foodfiapp.core.domain.entity.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FindUserUseCase {
    List<User> findAll();
    User findUserByEmail(String email);
    User findUserByUsername(String username);
    Optional<User> findById(UUID id);
}
