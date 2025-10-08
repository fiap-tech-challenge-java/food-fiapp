package com.fiap.foodfiapp.core.application.usecases.user;

import com.fiap.foodfiapp.core.domain.entity.User;

public interface FindUserByEmailUseCase {
    User execute(String email);
}
