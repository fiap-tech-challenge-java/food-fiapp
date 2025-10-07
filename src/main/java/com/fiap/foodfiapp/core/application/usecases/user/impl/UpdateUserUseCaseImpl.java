package com.fiap.foodfiapp.core.application.usecases.user.impl;

import com.fiap.foodfiapp.core.application.gateways.UserRepositoryGateway;
import com.fiap.foodfiapp.core.application.gateways.UserTypeRepositoryGateway;
import com.fiap.foodfiapp.core.application.usecases.user.UpdateUserUseCase;
import com.fiap.foodfiapp.core.domain.entity.User;
import com.fiap.foodfiapp.core.domain.entity.UserType;
import com.fiap.foodfiapp.core.domain.exception.BusinessException;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class UpdateUserUseCaseImpl implements UpdateUserUseCase {
    private final UserRepositoryGateway userRepositoryGateway;
    private final UserTypeRepositoryGateway userTypeRepositoryGateway;

    public UpdateUserUseCaseImpl(UserRepositoryGateway userRepositoryGateway, UserTypeRepositoryGateway userTypeRepositoryGateway) {
        this.userRepositoryGateway = userRepositoryGateway;
        this.userTypeRepositoryGateway = userTypeRepositoryGateway;
    }

    @Override
    public User execute(UUID userId, User userUpdates) {
        User existingUser = userRepositoryGateway.findById(userId)
                .orElseThrow(() -> new BusinessException("User not found."));

        if (userUpdates.getUserType() != null && userUpdates.getUserType().getUuid() != null) {
            UserType userType = userTypeRepositoryGateway.findById(userUpdates.getUserType().getUuid())
                    .orElseThrow(() -> new BusinessException("User type not found."));
            userUpdates.setUserType(userType);
        } else {
            userUpdates.setUserType(existingUser.getUserType());
        }

        if (userUpdates.getEmail() != null && !userUpdates.getEmail().equals(existingUser.getEmail())) {
            userRepositoryGateway.findByEmail(userUpdates.getEmail()).ifPresent(user -> {
                if (!user.getId().equals(userId)) {
                    throw new BusinessException("User with this email already exists.");
                }
            });
        }

        userUpdates.setId(userId);
        userUpdates.setCreatedAt(existingUser.getCreatedAt());

        return userRepositoryGateway.save(userUpdates);
    }
}