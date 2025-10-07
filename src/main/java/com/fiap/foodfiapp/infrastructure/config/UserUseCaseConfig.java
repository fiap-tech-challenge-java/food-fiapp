package com.fiap.foodfiapp.infrastructure.config;

import com.fiap.foodfiapp.core.domain.port.UserRepository;
import com.fiap.foodfiapp.core.domain.port.UserTypeRepository;
import com.fiap.foodfiapp.core.application.usecases.user.CreateUserUseCase;
import com.fiap.foodfiapp.core.application.usecases.user.UpdateUserUseCase;
import com.fiap.foodfiapp.core.application.usecases.user.FindUserUseCase;
import com.fiap.foodfiapp.core.application.usecases.user.impl.CreateUserUseCaseImpl;
import com.fiap.foodfiapp.core.application.usecases.user.impl.UpdateUserUseCaseImpl;
import com.fiap.foodfiapp.core.application.usecases.user.impl.FindUserUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserUseCaseConfig {

    @Bean
    public CreateUserUseCase createUserUseCase(UserRepository userRepository, UserTypeRepository userTypeRepository) {
        // Instanciamos a implementação concreta do caso de uso
        return new CreateUserUseCaseImpl(userRepository, userTypeRepository);
    }

    @Bean
    public UpdateUserUseCase updateUserUseCase(UserRepository userRepository, UserTypeRepository userTypeRepository) {
        return new UpdateUserUseCaseImpl(userRepository, userTypeRepository);
    }

    @Bean
    public FindUserUseCase findUserUseCase(UserRepository userRepository) {
        return new FindUserUseCaseImpl(userRepository);
    }
}