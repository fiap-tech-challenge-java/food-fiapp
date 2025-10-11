package com.fiap.foodfiapp.core.application.usecases.usertype;

import com.fiap.foodfiapp.core.application.usecases.usertype.impl.DeleteUserTypeUseCaseImpl;
import com.fiap.foodfiapp.core.domain.entity.UserType;
import com.fiap.foodfiapp.core.domain.exception.CoreUserTypeModificationException;
import com.fiap.foodfiapp.core.domain.exception.UserTypeInUseException;
import com.fiap.foodfiapp.core.domain.exception.UserTypeNotFoundException;
import com.fiap.foodfiapp.core.domain.port.UserRepository;
import com.fiap.foodfiapp.core.domain.port.UserTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteUserTypeUseCaseTest {

    @Mock
    private UserTypeRepository userTypeRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private DeleteUserTypeUseCaseImpl deleteUserTypeUseCase;

    private UUID userTypeId;
    private UserType testUserType;

    @BeforeEach
    void setUp() {
        userTypeId = UUID.randomUUID();
        testUserType = new UserType();
        testUserType.setUuid(userTypeId);
        testUserType.setName("TEST_ROLE");
        testUserType.setActive(true);
    }

    @Test
    void shouldDeleteUserTypeSuccessfully() {
        // Arrange
        when(userTypeRepository.findById(userTypeId)).thenReturn(Optional.of(testUserType));
        when(userRepository.existsByUserTypeUuid(userTypeId)).thenReturn(false);
        doNothing().when(userTypeRepository).deleteById(userTypeId);

        // Act
        deleteUserTypeUseCase.execute(userTypeId);

        // Assert
        verify(userTypeRepository).findById(userTypeId);
        verify(userRepository).existsByUserTypeUuid(userTypeId);
        verify(userTypeRepository).deleteById(userTypeId);
    }

    @Test
    void shouldThrowExceptionWhenUserTypeNotFound() {
        // Arrange
        when(userTypeRepository.findById(userTypeId)).thenReturn(Optional.empty());

        // Act & Assert
        UserTypeNotFoundException exception = assertThrows(
            UserTypeNotFoundException.class,
            () -> deleteUserTypeUseCase.execute(userTypeId)
        );
        
        assertEquals("User type not found.", exception.getMessage());
        verify(userTypeRepository, never()).deleteById(any());
    }

    @Test
    void shouldThrowExceptionWhenDeletingCoreUserType() {
        // Arrange
        testUserType.setName("ADMIN");
        when(userTypeRepository.findById(userTypeId)).thenReturn(Optional.of(testUserType));

        // Act & Assert
        assertThrows(
            CoreUserTypeModificationException.class,
            () -> deleteUserTypeUseCase.execute(userTypeId)
        );
        
        verify(userRepository, never()).existsByUserTypeUuid(any());
        verify(userTypeRepository, never()).deleteById(any());
    }

    @Test
    void shouldThrowExceptionWhenUserTypeIsInUse() {
        // Arrange
        when(userTypeRepository.findById(userTypeId)).thenReturn(Optional.of(testUserType));
        when(userRepository.existsByUserTypeUuid(userTypeId)).thenReturn(true);

        // Act & Assert
        assertThrows(
            UserTypeInUseException.class,
            () -> deleteUserTypeUseCase.execute(userTypeId)
        );
        
        verify(userTypeRepository, never()).deleteById(any());
    }

    @Test
    void shouldHandleCaseInsensitiveCoreTypeCheck() {
        // Test with different cases for core type names
        String[] coreTypes = {"owner", "Owner", "CUSTOMER", "admin", "AdMiN"};
        
        for (String coreType : coreTypes) {
            // Arrange
            testUserType.setName(coreType);
            when(userTypeRepository.findById(userTypeId)).thenReturn(Optional.of(testUserType));

            // Act & Assert
            assertThrows(
                CoreUserTypeModificationException.class,
                () -> deleteUserTypeUseCase.execute(userTypeId),
                "Should throw for core type: " + coreType
            );
            
            verify(userRepository, never()).existsByUserTypeUuid(any());
            verify(userTypeRepository, never()).deleteById(any());
            
            // Reset mocks for next iteration
            reset(userTypeRepository, userRepository);
        }
    }

    @Test
    void shouldNotDeleteIfUserTypeIsNull() {
        // Act & Assert
        assertThrows(
            NullPointerException.class,
            () -> deleteUserTypeUseCase.execute(null)
        );
        
        verify(userTypeRepository, never()).findById(any());
        verify(userRepository, never()).existsByUserTypeUuid(any());
        verify(userTypeRepository, never()).deleteById(any());
    }

    @Test
    void shouldHandleRepositoryDeleteException() {
        // Arrange
        when(userTypeRepository.findById(userTypeId)).thenReturn(Optional.of(testUserType));
        when(userRepository.existsByUserTypeUuid(userTypeId)).thenReturn(false);
        doThrow(new RuntimeException("Database error")).when(userTypeRepository).deleteById(userTypeId);

        // Act & Assert
        assertThrows(
            RuntimeException.class,
            () -> deleteUserTypeUseCase.execute(userTypeId),
            "Database error"
        );
    }

    @Test
    void shouldNotDeleteIfUserTypeNameIsNull() {
        // Arrange
        testUserType.setName(null);
        when(userTypeRepository.findById(userTypeId)).thenReturn(Optional.of(testUserType));

        // Act & Assert - Should not throw for non-core type with null name
        assertDoesNotThrow(() -> deleteUserTypeUseCase.execute(userTypeId));
        
        verify(userRepository).existsByUserTypeUuid(userTypeId);
        verify(userTypeRepository).deleteById(userTypeId);
    }

    @Test
    void shouldHandleMultipleDeletions() {
        // Arrange
        UUID secondId = UUID.randomUUID();
        UserType secondUserType = new UserType();
        secondUserType.setUuid(secondId);
        secondUserType.setName("ANOTHER_ROLE");
        
        when(userTypeRepository.findById(userTypeId)).thenReturn(Optional.of(testUserType));
        when(userTypeRepository.findById(secondId)).thenReturn(Optional.of(secondUserType));
        when(userRepository.existsByUserTypeUuid(any())).thenReturn(false);
        
        // Act
        deleteUserTypeUseCase.execute(userTypeId);
        deleteUserTypeUseCase.execute(secondId);
        
        // Assert
        verify(userTypeRepository).deleteById(userTypeId);
        verify(userTypeRepository).deleteById(secondId);
        verify(userRepository, times(2)).existsByUserTypeUuid(any());
    }
}
