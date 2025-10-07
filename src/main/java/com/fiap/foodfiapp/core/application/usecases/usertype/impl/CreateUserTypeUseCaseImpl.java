package com.fiap.foodfiapp.core.application.usecases.usertype.impl;

import com.fiap.foodfiapp.core.domain.port.UserTypeRepository;
import com.fiap.foodfiapp.core.application.usecases.usertype.CreateUserTypeUseCase;
import com.fiap.foodfiapp.core.domain.entity.UserType;
import com.fiap.foodfiapp.core.domain.exception.BusinessException;
import com.fiap.foodfiapp.core.domain.exception.UserTypeNameAlreadyExistsException;
import java.util.UUID;

public class CreateUserTypeUseCaseImpl implements CreateUserTypeUseCase {
    private final UserTypeRepository userTypeRepository;

    public CreateUserTypeUseCaseImpl(UserTypeRepository userTypeRepository) {
        this.userTypeRepository = userTypeRepository;
    }

    @Override
    public UserType execute(UserType userType) {
        userTypeRepository.findByName(userType.getName()).ifPresent(existingUserType -> {
            throw new UserTypeNameAlreadyExistsException(userType.getName());
        });

        if (userType.getUuid() == null) {
            userType.setUuid(UUID.randomUUID());
        } else {
            userTypeRepository.findById(userType.getUuid()).ifPresent(existing -> {
                throw new BusinessException("UserType with UUID already exists");
            });
        }

        // Garantir que novos UserTypes sejam criados como ativos por padrão
        userType.setActive(true);

        return userTypeRepository.save(userType);
    }
}