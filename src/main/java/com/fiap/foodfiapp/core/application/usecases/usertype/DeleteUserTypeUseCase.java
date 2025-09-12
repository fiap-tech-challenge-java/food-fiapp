package com.fiap.foodfiapp.core.application.usecases.usertype;

import com.fiap.foodfiapp.core.application.gateways.UserRepositoryGateway;
import com.fiap.foodfiapp.core.application.gateways.UserTypeRepositoryGateway;
import com.fiap.foodfiapp.core.domain.exception.BusinessException;

import java.util.UUID;

public class DeleteUserTypeUseCase {
    private final UserTypeRepositoryGateway userTypeRepositoryGateway;
    private final UserRepositoryGateway userRepositoryGateway;

    public DeleteUserTypeUseCase(UserTypeRepositoryGateway userTypeRepositoryGateway, UserRepositoryGateway userRepositoryGateway) {
        this.userTypeRepositoryGateway = userTypeRepositoryGateway;
        this.userRepositoryGateway = userRepositoryGateway;
    }

    public void execute(UUID uuid) {
        // Verificar se o UserType existe
        userTypeRepositoryGateway.findById(uuid)
                .orElseThrow(() -> new BusinessException("User type not found."));

        // Verificar se há usuários usando este UserType
        if (userRepositoryGateway.existsByUserTypeUuid(uuid)) {
            throw new BusinessException("Cannot delete user type that is being used by users.");
        }

        userTypeRepositoryGateway.deleteById(uuid);
    }
}
