package com.fiap.foodfiapp.infrastructure.config;

import com.fiap.foodfiapp.core.application.gateways.UserRepositoryGateway;
import com.fiap.foodfiapp.core.application.gateways.UserTypeRepositoryGateway;
import com.fiap.foodfiapp.core.application.usecases.usertype.CreateUserTypeUseCase;
import com.fiap.foodfiapp.core.application.usecases.usertype.DeleteUserTypeUseCase;
import com.fiap.foodfiapp.core.application.usecases.usertype.UpdateUserTypeUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserTypeUseCaseConfig {

    @Bean
    public CreateUserTypeUseCase createUserTypeUseCase(UserTypeRepositoryGateway userTypeRepositoryGateway) {
        return new CreateUserTypeUseCase(userTypeRepositoryGateway);
    }

    @Bean
    public UpdateUserTypeUseCase updateUserTypeUseCase(UserTypeRepositoryGateway userTypeRepositoryGateway) {
        return new UpdateUserTypeUseCase(userTypeRepositoryGateway);
    }

    @Bean
    public DeleteUserTypeUseCase deleteUserTypeUseCase(UserTypeRepositoryGateway userTypeRepositoryGateway, UserRepositoryGateway userRepositoryGateway) {
        return new DeleteUserTypeUseCase(userTypeRepositoryGateway, userRepositoryGateway);
    }
}
