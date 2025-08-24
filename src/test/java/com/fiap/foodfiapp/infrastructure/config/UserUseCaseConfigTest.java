package com.fiap.foodfiapp.infrastructure.config;

import com.fiap.foodfiapp.application.gateways.UserRepositoryGateway;
import com.fiap.foodfiapp.application.usecases.user.CreateUserUseCase;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;

class UserUseCaseConfigTest {
    @Test
    void shouldCreateCreateUserUseCaseBean() {
        UserRepositoryGateway gateway = Mockito.mock(UserRepositoryGateway.class);
        UserUseCaseConfig config = new UserUseCaseConfig();
        CreateUserUseCase useCase = config.createUserUseCase(gateway);
        assertThat(useCase).isNotNull();
    }
}

