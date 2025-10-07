package com.fiap.foodfiapp.core.application.usecases.usertype;

import com.fiap.foodfiapp.core.domain.entity.UserType;

public interface CreateUserTypeUseCase {
    UserType execute(UserType userType);
}