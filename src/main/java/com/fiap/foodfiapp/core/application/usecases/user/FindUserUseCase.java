package com.fiap.foodfiapp.core.application.usecases.user;

import com.fiap.foodfiapp.core.domain.entity.User;
import java.util.Optional;
import java.util.UUID;

public interface FindUserUseCase {
    Optional<User> execute(UUID id);
}
