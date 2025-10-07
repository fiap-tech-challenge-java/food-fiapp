package com.fiap.foodfiapp.core.application.usecases.user.impl;

import com.fiap.foodfiapp.core.domain.port.UserRepository;
import com.fiap.foodfiapp.core.domain.port.UserTypeRepository;
import com.fiap.foodfiapp.core.application.usecases.user.CreateUserUseCase;
import com.fiap.foodfiapp.core.domain.entity.User;
import com.fiap.foodfiapp.core.domain.entity.UserType;
import com.fiap.foodfiapp.core.domain.exception.BusinessException;
import com.fiap.foodfiapp.core.domain.exception.CpfAlreadyExistsException;
import com.fiap.foodfiapp.core.domain.exception.LoginAlreadyExistsException;
import com.fiap.foodfiapp.core.domain.exception.UserTypeNotFoundException;
import org.springframework.stereotype.Service;

public class CreateUserUseCaseImpl implements CreateUserUseCase {
    private final UserRepository userRepository;
    private final UserTypeRepository userTypeRepository;

    public CreateUserUseCaseImpl(UserRepository userRepository, UserTypeRepository userTypeRepository) {
        this.userTypeRepository = userTypeRepository;
        this.userRepository = userRepository;
    }

    @Override
    public User execute(User user) {
        userRepository.findByCpf(user.getCpf()).ifPresent(existingUser -> {
            throw new CpfAlreadyExistsException(user.getCpf());
        });

        userRepository.findByLogin(user.getLogin()).ifPresent(existingUser -> {
            throw new LoginAlreadyExistsException(user.getLogin());
        });

        userRepository.findByEmail(user.getEmail()).ifPresent(existingUser -> {
            throw new BusinessException("User with this email already exists.");
        });

        if (user.getUserType() == null || user.getUserType().getUuid() == null) {
            throw new BusinessException("User type is required.");
        }
        UserType userType = userTypeRepository.findById(user.getUserType().getUuid()).orElseThrow(() -> new UserTypeNotFoundException("User type not found."));
        user.setUserType(userType);
        return userRepository.save(user);
    }
}