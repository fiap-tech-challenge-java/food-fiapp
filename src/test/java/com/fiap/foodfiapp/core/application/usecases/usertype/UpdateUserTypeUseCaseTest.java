package com.fiap.foodfiapp.core.application.usecases.usertype;

import com.fiap.foodfiapp.core.application.gateways.UserRepositoryGateway;
import com.fiap.foodfiapp.core.application.gateways.UserTypeRepositoryGateway;
import com.fiap.foodfiapp.core.application.usecases.usertype.impl.UpdateUserTypeUseCaseImpl;
import com.fiap.foodfiapp.core.domain.entity.UserType;
import com.fiap.foodfiapp.core.domain.exception.UserTypeNameAlreadyExistsException;
import com.fiap.foodfiapp.core.domain.exception.UserTypeNotFoundException;
import com.fiap.foodfiapp.core.domain.exception.UserTypeInUseException;
import com.fiap.foodfiapp.core.domain.exception.CoreUserTypeModificationException;
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

    private UpdateUserTypeUseCaseImpl updateUserTypeUseCase;

    private UUID userTypeUuid;
    private UserType existingUserType;
    private UserType userTypeUpdates;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        updateUserTypeUseCase = new UpdateUserTypeUseCaseImpl(userTypeRepositoryGateway, userRepositoryGateway);

        userTypeUuid = UUID.randomUUID();

        existingUserType = new UserType();
        existingUserType.setUuid(userTypeUuid);
        existingUserType.setName("BASIC");

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
        assertThrows(UserTypeNotFoundException.class,
            () -> updateUserTypeUseCase.execute(userTypeUuid, userTypeUpdates));

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
        when(userRepositoryGateway.existsByUserTypeUuid(userTypeUuid)).thenReturn(false);
        when(userTypeRepositoryGateway.findByName("Premium Customer")).thenReturn(Optional.of(otherUserType));

        // Act & Assert
        assertThrows(UserTypeNameAlreadyExistsException.class,
            () -> updateUserTypeUseCase.execute(userTypeUuid, userTypeUpdates));

        verify(userTypeRepositoryGateway).findById(userTypeUuid);
        verify(userTypeRepositoryGateway).findByName("Premium Customer");
        verify(userTypeRepositoryGateway, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenUserTypeInUse() {
        // Arrange
        when(userTypeRepositoryGateway.findById(userTypeUuid)).thenReturn(Optional.of(existingUserType));
        when(userRepositoryGateway.existsByUserTypeUuid(userTypeUuid)).thenReturn(true);

        // Act & Assert
        assertThrows(UserTypeInUseException.class,
            () -> updateUserTypeUseCase.execute(userTypeUuid, userTypeUpdates));

        verify(userTypeRepositoryGateway).findById(userTypeUuid);
        verify(userRepositoryGateway).existsByUserTypeUuid(userTypeUuid);
        verify(userTypeRepositoryGateway, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenModifyingCoreUserType() {
        // Arrange
        existingUserType.setName("ADMIN");
        when(userTypeRepositoryGateway.findById(userTypeUuid)).thenReturn(Optional.of(existingUserType));

        // Act & Assert
        assertThrows(CoreUserTypeModificationException.class,
            () -> updateUserTypeUseCase.execute(userTypeUuid, userTypeUpdates));

        verify(userTypeRepositoryGateway).findById(userTypeUuid);
        verify(userTypeRepositoryGateway, never()).save(any());
    }

    @Test
    void shouldUpdateWithSameNameForSameUserType() {
        // Arrange
        userTypeUpdates.setName("BASIC"); // Same name as existing

        when(userTypeRepositoryGateway.findById(userTypeUuid)).thenReturn(Optional.of(existingUserType));
        when(userTypeRepositoryGateway.findByName("BASIC")).thenReturn(Optional.of(existingUserType));
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
        assertEquals("BASIC", userTypeUpdates.getName());
        verify(userTypeRepositoryGateway).save(userTypeUpdates);
    }
}
