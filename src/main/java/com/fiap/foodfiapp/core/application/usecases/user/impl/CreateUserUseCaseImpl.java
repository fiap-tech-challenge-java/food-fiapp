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
        // RN: Reviver utilizador inativo se houver match por e-mail
        var existingByEmail = userRepository.findByEmail(user.getEmail());
        if (existingByEmail.isPresent()) {
            var existing = existingByEmail.get();
            if (Boolean.TRUE.equals(existing.getIsActive())) {
                // Mantém a lógica atual de conflito quando já está ativo
                throw new BusinessException("User with this email already exists.");
            }
            // Revive usuário inativo com os novos dados
            applyUserUpdates(existing, user);
            existing.setIsActive(true);
            var revived = userRepository.save(existing);
            // Salvar endereços novos, se informados
            if (user.getAddresses() != null && !user.getAddresses().isEmpty()) {
                var savedAddresses = user.getAddresses().stream()
                        .map(address -> addressRepository.save(address, revived.getId(), AddressesOwnerTypeEnum.USER.getDescription()))
                        .collect(Collectors.toList());
                revived.setAddresses(savedAddresses);
            }
            return revived;
        }

        // RN: Reviver utilizador inativo se houver match por CPF
        var existingByCpf = userRepository.findByCpf(user.getCpf());
        if (existingByCpf.isPresent()) {
            var existing = existingByCpf.get();
            if (Boolean.TRUE.equals(existing.getIsActive())) {
                // Mantém a lógica atual de conflito quando já está ativo
                throw new CpfAlreadyExistsException(user.getCpf());
            }
            // Revive usuário inativo com os novos dados
            applyUserUpdates(existing, user);
            existing.setIsActive(true);
            var revived = userRepository.save(existing);
            // Salvar endereços novos, se informados
            if (user.getAddresses() != null && !user.getAddresses().isEmpty()) {
                var savedAddresses = user.getAddresses().stream()
                        .map(address -> addressRepository.save(address, revived.getId(), AddressesOwnerTypeEnum.USER.getDescription()))
                        .collect(Collectors.toList());
                revived.setAddresses(savedAddresses);
            }
            return revived;
        }

        // Validações de negócio (permanecem para casos sem revival)
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

    private void applyUserUpdates(User target, User source) {
        target.setName(source.getName());
        target.setEmail(source.getEmail());
        target.setCpf(source.getCpf());
        target.setLogin(source.getLogin());
        target.setPassword(source.getPassword());
        target.setUserType(source.getUserType());
        // Endereços são tratados após salvar o usuário (vinculados por ownerId)
    }
}