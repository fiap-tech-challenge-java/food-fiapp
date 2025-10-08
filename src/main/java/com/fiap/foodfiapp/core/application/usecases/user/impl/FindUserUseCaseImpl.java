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
    public List<User> findAll() {
        var users = this.userRepository.findAll();
        // Carrega os endereços para cada usuário
        users.forEach(this::loadUserAddresses);
        return users;
    }

    @Override
    public User findUserByEmail(String email) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("email", email));
        loadUserAddresses(user);
        return user;
    }

    @Override
    public User findUserByUsername(String username) {
        var user = userRepository.findByLogin(username)
                .orElseThrow(() -> new UserNotFoundException("login", username));
        loadUserAddresses(user);
        return user;
    }

    @Override
    public Optional<User> findById(UUID id) {
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