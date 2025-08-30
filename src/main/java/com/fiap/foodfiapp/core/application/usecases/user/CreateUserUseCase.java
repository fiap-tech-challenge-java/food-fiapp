package com.fiap.foodfiapp.core.application.usecases.user;

import com.fiap.foodfiapp.core.domain.entities.CreateUser;
import com.fiap.foodfiapp.core.domain.entities.User;
import com.fiap.foodfiapp.core.domain.exception.BusinessException;
import com.fiap.foodfiapp.core.application.gateways.UserRepositoryGateway;
import org.springframework.stereotype.Service;

@Service
public class CreateUserUseCase {
    private final UserRepositoryGateway userRepositoryGateway;

    public CreateUserUseCase(UserRepositoryGateway userRepositoryGateway) {
        this.userRepositoryGateway = userRepositoryGateway;
    }

    public User execute(CreateUser user) {
        userRepositoryGateway.findByEmail(user.getEmail()).ifPresent(existingUser -> {
            throw new BusinessException("User with this email already exists.");
        });

        return userRepositoryGateway.save(user);
    }
}
