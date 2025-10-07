package com.fiap.foodfiapp.core.application.usecases.user.impl;

import com.fiap.foodfiapp.core.domain.port.UserRepository;
import com.fiap.foodfiapp.core.application.usecases.user.FindUserUseCase;
import com.fiap.foodfiapp.core.domain.entity.User;
import com.fiap.foodfiapp.core.domain.exception.UserNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FindUserUseCaseImpl implements FindUserUseCase {
    private final UserRepository userRepository;

    public FindUserUseCaseImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> findAll() {
        return this.userRepository.findAll();
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("email", email));
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findByLogin(username)
                .orElseThrow(() -> new UserNotFoundException("login", username));
    }

    @Override
    public Optional<User> findById(UUID id) {
        return userRepository.findById(id);
    }
}