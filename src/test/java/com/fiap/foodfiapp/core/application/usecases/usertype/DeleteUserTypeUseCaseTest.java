package com.fiap.foodfiapp.core.application.usecases.usertype;

import com.fiap.foodfiapp.core.domain.port.UserRepository;
import com.fiap.foodfiapp.core.domain.port.UserTypeRepository;
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
    private UserTypeRepository userTypeRepository;

    @Mock
    private UserRepository userRepository;

    private DeleteUserTypeUseCaseImpl deleteUserTypeUseCase;

    private UUID userTypeUuid;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        deleteUserTypeUseCase = new DeleteUserTypeUseCaseImpl(userTypeRepository, userRepository);
        userTypeUuid = UUID.randomUUID();
    }

    @Test
    void shouldDeleteUserTypeWhenNotInUse() {
        // Arrange
        UserType userType = new UserType();
        userType.setName("BASIC");
        when(userTypeRepository.findById(userTypeUuid)).thenReturn(Optional.of(userType));
        when(userRepository.existsByUserTypeUuid(userTypeUuid)).thenReturn(false);

        // Act
        deleteUserTypeUseCase.execute(userTypeUuid);

        // Assert
        verify(userTypeRepository).findById(userTypeUuid);
        verify(userRepository).existsByUserTypeUuid(userTypeUuid);
        verify(userTypeRepository).deleteById(userTypeUuid);
    }

    @Test
    void shouldThrowExceptionWhenUserTypeNotFound() {
        // Arrange
        when(userTypeRepository.findById(userTypeUuid)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserTypeNotFoundException.class,
            () -> deleteUserTypeUseCase.execute(userTypeUuid));

        verify(userTypeRepository).findById(userTypeUuid);
        verify(userRepository, never()).existsByUserTypeUuid(any());
        verify(userTypeRepository, never()).deleteById(any());
    }

    @Test
    void shouldThrowExceptionWhenUserTypeInUse() {
        // Arrange
        UserType userType = new UserType();
        userType.setName("BASIC");
        when(userTypeRepository.findById(userTypeUuid)).thenReturn(Optional.of(userType));
        when(userRepository.existsByUserTypeUuid(userTypeUuid)).thenReturn(true);

        // Act & Assert
        assertThrows(UserTypeInUseException.class,
            () -> deleteUserTypeUseCase.execute(userTypeUuid));

        verify(userTypeRepository).findById(userTypeUuid);
        verify(userRepository).existsByUserTypeUuid(userTypeUuid);
        verify(userTypeRepository, never()).deleteById(any());
    }

    @Test
    void shouldThrowExceptionWhenDeletingCoreUserType() {
        // Arrange
        UserType userType = new UserType();
        userType.setName("ADMIN");
        when(userTypeRepository.findById(userTypeUuid)).thenReturn(Optional.of(userType));

        // Act & Assert
        assertThrows(CoreUserTypeModificationException.class,
            () -> deleteUserTypeUseCase.execute(userTypeUuid));

        verify(userTypeRepository).findById(userTypeUuid);
        verify(userRepository, never()).existsByUserTypeUuid(any());
        verify(userTypeRepository, never()).deleteById(any());
    }
}
