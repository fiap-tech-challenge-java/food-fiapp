package com.fiap.foodfiapp.application.usecases.user;

import com.fiap.foodfiapp.domain.entity.User;
import com.fiap.foodfiapp.domain.exception.BusinessException;
import com.fiap.foodfiapp.application.gateways.UserRepositoryGateway;

public class CreateUserUseCase {
    private final UserRepositoryGateway userRepositoryGateway;

    public CreateUserUseCase(UserRepositoryGateway userRepositoryGateway) {
        this.userRepositoryGateway = userRepositoryGateway;
    }

    public User execute(User user) {
        userRepositoryGateway.findByEmail(user.getEmail()).ifPresent(existingUser -> {
            throw new BusinessException("User with this email already exists.");
        });
        return userRepositoryGateway.save(user);
    }
}
