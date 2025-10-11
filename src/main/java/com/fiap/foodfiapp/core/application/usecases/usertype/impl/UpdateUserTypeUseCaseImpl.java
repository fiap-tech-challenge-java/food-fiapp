package com.fiap.foodfiapp.core.application.usecases.usertype.impl;

import com.fiap.foodfiapp.core.domain.port.UserRepository;
import com.fiap.foodfiapp.core.domain.port.UserTypeRepository;
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
    private final UserTypeRepository userTypeRepository;
    private final UserRepository userRepository;

    public UpdateUserTypeUseCaseImpl(UserTypeRepository userTypeRepository, UserRepository userRepository) {
        this.userTypeRepository = userTypeRepository;
        this.userRepository = userRepository;
    }

    public UserType execute(UUID uuid, UserType userTypeUpdates) {
        UserType existingUserType = userTypeRepository.findById(uuid)
                .orElseThrow(() -> new UserTypeNotFoundException("User type not found."));

        if (CORE_USER_TYPES.contains(existingUserType.getName().toUpperCase())) {
            throw new CoreUserTypeModificationException();
        }

        if (userRepository.existsByUserTypeUuid(uuid)) {
            throw new UserTypeInUseException();
        }

        if (userTypeUpdates.getName() != null && !userTypeUpdates.getName().equals(existingUserType.getName())) {
            userTypeRepository.findByName(userTypeUpdates.getName()).ifPresent(userType -> {
                if (!userType.getUuid().equals(uuid)) {
                    throw new UserTypeNameAlreadyExistsException(userTypeUpdates.getName());
                }
            });
        }

        userTypeUpdates.setUuid(uuid);
        if (userTypeUpdates.getName() == null) {
            userTypeUpdates.setName(existingUserType.getName());
        }

        return userTypeRepository.save(userTypeUpdates);
    }
}
