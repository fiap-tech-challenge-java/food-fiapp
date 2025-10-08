package com.fiap.foodfiapp.core.application.usecases.user;

import com.fiap.foodfiapp.core.domain.entity.User;

import java.util.List;

public interface FindAllUserUseCase {
    List<User> execute();
}
