package com.fiap.foodfiapp.core.application.usecases.usertype;

import com.fiap.foodfiapp.core.application.gateways.UserTypeRepositoryGateway;
import com.fiap.foodfiapp.core.domain.entity.UserType;
import com.fiap.foodfiapp.core.domain.exception.BusinessException;

import java.util.UUID;

public class UpdateUserTypeUseCase {
    private final UserTypeRepositoryGateway userTypeRepositoryGateway;

    public UpdateUserTypeUseCase(UserTypeRepositoryGateway userTypeRepositoryGateway) {
        this.userTypeRepositoryGateway = userTypeRepositoryGateway;
    }

    public UserType execute(UUID uuid, UserType userTypeUpdates) {
        // Verificar se o UserType existe
        UserType existingUserType = userTypeRepositoryGateway.findById(uuid)
                .orElseThrow(() -> new BusinessException("User type not found."));

        // Verificar se o nome já existe para outro UserType
        if (userTypeUpdates.getName() != null && !userTypeUpdates.getName().equals(existingUserType.getName())) {
            userTypeRepositoryGateway.findByName(userTypeUpdates.getName()).ifPresent(userType -> {
                if (!userType.getUuid().equals(uuid)) {
                    throw new BusinessException("User type with this name already exists.");
                }
            });
        }

        // Atualizar campos
        userTypeUpdates.setUuid(uuid);
        if (userTypeUpdates.getName() == null) {
            userTypeUpdates.setName(existingUserType.getName());
        }

        return userTypeRepositoryGateway.save(userTypeUpdates);
    }
}
