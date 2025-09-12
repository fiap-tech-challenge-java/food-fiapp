package com.fiap.foodfiapp.core.application.usecases.user;

import com.fiap.foodfiapp.core.application.gateways.UserRepositoryGateway;
import com.fiap.foodfiapp.core.application.gateways.UserTypeRepositoryGateway;
import com.fiap.foodfiapp.core.domain.entity.User;
import com.fiap.foodfiapp.core.domain.entity.UserType;
import com.fiap.foodfiapp.core.domain.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CreateUserUseCaseTest {

    @Mock
    private UserRepositoryGateway userRepositoryGateway;

    @Mock
    private UserTypeRepositoryGateway userTypeRepositoryGateway;

    private CreateUserUseCase createUserUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        createUserUseCase = new CreateUserUseCase(userRepositoryGateway, userTypeRepositoryGateway);
    }

    @Test
    void shouldCreateUserWhenEmailNotExistsAndUserTypeExists() {
        // Arrange
        UUID userTypeId = UUID.randomUUID();
        UserType userType = new UserType();
        userType.setUuid(userTypeId);
        userType.setName("Customer");

        User user = new User(null, "Test", "test@email.com", "login", "12345678901", null, userType, true, OffsetDateTime.now(), OffsetDateTime.now(), "password123");

        when(userRepositoryGateway.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(userTypeRepositoryGateway.findById(userTypeId)).thenReturn(Optional.of(userType));
        when(userRepositoryGateway.save(any(User.class))).thenReturn(user);

        // Act
        User created = createUserUseCase.execute(user);

        // Assert
        assertNotNull(created);
        assertEquals(user.getEmail(), created.getEmail());
        assertEquals(userType, created.getUserType());
        verify(userRepositoryGateway).save(user);
    }

    @Test
    void shouldThrowBusinessExceptionWhenEmailExists() {
        // Arrange
        UUID userTypeId = UUID.randomUUID();
        UserType userType = new UserType();
        userType.setUuid(userTypeId);

        User user = new User(null, "Test", "test@email.com", "login", "12345678901", null, userType, true, OffsetDateTime.now(), OffsetDateTime.now(), "password123");
        when(userRepositoryGateway.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> createUserUseCase.execute(user));
        assertEquals("User with this email already exists.", exception.getMessage());
        verify(userRepositoryGateway, never()).save(any());
    }

    @Test
    void shouldThrowBusinessExceptionWhenUserTypeIsNull() {
        // Arrange
        User user = new User(null, "Test", "test@email.com", "login", "12345678901", null, null, true, OffsetDateTime.now(), OffsetDateTime.now(), "password123");
        when(userRepositoryGateway.findByEmail(user.getEmail())).thenReturn(Optional.empty());

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> createUserUseCase.execute(user));
        assertEquals("User type is required.", exception.getMessage());
        verify(userRepositoryGateway, never()).save(any());
    }

    @Test
    void shouldThrowBusinessExceptionWhenUserTypeNotFound() {
        // Arrange
        UUID userTypeId = UUID.randomUUID();
        UserType userType = new UserType();
        userType.setUuid(userTypeId);

        User user = new User(null, "Test", "test@email.com", "login", "12345678901", null, userType, true, OffsetDateTime.now(), OffsetDateTime.now(), "password123");

        when(userRepositoryGateway.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(userTypeRepositoryGateway.findById(userTypeId)).thenReturn(Optional.empty());

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> createUserUseCase.execute(user));
        assertEquals("User type not found.", exception.getMessage());
        verify(userRepositoryGateway, never()).save(any());
    }
}
