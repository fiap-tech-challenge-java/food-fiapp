package com.fiap.foodfiapp.core.application.usecases.user.impl;

import com.fiap.foodfiapp.core.application.gateways.UserRepositoryGateway;
import com.fiap.foodfiapp.core.application.usecases.user.FindUserUseCase;
import com.fiap.foodfiapp.core.domain.entities.User;
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
        return null;
    }

    @Override
    public User findUserByUsername(String username) {
        return null;
    }
}
