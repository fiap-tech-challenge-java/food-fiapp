package com.fiap.foodfiapp.core.application.usecases.usertype;

import com.fiap.foodfiapp.core.domain.entity.UserType;

public interface DeleteUserTypeUseCase {
    UserType execute(UserType userType);
}