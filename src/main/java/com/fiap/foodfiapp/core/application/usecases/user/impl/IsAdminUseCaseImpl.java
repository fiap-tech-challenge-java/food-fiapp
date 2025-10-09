package com.fiap.foodfiapp.core.application.usecases.user.impl;

import com.fiap.foodfiapp.core.application.usecases.user.IsAdminUseCase;
import com.fiap.foodfiapp.core.domain.entity.User;
import com.fiap.foodfiapp.core.domain.port.UserRepository;

import java.util.UUID;

public class IsAdminUseCaseImpl implements IsAdminUseCase {
    private final UserRepository userRepository;

    public IsAdminUseCaseImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean execute(UUID userId) {
        return userRepository.findById(userId)
                .map(user -> {
                    String userTypeName = user.getUserType() != null ? user.getUserType().getName() : null;
                    return "ADMIN".equalsIgnoreCase(userTypeName);
                })
                .orElse(false);
    }
}
