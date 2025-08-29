package com.fiap.foodfiapp.infrastructure.config;

import com.fiap.foodfiapp.core.application.usecases.user.CreateUserUseCase;
import com.fiap.foodfiapp.core.application.gateways.UserRepositoryGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserUseCaseConfig {
    @Bean
    public CreateUserUseCase createUserUseCase(UserRepositoryGateway userRepositoryGateway) {
        return new CreateUserUseCase(userRepositoryGateway);
    }
}

