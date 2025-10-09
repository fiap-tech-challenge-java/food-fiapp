package com.fiap.foodfiapp.core.application.usecases.user.impl;

import com.fiap.foodfiapp.core.application.usecases.user.ValidateAdminOrOwnerUseCase;
import com.fiap.foodfiapp.core.domain.entity.User;
import com.fiap.foodfiapp.core.domain.exception.UserNotFoundException;
import com.fiap.foodfiapp.core.domain.port.UserRepository;

import java.util.UUID;

public class ValidateAdminOrOwnerUseCaseImpl implements ValidateAdminOrOwnerUseCase {
    private final UserRepository userRepository;

    public ValidateAdminOrOwnerUseCaseImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean execute(UUID userId, UUID resourceOwnerId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("id", userId.toString()));

        // Check if user is ADMIN
        String userTypeName = user.getUserType() != null ? user.getUserType().getName() : null;
        boolean isAdmin = "ADMIN".equalsIgnoreCase(userTypeName);

        if (isAdmin) {
            return true; // ADMIN can access everything
        }

        // If not ADMIN, check if user is the owner
        if (resourceOwnerId != null) {
            return userId.equals(resourceOwnerId);
        }

        return false;
    }
}

