package com.fiap.foodfiapp.core.application.usecases.user;

import com.fiap.foodfiapp.core.application.gateways.UserRepositoryGateway;
import com.fiap.foodfiapp.core.application.gateways.UserTypeRepositoryGateway;
import com.fiap.foodfiapp.core.domain.entity.User;
import com.fiap.foodfiapp.core.domain.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CreateUserUseCaseTest {
    private UserRepositoryGateway userRepositoryGateway;
    private UserTypeRepositoryGateway userTypeRepositoryGateway;
    private CreateUserUseCase createUserUseCase;

    @BeforeEach
    void setUp() {
        userRepositoryGateway = Mockito.mock(UserRepositoryGateway.class);
        userTypeRepositoryGateway = Mockito.mock(UserTypeRepositoryGateway.class);
        createUserUseCase = new CreateUserUseCase(userRepositoryGateway, userTypeRepositoryGateway);
    }

    @Test
    void shouldCreateUserWhenEmailNotExists() {
        User user = new User(UUID.randomUUID(), "Test", "test@email.com", "login", "12345678901", null, null, true, OffsetDateTime.now(), OffsetDateTime.now(), "password123");
        when(userRepositoryGateway.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(userRepositoryGateway.save(any(User.class))).thenReturn(user);

        User created = createUserUseCase.execute(user);
        assertNotNull(created);
        assertEquals(user.getEmail(), created.getEmail());
        verify(userRepositoryGateway).save(user);
    }

    @Test
    void shouldThrowBusinessExceptionWhenEmailExists() {
        User user = new User(UUID.randomUUID(), "Test", "test@email.com", "login", "12345678901", null, null, true, OffsetDateTime.now(), OffsetDateTime.now(), "password123");
        when(userRepositoryGateway.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        assertThrows(BusinessException.class, () -> createUserUseCase.execute(user));
        verify(userRepositoryGateway, never()).save(any());
    }
}

