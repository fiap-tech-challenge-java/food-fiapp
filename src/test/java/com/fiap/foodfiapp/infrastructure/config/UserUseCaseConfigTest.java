package com.fiap.foodfiapp.infrastructure.config;

import com.fiap.foodfiapp.core.application.gateways.UserRepositoryGateway;
import com.fiap.foodfiapp.core.application.gateways.UserTypeRepositoryGateway;
import com.fiap.foodfiapp.core.application.usecases.user.CreateUserUseCase;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;

class UserUseCaseConfigTest {
    @Test
    void shouldCreateCreateUserUseCaseBean() {
        UserRepositoryGateway gateway = Mockito.mock(UserRepositoryGateway.class);
        UserTypeRepositoryGateway userTypeRepositoryGateway = Mockito.mock(UserTypeRepositoryGateway.class);
        UserUseCaseConfig config = new UserUseCaseConfig();
        CreateUserUseCase useCase = config.createUserUseCase(gateway, userTypeRepositoryGateway);
        assertThat(useCase).isNotNull();
    }
}

