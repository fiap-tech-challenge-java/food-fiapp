package com.fiap.foodfiapp.core.application.usecases.usertype;

import com.fiap.foodfiapp.core.application.gateways.UserTypeRepositoryGateway;
import com.fiap.foodfiapp.core.domain.entity.UserType;
import com.fiap.foodfiapp.core.domain.exception.BusinessException;
import com.fiap.foodfiapp.core.domain.exception.UserTypeNameAlreadyExistsException;

import java.util.UUID;

public class CreateUserTypeUseCase {
    private final UserTypeRepositoryGateway userTypeRepositoryGateway;

    public CreateUserTypeUseCase(UserTypeRepositoryGateway userTypeRepositoryGateway) {
        this.userTypeRepositoryGateway = userTypeRepositoryGateway;
    }

    public UserType execute(UserType userType) {
        // Validate that the name is unique
        userTypeRepositoryGateway.findByName(userType.getName()).ifPresent(existingUserType -> {
            throw new UserTypeNameAlreadyExistsException(userType.getName());
        });

        // Generate UUID if not already set
        if (userType.getUuid() == null) {
            userType.setUuid(UUID.randomUUID());
        } else {
            // If UUID is provided by client, ensure it doesn't conflict
            userTypeRepositoryGateway.findById(userType.getUuid()).ifPresent(existing -> {
                throw new BusinessException("UserType with UUID already exists");
            });
        }

        return userTypeRepositoryGateway.save(userType);
    }
}
