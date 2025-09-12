package com.fiap.foodfiapp.core.application.usecases.usertype;

import com.fiap.foodfiapp.core.application.gateways.UserRepositoryGateway;
import com.fiap.foodfiapp.core.application.gateways.UserTypeRepositoryGateway;
import com.fiap.foodfiapp.core.domain.exception.BusinessException;
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

    private DeleteUserTypeUseCase deleteUserTypeUseCase;

    private UUID userTypeUuid;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        deleteUserTypeUseCase = new DeleteUserTypeUseCase(userTypeRepositoryGateway, userRepositoryGateway);
        userTypeUuid = UUID.randomUUID();
    }

    @Test
    void shouldDeleteUserTypeWhenNotInUse() {
        // Arrange
        when(userTypeRepositoryGateway.findById(userTypeUuid)).thenReturn(Optional.of(new com.fiap.foodfiapp.core.domain.entity.UserType()));
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
        BusinessException exception = assertThrows(BusinessException.class,
            () -> deleteUserTypeUseCase.execute(userTypeUuid));

        assertEquals("User type not found.", exception.getMessage());
        verify(userTypeRepositoryGateway).findById(userTypeUuid);
        verify(userRepositoryGateway, never()).existsByUserTypeUuid(any());
        verify(userTypeRepositoryGateway, never()).deleteById(any());
    }

    @Test
    void shouldThrowExceptionWhenUserTypeInUse() {
        // Arrange
        when(userTypeRepositoryGateway.findById(userTypeUuid)).thenReturn(Optional.of(new com.fiap.foodfiapp.core.domain.entity.UserType()));
        when(userRepositoryGateway.existsByUserTypeUuid(userTypeUuid)).thenReturn(true);

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
            () -> deleteUserTypeUseCase.execute(userTypeUuid));

        assertEquals("Cannot delete user type that is being used by users.", exception.getMessage());
        verify(userTypeRepositoryGateway).findById(userTypeUuid);
        verify(userRepositoryGateway).existsByUserTypeUuid(userTypeUuid);
        verify(userTypeRepositoryGateway, never()).deleteById(any());
    }
}
