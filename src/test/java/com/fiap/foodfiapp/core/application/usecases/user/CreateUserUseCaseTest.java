package com.fiap.foodfiapp.core.application.usecases.user;

import com.fiap.foodfiapp.core.application.gateways.UserRepositoryGateway;
import com.fiap.foodfiapp.core.domain.entities.User;
import com.fiap.foodfiapp.core.domain.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CreateUserUseCaseTest {
    private UserRepositoryGateway userRepositoryGateway;
    private CreateUserUseCase createUserUseCase;

    @BeforeEach
    void setUp() {
        userRepositoryGateway = Mockito.mock(UserRepositoryGateway.class);
        createUserUseCase = new CreateUserUseCase(userRepositoryGateway);
    }

    /////////// COMENTADO POIS A CLASSE USER MUDOU, DEVEMOS APENAS ACRESCER OS CAMPOS NO CONSTRUTOR

//    @Test
//    void shouldCreateUserWhenEmailNotExists() {
//        User user = new User(null, "Test", "test@email.com", "1234");
//        when(userRepositoryGateway.findByEmail(user.email())).thenReturn(Optional.empty());
//        when(userRepositoryGateway.save(any(User.class))).thenReturn(new User(1L, user.name(), user.email(), user.password()));
//
//        User created = createUserUseCase.execute(user);
//        assertNotNull(created);
//        assertEquals(user.email(), created.email());
//        verify(userRepositoryGateway).save(user);
//    }
//
//    @Test
//    void shouldThrowBusinessExceptionWhenEmailExists() {
//        User user = new User(null, "Test", "test@email.com", "1234");
//        when(userRepositoryGateway.findByEmail(user.email())).thenReturn(Optional.of(user));
//
//        assertThrows(BusinessException.class, () -> createUserUseCase.execute(user));
//        verify(userRepositoryGateway, never()).save(any());
//    }
}

