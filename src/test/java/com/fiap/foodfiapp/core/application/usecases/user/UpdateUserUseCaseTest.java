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

class UpdateUserUseCaseTest {

    @Mock
    private UserRepositoryGateway userRepositoryGateway;

    @Mock
    private UserTypeRepositoryGateway userTypeRepositoryGateway;

    private UpdateUserUseCase updateUserUseCase;

    private UUID userId;
    private UUID userTypeId;
    private User existingUser;
    private UserType userType;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        updateUserUseCase = new UpdateUserUseCase(userRepositoryGateway, userTypeRepositoryGateway);

        userId = UUID.randomUUID();
        userTypeId = UUID.randomUUID();

        userType = new UserType();
        userType.setUuid(userTypeId);
        userType.setName("Customer");

        existingUser = new User();
        existingUser.setId(userId);
        existingUser.setEmail("existing@example.com");
        existingUser.setUserType(userType);
        existingUser.setCreatedAt(OffsetDateTime.now().minusDays(1));
    }

    @Test
    void shouldUpdateUserSuccessfully() {
        // Arrange
        User userUpdates = new User();
        userUpdates.setName("Updated Name");
        userUpdates.setEmail("updated@example.com");

        UserType newUserType = new UserType();
        newUserType.setUuid(UUID.randomUUID());
        userUpdates.setUserType(newUserType);

        when(userRepositoryGateway.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userTypeRepositoryGateway.findById(newUserType.getUuid())).thenReturn(Optional.of(newUserType));
        when(userRepositoryGateway.findByEmail("updated@example.com")).thenReturn(Optional.empty());
        when(userRepositoryGateway.save(any(User.class))).thenReturn(userUpdates);

        // Act
        User result = updateUserUseCase.execute(userId, userUpdates);

        // Assert
        assertNotNull(result);
        assertEquals(userId, userUpdates.getId());
        assertEquals(newUserType, userUpdates.getUserType());
        verify(userRepositoryGateway).save(userUpdates);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        // Arrange
        User userUpdates = new User();
        when(userRepositoryGateway.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
            () -> updateUserUseCase.execute(userId, userUpdates));

        assertEquals("User not found.", exception.getMessage());
        verify(userRepositoryGateway, never()).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenUserTypeNotFound() {
        // Arrange
        User userUpdates = new User();
        UserType invalidUserType = new UserType();
        invalidUserType.setUuid(UUID.randomUUID());
        userUpdates.setUserType(invalidUserType);

        when(userRepositoryGateway.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userTypeRepositoryGateway.findById(invalidUserType.getUuid())).thenReturn(Optional.empty());

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
            () -> updateUserUseCase.execute(userId, userUpdates));

        assertEquals("User type not found.", exception.getMessage());
        verify(userRepositoryGateway, never()).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        // Arrange
        User userUpdates = new User();
        userUpdates.setEmail("duplicate@example.com");

        User otherUser = new User();
        otherUser.setId(UUID.randomUUID());
        otherUser.setEmail("duplicate@example.com");

        when(userRepositoryGateway.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepositoryGateway.findByEmail("duplicate@example.com")).thenReturn(Optional.of(otherUser));

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
            () -> updateUserUseCase.execute(userId, userUpdates));

        assertEquals("User with this email already exists.", exception.getMessage());
        verify(userRepositoryGateway, never()).save(any(User.class));
    }

    @Test
    void shouldKeepExistingUserTypeWhenNotProvided() {
        // Arrange
        User userUpdates = new User();
        userUpdates.setName("Updated Name");
        // UserType não fornecido (null)

        when(userRepositoryGateway.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepositoryGateway.save(any(User.class))).thenReturn(userUpdates);

        // Act
        updateUserUseCase.execute(userId, userUpdates);

        // Assert
        assertEquals(existingUser.getUserType(), userUpdates.getUserType());
        verify(userRepositoryGateway).save(userUpdates);
    }
}
