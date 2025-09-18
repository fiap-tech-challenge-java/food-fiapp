package com.fiap.foodfiapp.core.application.usecases.user;

import com.fiap.foodfiapp.core.application.gateways.UserRepositoryGateway;
import com.fiap.foodfiapp.core.application.gateways.UserTypeRepositoryGateway;
import com.fiap.foodfiapp.core.domain.entity.User;
import com.fiap.foodfiapp.core.domain.entity.UserType;
import com.fiap.foodfiapp.core.domain.exception.BusinessException;

import java.util.UUID;

public class UpdateUserUseCase {
    private final UserRepositoryGateway userRepositoryGateway;
    private final UserTypeRepositoryGateway userTypeRepositoryGateway;

    public UpdateUserUseCase(UserRepositoryGateway userRepositoryGateway, UserTypeRepositoryGateway userTypeRepositoryGateway) {
        this.userRepositoryGateway = userRepositoryGateway;
        this.userTypeRepositoryGateway = userTypeRepositoryGateway;
    }

    public User execute(UUID userId, User userUpdates) {
        // Verificar se o usuário existe
        User existingUser = userRepositoryGateway.findById(userId)
                .orElseThrow(() -> new BusinessException("User not found."));

        // Validar e buscar UserType se fornecido
        if (userUpdates.getUserType() != null && userUpdates.getUserType().getUuid() != null) {
            UserType userType = userTypeRepositoryGateway.findById(userUpdates.getUserType().getUuid())
                    .orElseThrow(() -> new BusinessException("User type not found."));
            userUpdates.setUserType(userType);
        } else {
            // Se não fornecido, manter o userType existente
            userUpdates.setUserType(existingUser.getUserType());
        }

        // Verificar se email já existe para outro usuário
        if (userUpdates.getEmail() != null && !userUpdates.getEmail().equals(existingUser.getEmail())) {
            userRepositoryGateway.findByEmail(userUpdates.getEmail()).ifPresent(user -> {
                if (!user.getId().equals(userId)) {
                    throw new BusinessException("User with this email already exists.");
                }
            });
        }

        // Manter o ID do usuário existente
        userUpdates.setId(userId);

        // Preservar campos que não devem ser alterados via update
        userUpdates.setCreatedAt(existingUser.getCreatedAt());

        return userRepositoryGateway.save(userUpdates);
    }
}
