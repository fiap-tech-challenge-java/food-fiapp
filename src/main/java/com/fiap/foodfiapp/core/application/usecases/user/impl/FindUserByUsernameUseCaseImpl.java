package com.fiap.foodfiapp.core.application.usecases.user.impl;

import com.fiap.foodfiapp.core.application.usecases.user.FindUserByUsernameUseCase;
import com.fiap.foodfiapp.core.domain.entity.User;
import com.fiap.foodfiapp.core.domain.enums.AddressOwnerTypeEnum;
import com.fiap.foodfiapp.core.domain.exception.UserNotFoundException;
import com.fiap.foodfiapp.core.domain.port.AddressRepository;
import com.fiap.foodfiapp.core.domain.port.UserRepository;

public class FindUserByUsernameUseCaseImpl implements FindUserByUsernameUseCase {
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

    public FindUserByUsernameUseCaseImpl(UserRepository userRepository, AddressRepository addressRepository) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
    }

    @Override
    public User execute(String username) {
        var user = userRepository.findByLogin(username)
                .orElseThrow(() -> new UserNotFoundException("login", username));
        loadUserAddresses(user);
        return user;
    }


    private void loadUserAddresses(User user) {
        if (user != null && user.getId() != null) {
            var addresses = addressRepository.findByOwner(user.getId(), AddressOwnerTypeEnum.USER.getDescription());
            user.setAddress(addresses);
        }
    }
}