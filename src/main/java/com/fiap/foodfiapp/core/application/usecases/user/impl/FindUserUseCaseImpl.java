package com.fiap.foodfiapp.core.application.usecases.user.impl;

import com.fiap.foodfiapp.core.application.gateways.UserRepositoryGateway;
import com.fiap.foodfiapp.core.application.usecases.user.FindUserUseCase;
import com.fiap.foodfiapp.core.domain.entity.User;
import com.fiap.foodfiapp.core.domain.exception.UserNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FindUserUseCaseImpl implements FindUserUseCase {
    private final UserRepositoryGateway userRepositoryGateway;

    public FindUserUseCaseImpl(UserRepositoryGateway userRepositoryGateway) {
        this.userRepositoryGateway = userRepositoryGateway;
    }

    @Override
    public List<User> findAll() {
        return this.userRepositoryGateway.findAll();
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepositoryGateway.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("email", email));
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepositoryGateway.findByLogin(username)
                .orElseThrow(() -> new UserNotFoundException("login", username));
    }
}