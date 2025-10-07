package com.fiap.foodfiapp.core.application.usecases.user.impl;

import com.fiap.foodfiapp.core.domain.port.AddressRepository;
import com.fiap.foodfiapp.core.domain.port.UserRepository;
import com.fiap.foodfiapp.core.domain.port.UserTypeRepository;
import com.fiap.foodfiapp.core.application.usecases.user.CreateUserUseCase;
import com.fiap.foodfiapp.core.domain.entity.User;
import com.fiap.foodfiapp.core.domain.entity.UserType;
import com.fiap.foodfiapp.core.domain.exception.BusinessException;
import com.fiap.foodfiapp.core.domain.exception.CpfAlreadyExistsException;
import com.fiap.foodfiapp.core.domain.exception.LoginAlreadyExistsException;
import com.fiap.foodfiapp.core.domain.exception.UserTypeNotFoundException;
import com.fiap.foodfiapp.infrastructure.persistence.enums.AddressesOwnerTypeEnum;

import java.util.stream.Collectors;

public class CreateUserUseCaseImpl implements CreateUserUseCase {
    private final UserRepository userRepository;
    private final UserTypeRepository userTypeRepository;
    private final AddressRepository addressRepository; // Dependência adicionada

    public CreateUserUseCaseImpl(UserRepository userRepository, UserTypeRepository userTypeRepository, AddressRepository addressRepository) {
        this.userRepository = userRepository;
        this.userTypeRepository = userTypeRepository;
        this.addressRepository = addressRepository; // Injetada
    }

    @Override
    public User execute(User user) {
        // Validações de negócio
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
        UserType userType = userTypeRepository.findById(user.getUserType().getUuid())
                .orElseThrow(() -> new UserTypeNotFoundException("User type not found."));
        user.setUserType(userType);

        // Salva o usuário primeiro para obter o ID
        User savedUser = userRepository.save(user);

        // Salva os endereços associados ao usuário
        if (user.getAddresses() != null && !user.getAddresses().isEmpty()) {
            var savedAddresses = user.getAddresses().stream()
                    .map(address -> addressRepository.save(address, savedUser.getId(), AddressesOwnerTypeEnum.USER.getDescription()))
                    .collect(Collectors.toList());
            savedUser.setAddresses(savedAddresses);
        }

        return savedUser;
    }
}