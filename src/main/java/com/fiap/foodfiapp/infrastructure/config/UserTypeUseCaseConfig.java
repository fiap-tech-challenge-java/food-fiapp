package com.fiap.foodfiapp.infrastructure.config;

import com.fiap.foodfiapp.core.application.usecases.usertype.DeleteUserTypeUseCase;
import com.fiap.foodfiapp.core.application.usecases.usertype.UpdateUserTypeUseCase;
import com.fiap.foodfiapp.core.application.usecases.usertype.impl.CreateUserTypeUseCaseImpl;
import com.fiap.foodfiapp.core.domain.port.UserRepository;
import com.fiap.foodfiapp.core.domain.port.UserTypeRepository;
import com.fiap.foodfiapp.core.application.usecases.usertype.CreateUserTypeUseCase;
import com.fiap.foodfiapp.core.application.usecases.usertype.impl.DeleteUserTypeUseCaseImpl;
import com.fiap.foodfiapp.core.application.usecases.usertype.impl.UpdateUserTypeUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserTypeUseCaseConfig {

    @Bean
    public CreateUserTypeUseCase createUserTypeUseCase(UserTypeRepository userTypeRepository) {
        return new CreateUserTypeUseCaseImpl(userTypeRepository);
    }

    @Bean
    public UpdateUserTypeUseCase updateUserTypeUseCase(UserTypeRepository userTypeRepository, UserRepository userRepository) {
        return new UpdateUserTypeUseCaseImpl(userTypeRepository, userRepository);
    }

    @Bean
    public DeleteUserTypeUseCase deleteUserTypeUseCase(UserTypeRepository userTypeRepository, UserRepository userRepository) {
        return new DeleteUserTypeUseCaseImpl(userTypeRepository, userRepository);
    }
}
