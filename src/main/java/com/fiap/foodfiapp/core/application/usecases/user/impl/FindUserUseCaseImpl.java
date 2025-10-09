package com.fiap.foodfiapp.core.application.usecases.user.impl;

import com.fiap.foodfiapp.core.domain.port.AddressRepository;
import com.fiap.foodfiapp.core.domain.port.UserRepository;
import com.fiap.foodfiapp.core.application.usecases.user.FindUserUseCase;
import com.fiap.foodfiapp.core.domain.entity.User;
import com.fiap.foodfiapp.core.domain.enums.AddressOwnerTypeEnum;
import com.fiap.foodfiapp.core.domain.exception.UserNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FindUserUseCaseImpl implements FindUserUseCase {
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

    public FindUserUseCaseImpl(UserRepository userRepository, AddressRepository addressRepository) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
    }

    @Override
    public Optional<User> execute(UUID id) {
        if (id == null) {
            return Optional.empty();
        }
        var optionalUser = userRepository.findById(id);
        optionalUser.ifPresent(this::loadUserAddresses);
        return optionalUser;
    }

    /**
     * Carrega os endereços associados ao usuário
     */
    private void loadUserAddresses(User user) {
        if (user != null && user.getId() != null) {
            var addresses = addressRepository.findByOwner(user.getId(), AddressOwnerTypeEnum.USER.getDescription());
            user.setAddress(addresses);
        }
    }
}