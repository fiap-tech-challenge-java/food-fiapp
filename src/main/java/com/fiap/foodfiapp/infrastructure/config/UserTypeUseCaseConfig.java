package com.fiap.foodfiapp.infrastructure.config;

import com.fiap.foodfiapp.core.application.gateways.UserRepositoryGateway;
import com.fiap.foodfiapp.core.application.gateways.UserTypeRepositoryGateway;
import com.fiap.foodfiapp.core.application.usecases.usertype.CreateUserTypeUseCase;
import com.fiap.foodfiapp.core.application.usecases.usertype.impl.DeleteUserTypeUseCaseImpl;
import com.fiap.foodfiapp.core.application.usecases.usertype.impl.UpdateUserTypeUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserTypeUseCaseConfig {

    @Bean
    public CreateUserTypeUseCase createUserTypeUseCase(UserTypeRepositoryGateway userTypeRepositoryGateway) {
        return new CreateUserTypeUseCase(userTypeRepositoryGateway);
    }

    @Bean
    public UpdateUserTypeUseCaseImpl updateUserTypeUseCase(UserTypeRepositoryGateway userTypeRepositoryGateway, UserRepositoryGateway userRepositoryGateway) {
        return new UpdateUserTypeUseCaseImpl(userTypeRepositoryGateway, userRepositoryGateway);
    }

    @Bean
    public DeleteUserTypeUseCaseImpl deleteUserTypeUseCase(UserTypeRepositoryGateway userTypeRepositoryGateway, UserRepositoryGateway userRepositoryGateway) {
        return new DeleteUserTypeUseCaseImpl(userTypeRepositoryGateway, userRepositoryGateway);
    }
}
