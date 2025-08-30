package com.fiap.foodfiapp.infrastructure.config;

import com.fiap.foodfiapp.core.application.gateways.UserRepositoryGateway;
import com.fiap.foodfiapp.core.application.usecases.user.impl.CreateUserUseCaseImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;

class UserUseCaseConfigTest {
    @Test
    void shouldCreateCreateUserUseCaseBean() {
        UserRepositoryGateway gateway = Mockito.mock(UserRepositoryGateway.class);
        UserUseCaseConfig config = new UserUseCaseConfig();
        CreateUserUseCaseImpl useCase = config.createUserUseCase(gateway);
        assertThat(useCase).isNotNull();
    }
}

