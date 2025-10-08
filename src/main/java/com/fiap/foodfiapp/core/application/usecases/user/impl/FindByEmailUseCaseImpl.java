package com.fiap.foodfiapp.core.application.usecases.user.impl;

import com.fiap.foodfiapp.core.application.usecases.user.FindUserByEmailUseCase;
import com.fiap.foodfiapp.core.domain.entity.User;
import com.fiap.foodfiapp.core.domain.enums.AddressOwnerTypeEnum;
import com.fiap.foodfiapp.core.domain.exception.UserNotFoundException;
import com.fiap.foodfiapp.core.domain.port.AddressRepository;
import com.fiap.foodfiapp.core.domain.port.UserRepository;


public class FindByEmailUseCaseImpl implements FindUserByEmailUseCase {
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

    public FindByEmailUseCaseImpl(UserRepository userRepository, AddressRepository addressRepository) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
    }

    @Override
    public User execute(String email) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("email", email));
        loadUserAddresses(user);
        return user;
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