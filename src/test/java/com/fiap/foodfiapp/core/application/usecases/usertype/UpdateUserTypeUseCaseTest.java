package com.fiap.foodfiapp.core.application.usecases.usertype;

import com.fiap.foodfiapp.core.application.gateways.UserRepositoryGateway;
import com.fiap.foodfiapp.core.application.gateways.UserTypeRepositoryGateway;
import com.fiap.foodfiapp.core.domain.entity.UserType;
import com.fiap.foodfiapp.core.domain.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UpdateUserTypeUseCaseTest {

    @Mock
    private UserTypeRepositoryGateway userTypeRepositoryGateway;

    @Mock
    private UserRepositoryGateway userRepositoryGateway;

    private UpdateUserTypeUseCase updateUserTypeUseCase;

    private UUID userTypeUuid;
    private UserType existingUserType;
    private UserType userTypeUpdates;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        updateUserTypeUseCase = new UpdateUserTypeUseCase(userTypeRepositoryGateway, userRepositoryGateway);

        userTypeUuid = UUID.randomUUID();

        existingUserType = new UserType();
        existingUserType.setUuid(userTypeUuid);
        existingUserType.setName("Customer");

        userTypeUpdates = new UserType();
        userTypeUpdates.setName("Premium Customer");
    }

    @Test
    void shouldUpdateUserTypeSuccessfully() {
        // Arrange
        when(userTypeRepositoryGateway.findById(userTypeUuid)).thenReturn(Optional.of(existingUserType));
        when(userRepositoryGateway.existsByUserTypeUuid(userTypeUuid)).thenReturn(false);
        when(userTypeRepositoryGateway.findByName("Premium Customer")).thenReturn(Optional.empty());
        when(userTypeRepositoryGateway.save(any(UserType.class))).thenReturn(userTypeUpdates);

        // Act
        UserType result = updateUserTypeUseCase.execute(userTypeUuid, userTypeUpdates);

        // Assert
        assertNotNull(result);
        assertEquals(userTypeUuid, userTypeUpdates.getUuid());
        verify(userTypeRepositoryGateway).findById(userTypeUuid);
        verify(userRepositoryGateway).existsByUserTypeUuid(userTypeUuid);
        verify(userTypeRepositoryGateway).findByName("Premium Customer");
        verify(userTypeRepositoryGateway).save(userTypeUpdates);
    }

    @Test
    void shouldThrowExceptionWhenUserTypeNotFound() {
        // Arrange
        when(userTypeRepositoryGateway.findById(userTypeUuid)).thenReturn(Optional.empty());

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
            () -> updateUserTypeUseCase.execute(userTypeUuid, userTypeUpdates));

        assertEquals("User type not found.", exception.getMessage());
        verify(userTypeRepositoryGateway).findById(userTypeUuid);
        verify(userTypeRepositoryGateway, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenNameExistsForDifferentUserType() {
        // Arrange
        UserType otherUserType = new UserType();
        otherUserType.setUuid(UUID.randomUUID());
        otherUserType.setName("Premium Customer");

        when(userTypeRepositoryGateway.findById(userTypeUuid)).thenReturn(Optional.of(existingUserType));
        when(userTypeRepositoryGateway.findByName("Premium Customer")).thenReturn(Optional.of(otherUserType));

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
            () -> updateUserTypeUseCase.execute(userTypeUuid, userTypeUpdates));

        assertEquals("User type with this name already exists.", exception.getMessage());
        verify(userTypeRepositoryGateway).findById(userTypeUuid);
        verify(userTypeRepositoryGateway).findByName("Premium Customer");
        verify(userTypeRepositoryGateway, never()).save(any());
    }

    @Test
    void shouldUpdateWithSameNameForSameUserType() {
        // Arrange
        userTypeUpdates.setName("Customer"); // Same name as existing

        when(userTypeRepositoryGateway.findById(userTypeUuid)).thenReturn(Optional.of(existingUserType));
        when(userTypeRepositoryGateway.findByName("Customer")).thenReturn(Optional.of(existingUserType));
        when(userTypeRepositoryGateway.save(any(UserType.class))).thenReturn(userTypeUpdates);

        // Act
        UserType result = updateUserTypeUseCase.execute(userTypeUuid, userTypeUpdates);

        // Assert
        assertNotNull(result);
        verify(userTypeRepositoryGateway).save(userTypeUpdates);
    }

    @Test
    void shouldPreserveExistingNameWhenNullProvided() {
        // Arrange
        userTypeUpdates.setName(null);

        when(userTypeRepositoryGateway.findById(userTypeUuid)).thenReturn(Optional.of(existingUserType));
        when(userTypeRepositoryGateway.save(any(UserType.class))).thenReturn(userTypeUpdates);

        // Act
        updateUserTypeUseCase.execute(userTypeUuid, userTypeUpdates);

        // Assert
        assertEquals("Customer", userTypeUpdates.getName());
        verify(userTypeRepositoryGateway).save(userTypeUpdates);
    }
}
