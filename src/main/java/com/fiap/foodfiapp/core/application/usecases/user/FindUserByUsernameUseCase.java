package com.fiap.foodfiapp.core.application.usecases.user;

import com.fiap.foodfiapp.core.domain.entity.User;

public interface FindUserByUsernameUseCase {
    User execute(String username);
}
