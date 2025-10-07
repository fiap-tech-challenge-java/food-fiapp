package com.fiap.foodfiapp.infrastructure.config;

import com.fiap.foodfiapp.core.domain.port.UserTypeRepository;
import com.fiap.foodfiapp.core.application.usecases.user.CreateUserUseCase;
import com.fiap.foodfiapp.core.application.usecases.user.UpdateUserUseCase;
import com.fiap.foodfiapp.core.domain.port.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserUseCaseConfig {
    @Bean
    public CreateUserUseCase createUserUseCase(UserRepository userRepository, UserTypeRepository userTypeRepository) {
        return new CreateUserUseCase(userRepository, userTypeRepository);
    }

    @Bean
    public UpdateUserUseCase updateUserUseCase(UserRepository userRepository, UserTypeRepository userTypeRepository) {
        return new UpdateUserUseCase(userRepository, userTypeRepository);
    }
}
