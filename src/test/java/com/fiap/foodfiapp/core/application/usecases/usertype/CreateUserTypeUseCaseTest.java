package com.fiap.foodfiapp.core.application.usecases.usertype;

import com.fiap.foodfiapp.core.domain.port.UserTypeRepository;
import com.fiap.foodfiapp.core.domain.entity.UserType;
import com.fiap.foodfiapp.core.domain.exception.UserTypeNameAlreadyExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CreateUserTypeUseCaseTest {

    @Mock
    private UserTypeRepository userTypeRepository;

    private CreateUserTypeUseCase createUserTypeUseCase;

    private UserType userType;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        createUserTypeUseCase = new CreateUserTypeUseCase(userTypeRepository);

        userType = new UserType();
        userType.setName("Customer");
    }

    @Test
    void shouldCreateUserTypeWhenNameDoesNotExist() {
        // Arrange
        when(userTypeRepository.findByName("Customer")).thenReturn(Optional.empty());
        when(userTypeRepository.save(any(UserType.class))).thenReturn(userType);

        // Act
        UserType result = createUserTypeUseCase.execute(userType);

        // Assert
        assertNotNull(result);
        assertNotNull(userType.getUuid()); // UUID should be generated
        verify(userTypeRepository).findByName("Customer");
        verify(userTypeRepository).save(userType);
    }

    @Test
    void shouldThrowExceptionWhenNameAlreadyExists() {
        // Arrange
        UserType existingUserType = new UserType();
        existingUserType.setUuid(UUID.randomUUID());
        existingUserType.setName("Customer");

        when(userTypeRepository.findByName("Customer")).thenReturn(Optional.of(existingUserType));

        // Act & Assert
        UserTypeNameAlreadyExistsException exception = assertThrows(UserTypeNameAlreadyExistsException.class,
            () -> createUserTypeUseCase.execute(userType));

        assertEquals("User type with name 'Customer' already exists.", exception.getMessage());
        verify(userTypeRepository).findByName("Customer");
        verify(userTypeRepository, never()).save(any(UserType.class));
    }

    @Test
    void shouldPreserveUuidIfProvided() {
        // Arrange
        UUID providedUuid = UUID.randomUUID();
        userType.setUuid(providedUuid);

        when(userTypeRepository.findByName("Customer")).thenReturn(Optional.empty());
        when(userTypeRepository.save(any(UserType.class))).thenReturn(userType);

        // Act
        UserType result = createUserTypeUseCase.execute(userType);

        // Assert
        assertNotNull(result);
        assertEquals(providedUuid, userType.getUuid());
        verify(userTypeRepository).save(userType);
    }
}
