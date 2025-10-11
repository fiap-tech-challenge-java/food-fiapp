package com.fiap.foodfiapp.core.application.usecases.user;

import com.fiap.foodfiapp.core.application.usecases.user.impl.IsAdminUseCaseImpl;
import com.fiap.foodfiapp.core.domain.entity.User;
import com.fiap.foodfiapp.core.domain.entity.UserType;
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
class IsAdminUseCaseTest {

    @Mock
    private UserRepository userRepository;

    private IsAdminUseCaseImpl isAdminUseCase;

    private UUID userId;
    private User adminUser;
    private User nonAdminUser;

    @BeforeEach
    void setUp() {
        isAdminUseCase = new IsAdminUseCaseImpl(userRepository);

        userId = UUID.randomUUID();

        // Setup admin user
        UserType adminType = new UserType();
        adminType.setName("ADMIN");
        adminUser = new User();
        adminUser.setId(userId);
        adminUser.setUserType(adminType);

        // Setup non-admin user
        UserType customerType = new UserType();
        customerType.setName("CUSTOMER");
        nonAdminUser = new User();
        nonAdminUser.setId(userId);
        nonAdminUser.setUserType(customerType);
    }

    @Test
    void shouldReturnTrueWhenUserIsAdmin() {
        // Arrange
        when(userRepository.findById(userId)).thenReturn(Optional.of(adminUser));

        // Act
        boolean result = isAdminUseCase.execute(userId);

        // Assert
        assertTrue(result);
        verify(userRepository).findById(userId);
    }

    @Test
    void shouldReturnFalseWhenUserIsNotAdmin() {
        // Arrange
        when(userRepository.findById(userId)).thenReturn(Optional.of(nonAdminUser));

        // Act
        boolean result = isAdminUseCase.execute(userId);

        // Assert
        assertFalse(result);
        verify(userRepository).findById(userId);
    }

    @Test
    void shouldReturnFalseWhenUserNotFound() {
        // Arrange
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act
        boolean result = isAdminUseCase.execute(userId);

        // Assert
        assertFalse(result);
        verify(userRepository).findById(userId);
    }

    @Test
    void shouldReturnFalseWhenUserTypeIsNull() {
        // Arrange
        User userWithNullType = new User();
        userWithNullType.setId(userId);
        userWithNullType.setUserType(null);
        when(userRepository.findById(userId)).thenReturn(Optional.of(userWithNullType));

        // Act
        boolean result = isAdminUseCase.execute(userId);

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
        boolean result = isAdminUseCase.execute(userId);

        // Assert
        assertTrue(result);
        verify(userRepository).findById(userId);
    }

    @Test
    void shouldHandleMixedCaseAdminType() {
        // Arrange
        UserType adminTypeMixedCase = new UserType();
        adminTypeMixedCase.setName("AdMiN");
        User adminUserMixedCase = new User();
        adminUserMixedCase.setId(userId);
        adminUserMixedCase.setUserType(adminTypeMixedCase);
        when(userRepository.findById(userId)).thenReturn(Optional.of(adminUserMixedCase));

        // Act
        boolean result = isAdminUseCase.execute(userId);

        // Assert
        assertTrue(result);
        verify(userRepository).findById(userId);
    }

    @Test
    void shouldReturnFalseForOwnerType() {
        // Arrange
        UserType ownerType = new UserType();
        ownerType.setName("OWNER");
        User ownerUser = new User();
        ownerUser.setId(userId);
        ownerUser.setUserType(ownerType);
        when(userRepository.findById(userId)).thenReturn(Optional.of(ownerUser));

        // Act
        boolean result = isAdminUseCase.execute(userId);

        // Assert
        assertFalse(result);
        verify(userRepository).findById(userId);
    }
}
