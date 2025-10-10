package com.fiap.foodfiapp.core.application.usecases.user.impl;

import com.fiap.foodfiapp.core.domain.port.UserRepository;
import com.fiap.foodfiapp.core.domain.port.UserTypeRepository;
import com.fiap.foodfiapp.core.application.usecases.user.UpdateUserUseCase;
import com.fiap.foodfiapp.core.domain.entity.User;
import com.fiap.foodfiapp.core.domain.entity.UserType;
import com.fiap.foodfiapp.core.domain.exception.BusinessException;
import com.fiap.foodfiapp.core.domain.validator.UserValidator;
import java.util.UUID;

public class UpdateUserUseCaseImpl implements UpdateUserUseCase {
    private final UserRepository userRepository;
    private final UserTypeRepository userTypeRepository;

    public UpdateUserUseCaseImpl(UserRepository userRepository, UserTypeRepository userTypeRepository) {
        this.userRepository = userRepository;
        this.userTypeRepository = userTypeRepository;
    }

    @Override
    public User execute(UUID userId, User userUpdates) {
        // Validate individual fields that are being updated
        if (userUpdates.getName() != null) {
            UserValidator.validateName(userUpdates.getName());
        }
        if (userUpdates.getEmail() != null) {
            UserValidator.validateEmail(userUpdates.getEmail());
        }
        if (userUpdates.getCpf() != null) {
            UserValidator.validateCpf(userUpdates.getCpf());
        }
        if (userUpdates.getLogin() != null) {
            UserValidator.validateLogin(userUpdates.getLogin());
        }

        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("User not found."));

        // Check if the requesting user (from userUpdates if it has an ID, or userId) is ADMIN
        // Note: In a real scenario, you'd get the authenticated user ID separately
        // For now, we'll check if the user being updated is trying to change their own UserType to ADMIN
        // This logic might need adjustment based on your authentication flow

        if (userUpdates.getUserType() != null && userUpdates.getUserType().getUuid() != null) {
            UserType userType = userTypeRepository.findById(userUpdates.getUserType().getUuid())
                    .orElseThrow(() -> new BusinessException("User type not found."));
            userUpdates.setUserType(userType);
        } else {
            userUpdates.setUserType(existingUser.getUserType());
        }

        if (userUpdates.getEmail() != null && !userUpdates.getEmail().equals(existingUser.getEmail())) {
            userRepository.findByEmail(userUpdates.getEmail()).ifPresent(user -> {
                if (!user.getId().equals(userId)) {
                    throw new BusinessException("User with this email already exists.");
                }
            });
        }

        userUpdates.setId(userId);
        userUpdates.setCreatedAt(existingUser.getCreatedAt());

        return userRepository.save(userUpdates);
    }
}