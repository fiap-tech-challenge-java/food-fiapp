package com.fiap.foodfiapp.core.application.usecases.user;

import com.fiap.foodfiapp.core.application.gateways.UserTypeRepositoryGateway;
import com.fiap.foodfiapp.core.domain.entity.User;
import com.fiap.foodfiapp.core.domain.entity.UserType;
import com.fiap.foodfiapp.core.domain.exception.BusinessException;
import com.fiap.foodfiapp.core.application.gateways.UserRepositoryGateway;


public class CreateUserUseCase {
    private final UserRepositoryGateway userRepositoryGateway;
    private final UserTypeRepositoryGateway userTypeRepositoryGateway;

    public CreateUserUseCase(UserRepositoryGateway userRepositoryGateway, UserTypeRepositoryGateway userTypeRepositoryGateway) {
        this.userTypeRepositoryGateway = userTypeRepositoryGateway;
        this.userRepositoryGateway = userRepositoryGateway;
    }

    public User execute(User user) {
        userRepositoryGateway.findByEmail(user.getEmail()).ifPresent(existingUser -> {
            throw new BusinessException("User with this email already exists.");
        });

        if (user.getUserType() == null || user.getUserType().getUuid() == null) {
            throw new BusinessException("User type is required.");
        }
        UserType userType = userTypeRepositoryGateway.findById(user.getUserType().getUuid()).orElseThrow(() -> new BusinessException("User type not found."));
        user.setUserType(userType);
        return userRepositoryGateway.save(user);
    }

}
