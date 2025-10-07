package com.fiap.foodfiapp.core.application.usecases.user;

import com.fiap.foodfiapp.core.domain.entity.User;

public interface CreateUserUseCase {
    User execute(User user);
}