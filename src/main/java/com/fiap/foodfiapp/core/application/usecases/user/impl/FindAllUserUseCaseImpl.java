package com.fiap.foodfiapp.core.application.usecases.user.impl;

import com.fiap.foodfiapp.core.application.usecases.user.FindAllUserUseCase;
import com.fiap.foodfiapp.core.domain.entity.User;
import com.fiap.foodfiapp.core.domain.enums.AddressOwnerTypeEnum;
import com.fiap.foodfiapp.core.domain.port.AddressRepository;
import com.fiap.foodfiapp.core.domain.port.UserRepository;

import java.util.List;

public class FindAllUserUseCaseImpl implements FindAllUserUseCase {
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

    public FindAllUserUseCaseImpl(UserRepository userRepository, AddressRepository addressRepository) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
    }

    @Override
    public List<User> execute() {
        var users = this.userRepository.findAll();
        // Carrega os endereços para cada usuário
        users.forEach(this::loadUserAddresses);
        return users;
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