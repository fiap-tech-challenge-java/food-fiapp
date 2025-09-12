package com.fiap.foodfiapp.core.application.usecases.usertype;

import com.fiap.foodfiapp.core.application.gateways.UserTypeRepositoryGateway;
import com.fiap.foodfiapp.core.domain.entity.UserType;
import com.fiap.foodfiapp.core.domain.exception.BusinessException;

public class CreateUserTypeUseCase {
    private final UserTypeRepositoryGateway userTypeRepositoryGateway;

    public CreateUserTypeUseCase(UserTypeRepositoryGateway userTypeRepositoryGateway) {
        this.userTypeRepositoryGateway = userTypeRepositoryGateway;
    }

    public UserType execute(UserType userType) {
        // Validar se o nome é único
        userTypeRepositoryGateway.findByName(userType.getName()).ifPresent(existingUserType -> {
            throw new BusinessException("User type with this name already exists.");
        });

        // Não definir UUID aqui - deixar o Hibernate gerar automaticamente
        // O UUID será gerado pelo banco de dados conforme configurado na entidade

        return userTypeRepositoryGateway.save(userType);
    }
}
