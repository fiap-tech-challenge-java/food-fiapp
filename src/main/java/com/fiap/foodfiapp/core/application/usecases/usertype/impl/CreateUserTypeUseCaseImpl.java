package com.fiap.foodfiapp.core.application.usecases.usertype.impl;

import com.fiap.foodfiapp.core.application.gateways.UserTypeRepositoryGateway;
import com.fiap.foodfiapp.core.application.usecases.usertype.CreateUserTypeUseCase;
import com.fiap.foodfiapp.core.domain.entity.UserType;
import com.fiap.foodfiapp.core.domain.exception.BusinessException;
import com.fiap.foodfiapp.core.domain.exception.UserTypeNameAlreadyExistsException;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class CreateUserTypeUseCaseImpl implements CreateUserTypeUseCase {
    private final UserTypeRepositoryGateway userTypeRepositoryGateway;

    public CreateUserTypeUseCaseImpl(UserTypeRepositoryGateway userTypeRepositoryGateway) {
        this.userTypeRepositoryGateway = userTypeRepositoryGateway;
    }

    @Override
    public UserType execute(UserType userType) {
        userTypeRepositoryGateway.findByName(userType.getName()).ifPresent(existingUserType -> {
            throw new UserTypeNameAlreadyExistsException(userType.getName());
        });

        if (userType.getUuid() == null) {
            userType.setUuid(UUID.randomUUID());
        } else {
            userTypeRepositoryGateway.findById(userType.getUuid()).ifPresent(existing -> {
                throw new BusinessException("UserType with UUID already exists");
            });
        }

        return userTypeRepositoryGateway.save(userType);
    }
}