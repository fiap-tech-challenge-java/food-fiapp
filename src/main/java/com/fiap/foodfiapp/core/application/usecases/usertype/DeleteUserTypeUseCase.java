package com.fiap.foodfiapp.core.application.usecases.usertype;

import com.fiap.foodfiapp.core.application.gateways.UserRepositoryGateway;
import com.fiap.foodfiapp.core.application.gateways.UserTypeRepositoryGateway;
import com.fiap.foodfiapp.core.domain.entity.UserType;
import com.fiap.foodfiapp.core.domain.exception.CoreUserTypeModificationException;
import com.fiap.foodfiapp.core.domain.exception.UserTypeInUseException;
import com.fiap.foodfiapp.core.domain.exception.UserTypeNotFoundException;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class DeleteUserTypeUseCase {
    private static final List<String> CORE_USER_TYPES = Arrays.asList("OWNER", "CUSTOMER", "ADMIN");
    private final UserTypeRepositoryGateway userTypeRepositoryGateway;
    private final UserRepositoryGateway userRepositoryGateway;

    public DeleteUserTypeUseCase(UserTypeRepositoryGateway userTypeRepositoryGateway, UserRepositoryGateway userRepositoryGateway) {
        this.userTypeRepositoryGateway = userTypeRepositoryGateway;
        this.userRepositoryGateway = userRepositoryGateway;
    }

    public void execute(UUID uuid) {
        UserType userType = userTypeRepositoryGateway.findById(uuid)
                .orElseThrow(() -> new UserTypeNotFoundException("User type not found."));

        if (CORE_USER_TYPES.contains(userType.getName().toUpperCase())) {
            throw new CoreUserTypeModificationException();
        }

        // Verificar se há usuários usando este UserType
        if (userRepositoryGateway.existsByUserTypeUuid(uuid)) {
            throw new UserTypeInUseException();
        }

        userTypeRepositoryGateway.deleteById(uuid);
    }
}
