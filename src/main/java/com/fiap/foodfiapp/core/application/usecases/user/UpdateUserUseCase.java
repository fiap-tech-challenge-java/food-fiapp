package com.fiap.foodfiapp.core.application.usecases.user;

import com.fiap.foodfiapp.core.domain.entity.User;
import java.util.UUID;

public interface UpdateUserUseCase {
    User execute(UUID userId, User userUpdates);
}