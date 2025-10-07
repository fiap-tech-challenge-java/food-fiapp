package com.fiap.foodfiapp.core.application.usecases.usertype.impl;

import com.fiap.foodfiapp.core.application.gateways.UserRepositoryGateway;
import com.fiap.foodfiapp.core.application.gateways.UserTypeRepositoryGateway;
import com.fiap.foodfiapp.core.application.usecases.usertype.UpdateUserTypeUseCase;
import com.fiap.foodfiapp.core.domain.entity.UserType;
import com.fiap.foodfiapp.core.domain.exception.CoreUserTypeModificationException;
import com.fiap.foodfiapp.core.domain.exception.UserTypeInUseException;
import com.fiap.foodfiapp.core.domain.exception.UserTypeNameAlreadyExistsException;
import com.fiap.foodfiapp.core.domain.exception.UserTypeNotFoundException;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class UpdateUserTypeUseCaseImpl implements UpdateUserTypeUseCase {
    private static final List<String> CORE_USER_TYPES = Arrays.asList("OWNER", "CUSTOMER", "ADMIN");
    private final UserTypeRepositoryGateway userTypeRepositoryGateway;
    private final UserRepositoryGateway userRepositoryGateway;

    public UpdateUserTypeUseCaseImpl(UserTypeRepositoryGateway userTypeRepositoryGateway, UserRepositoryGateway userRepositoryGateway) {
        this.userTypeRepositoryGateway = userTypeRepositoryGateway;
        this.userRepositoryGateway = userRepositoryGateway;
    }

    public UserType execute(UUID uuid, UserType userTypeUpdates) {
        UserType existingUserType = userTypeRepositoryGateway.findById(uuid)
                .orElseThrow(() -> new UserTypeNotFoundException("User type not found."));

        if (CORE_USER_TYPES.contains(existingUserType.getName().toUpperCase())) {
            throw new CoreUserTypeModificationException();
        }

        // Verificar se o UserType está em uso
        if (userRepositoryGateway.existsByUserTypeUuid(uuid)) {
            throw new UserTypeInUseException();
        }

        // Verificar se o nome já existe para outro UserType
        if (userTypeUpdates.getName() != null && !userTypeUpdates.getName().equals(existingUserType.getName())) {
            userTypeRepositoryGateway.findByName(userTypeUpdates.getName()).ifPresent(userType -> {
                if (!userType.getUuid().equals(uuid)) {
                    throw new UserTypeNameAlreadyExistsException(userTypeUpdates.getName());
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
