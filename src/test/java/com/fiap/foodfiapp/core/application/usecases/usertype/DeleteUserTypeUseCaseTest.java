package com.fiap.foodfiapp.core.application.usecases.usertype;

import com.fiap.foodfiapp.core.application.gateways.UserRepositoryGateway;
import com.fiap.foodfiapp.core.application.gateways.UserTypeRepositoryGateway;
import com.fiap.foodfiapp.core.application.usecases.usertype.impl.DeleteUserTypeUseCaseImpl;
import com.fiap.foodfiapp.core.domain.entity.UserType;
import com.fiap.foodfiapp.core.domain.exception.CoreUserTypeModificationException;
import com.fiap.foodfiapp.core.domain.exception.UserTypeInUseException;
import com.fiap.foodfiapp.core.domain.exception.UserTypeNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DeleteUserTypeUseCaseTest {

    @Mock
    private UserTypeRepositoryGateway userTypeRepositoryGateway;

    @Mock
    private UserRepositoryGateway userRepositoryGateway;

    private DeleteUserTypeUseCaseImpl deleteUserTypeUseCase;

    private UUID userTypeUuid;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        deleteUserTypeUseCase = new DeleteUserTypeUseCaseImpl(userTypeRepositoryGateway, userRepositoryGateway);
        userTypeUuid = UUID.randomUUID();
    }

    @Test
    void shouldDeleteUserTypeWhenNotInUse() {
        // Arrange
        UserType userType = new UserType();
        userType.setName("BASIC");
        when(userTypeRepositoryGateway.findById(userTypeUuid)).thenReturn(Optional.of(userType));
        when(userRepositoryGateway.existsByUserTypeUuid(userTypeUuid)).thenReturn(false);

        // Act
        deleteUserTypeUseCase.execute(userTypeUuid);

        // Assert
        verify(userTypeRepositoryGateway).findById(userTypeUuid);
        verify(userRepositoryGateway).existsByUserTypeUuid(userTypeUuid);
        verify(userTypeRepositoryGateway).deleteById(userTypeUuid);
    }

    @Test
    void shouldThrowExceptionWhenUserTypeNotFound() {
        // Arrange
        when(userTypeRepositoryGateway.findById(userTypeUuid)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserTypeNotFoundException.class,
            () -> deleteUserTypeUseCase.execute(userTypeUuid));

        verify(userTypeRepositoryGateway).findById(userTypeUuid);
        verify(userRepositoryGateway, never()).existsByUserTypeUuid(any());
        verify(userTypeRepositoryGateway, never()).deleteById(any());
    }

    @Test
    void shouldThrowExceptionWhenUserTypeInUse() {
        // Arrange
        UserType userType = new UserType();
        userType.setName("BASIC");
        when(userTypeRepositoryGateway.findById(userTypeUuid)).thenReturn(Optional.of(userType));
        when(userRepositoryGateway.existsByUserTypeUuid(userTypeUuid)).thenReturn(true);

        // Act & Assert
        assertThrows(UserTypeInUseException.class,
            () -> deleteUserTypeUseCase.execute(userTypeUuid));

        verify(userTypeRepositoryGateway).findById(userTypeUuid);
        verify(userRepositoryGateway).existsByUserTypeUuid(userTypeUuid);
        verify(userTypeRepositoryGateway, never()).deleteById(any());
    }

    @Test
    void shouldThrowExceptionWhenDeletingCoreUserType() {
        // Arrange
        UserType userType = new UserType();
        userType.setName("ADMIN");
        when(userTypeRepositoryGateway.findById(userTypeUuid)).thenReturn(Optional.of(userType));

        // Act & Assert
        assertThrows(CoreUserTypeModificationException.class,
            () -> deleteUserTypeUseCase.execute(userTypeUuid));

        verify(userTypeRepositoryGateway).findById(userTypeUuid);
        verify(userRepositoryGateway, never()).existsByUserTypeUuid(any());
        verify(userTypeRepositoryGateway, never()).deleteById(any());
    }
}
