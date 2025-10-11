package com.fiap.foodfiapp.core.application.usecases.user;

import com.fiap.foodfiapp.core.application.usecases.user.impl.ValidateAdminOrOwnerUseCaseImpl;
import com.fiap.foodfiapp.core.domain.entity.User;
import com.fiap.foodfiapp.core.domain.entity.UserType;
import com.fiap.foodfiapp.core.domain.exception.UserNotFoundException;
import com.fiap.foodfiapp.core.domain.port.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ValidateAdminOrOwnerUseCaseTest {

    @Mock
    private UserRepository userRepository;

    private ValidateAdminOrOwnerUseCaseImpl validateAdminOrOwnerUseCase;

    private UUID userId;
    private UUID resourceOwnerId;
    private User adminUser;
    private User ownerUser;
    private User regularUser;

    @BeforeEach
    void setUp() {
        validateAdminOrOwnerUseCase = new ValidateAdminOrOwnerUseCaseImpl(userRepository);

        userId = UUID.randomUUID();
        resourceOwnerId = UUID.randomUUID();

        // Setup admin user
        UserType adminType = new UserType();
        adminType.setName("ADMIN");
        adminUser = new User();
        adminUser.setId(userId);
        adminUser.setUserType(adminType);

        // Setup owner user
        UserType ownerType = new UserType();
        ownerType.setName("OWNER");
        ownerUser = new User();
        ownerUser.setId(resourceOwnerId);
        ownerUser.setUserType(ownerType);

        // Setup regular user
        UserType customerType = new UserType();
        customerType.setName("CUSTOMER");
        regularUser = new User();
        regularUser.setId(userId);
        regularUser.setUserType(customerType);
    }

    @Test
    void shouldReturnTrueWhenUserIsAdmin() {
        // Arrange
        when(userRepository.findById(userId)).thenReturn(Optional.of(adminUser));

        // Act
        boolean result = validateAdminOrOwnerUseCase.execute(userId, resourceOwnerId);

        // Assert
        assertTrue(result);
        verify(userRepository).findById(userId);
    }

    @Test
    void shouldReturnTrueWhenUserIsOwner() {
        // Arrange
        when(userRepository.findById(resourceOwnerId)).thenReturn(Optional.of(ownerUser));

        // Act
        boolean result = validateAdminOrOwnerUseCase.execute(resourceOwnerId, resourceOwnerId);

        // Assert
        assertTrue(result);
        verify(userRepository).findById(resourceOwnerId);
    }

    @Test
    void shouldReturnFalseWhenUserIsNeitherAdminNorOwner() {
        // Arrange
        UUID differentOwnerId = UUID.randomUUID();
        when(userRepository.findById(userId)).thenReturn(Optional.of(regularUser));

        // Act
        boolean result = validateAdminOrOwnerUseCase.execute(userId, differentOwnerId);

        // Assert
        assertFalse(result);
        verify(userRepository).findById(userId);
    }

    @Test
    void shouldReturnFalseWhenResourceOwnerIdIsNull() {
        // Arrange
        when(userRepository.findById(userId)).thenReturn(Optional.of(regularUser));

        // Act
        boolean result = validateAdminOrOwnerUseCase.execute(userId, null);

        // Assert
        assertFalse(result);
        verify(userRepository).findById(userId);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        // Arrange
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        UserNotFoundException exception = assertThrows(
            UserNotFoundException.class,
            () -> validateAdminOrOwnerUseCase.execute(userId, resourceOwnerId)
        );

        assertTrue(exception.getMessage().contains(userId.toString()));
        verify(userRepository).findById(userId);
    }

    @Test
    void shouldReturnTrueWhenAdminAccessesAnyResource() {
        // Arrange
        UUID anyResourceOwnerId = UUID.randomUUID();
        when(userRepository.findById(userId)).thenReturn(Optional.of(adminUser));

        // Act
        boolean result = validateAdminOrOwnerUseCase.execute(userId, anyResourceOwnerId);

        // Assert
        assertTrue(result);
        verify(userRepository).findById(userId);
    }

    @Test
    void shouldHandleUserWithNullUserType() {
        // Arrange
        User userWithNullType = new User();
        userWithNullType.setId(userId);
        userWithNullType.setUserType(null);
        when(userRepository.findById(userId)).thenReturn(Optional.of(userWithNullType));

        // Act
        boolean result = validateAdminOrOwnerUseCase.execute(userId, resourceOwnerId);

        // Assert
        assertFalse(result);
        verify(userRepository).findById(userId);
    }

    @Test
    void shouldBeCaseInsensitiveForAdminCheck() {
        // Arrange
        UserType adminTypeLowerCase = new UserType();
        adminTypeLowerCase.setName("admin");
        User adminUserLowerCase = new User();
        adminUserLowerCase.setId(userId);
        adminUserLowerCase.setUserType(adminTypeLowerCase);
        when(userRepository.findById(userId)).thenReturn(Optional.of(adminUserLowerCase));

        // Act
        boolean result = validateAdminOrOwnerUseCase.execute(userId, resourceOwnerId);

        // Assert
        assertTrue(result);
        verify(userRepository).findById(userId);
    }
}
