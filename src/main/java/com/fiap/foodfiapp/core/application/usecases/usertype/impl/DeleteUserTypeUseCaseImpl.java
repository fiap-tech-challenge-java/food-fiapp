package com.fiap.foodfiapp.core.application.usecases.usertype.impl;

import com.fiap.foodfiapp.core.domain.port.UserRepository;
import com.fiap.foodfiapp.core.domain.port.UserTypeRepository;
import com.fiap.foodfiapp.core.application.usecases.usertype.DeleteUserTypeUseCase;
import com.fiap.foodfiapp.core.domain.entity.UserType;
import com.fiap.foodfiapp.core.domain.exception.CoreUserTypeModificationException;
import com.fiap.foodfiapp.core.domain.exception.UserTypeInUseException;
import com.fiap.foodfiapp.core.domain.exception.UserTypeNotFoundException;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class DeleteUserTypeUseCaseImpl implements DeleteUserTypeUseCase {
    private static final List<String> CORE_USER_TYPES = Arrays.asList("OWNER", "CUSTOMER", "ADMIN");
    private final UserTypeRepository userTypeRepository;
    private final UserRepository userRepository;

    public DeleteUserTypeUseCaseImpl(UserTypeRepository userTypeRepository, UserRepository userRepository) {
        this.userTypeRepository = userTypeRepository;
        this.userRepository = userRepository;
    }

    // Assinatura corrigida para receber apenas o UUID
    @Override
    public void execute(UUID uuid) {
        UserType userType = userTypeRepository.findById(uuid)
                .orElseThrow(() -> new UserTypeNotFoundException("User type not found."));

        if (CORE_USER_TYPES.contains(userType.getName().toUpperCase())) {
            throw new CoreUserTypeModificationException();
        }

        if (userRepository.existsByUserTypeUuid(uuid)) {
            throw new UserTypeInUseException();
        }

        userTypeRepository.deleteById(uuid);
    }
}