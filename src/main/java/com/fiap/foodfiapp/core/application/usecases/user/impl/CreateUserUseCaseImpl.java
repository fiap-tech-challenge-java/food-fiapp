package com.fiap.foodfiapp.core.application.usecases.user.impl;

import com.fiap.foodfiapp.core.application.gateways.UserRepositoryGateway;
import com.fiap.foodfiapp.core.application.gateways.UserTypeRepositoryGateway;
import com.fiap.foodfiapp.core.application.usecases.user.CreateUserUseCase;
import com.fiap.foodfiapp.core.domain.entity.User;
import com.fiap.foodfiapp.core.domain.entity.UserType;
import com.fiap.foodfiapp.core.domain.exception.BusinessException;
import com.fiap.foodfiapp.core.domain.exception.CpfAlreadyExistsException;
import com.fiap.foodfiapp.core.domain.exception.LoginAlreadyExistsException;
import com.fiap.foodfiapp.core.domain.exception.UserTypeNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CreateUserUseCaseImpl implements CreateUserUseCase {
    private final UserRepositoryGateway userRepositoryGateway;
    private final UserTypeRepositoryGateway userTypeRepositoryGateway;

    public CreateUserUseCaseImpl(UserRepositoryGateway userRepositoryGateway, UserTypeRepositoryGateway userTypeRepositoryGateway) {
        this.userTypeRepositoryGateway = userTypeRepositoryGateway;
        this.userRepositoryGateway = userRepositoryGateway;
    }

    @Override
    public User execute(User user) {
        userRepositoryGateway.findByCpf(user.getCpf()).ifPresent(existingUser -> {
            throw new CpfAlreadyExistsException(user.getCpf());
        });

        userRepositoryGateway.findByLogin(user.getLogin()).ifPresent(existingUser -> {
            throw new LoginAlreadyExistsException(user.getLogin());
        });

        userRepositoryGateway.findByEmail(user.getEmail()).ifPresent(existingUser -> {
            throw new BusinessException("User with this email already exists.");
        });

        if (user.getUserType() == null || user.getUserType().getUuid() == null) {
            throw new BusinessException("User type is required.");
        }
        UserType userType = userTypeRepositoryGateway.findById(user.getUserType().getUuid()).orElseThrow(() -> new UserTypeNotFoundException("User type not found."));
        user.setUserType(userType);
        return userRepositoryGateway.save(user);
    }
}