package com.fiap.foodfiapp.core.application.usecases.usertype;

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

class CreateUserTypeUseCaseTest {

    @Mock
    private UserTypeRepositoryGateway userTypeRepositoryGateway;

    private CreateUserTypeUseCase createUserTypeUseCase;

    private UserType userType;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        createUserTypeUseCase = new CreateUserTypeUseCase(userTypeRepositoryGateway);

        userType = new UserType();
        userType.setName("Customer");
    }

    @Test
    void shouldCreateUserTypeWhenNameDoesNotExist() {
        // Arrange
        when(userTypeRepositoryGateway.findByName("Customer")).thenReturn(Optional.empty());
        when(userTypeRepositoryGateway.save(any(UserType.class))).thenReturn(userType);

        // Act
        UserType result = createUserTypeUseCase.execute(userType);

        // Assert
        assertNotNull(result);
        assertNotNull(userType.getUuid()); // UUID should be generated
        verify(userTypeRepositoryGateway).findByName("Customer");
        verify(userTypeRepositoryGateway).save(userType);
    }

    @Test
    void shouldThrowExceptionWhenNameAlreadyExists() {
        // Arrange
        UserType existingUserType = new UserType();
        existingUserType.setUuid(UUID.randomUUID());
        existingUserType.setName("Customer");

        when(userTypeRepositoryGateway.findByName("Customer")).thenReturn(Optional.of(existingUserType));

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
            () -> createUserTypeUseCase.execute(userType));

        assertEquals("User type with this name already exists.", exception.getMessage());
        verify(userTypeRepositoryGateway).findByName("Customer");
        verify(userTypeRepositoryGateway, never()).save(any(UserType.class));
    }

    @Test
    void shouldPreserveUuidIfProvided() {
        // Arrange
        UUID providedUuid = UUID.randomUUID();
        userType.setUuid(providedUuid);

        when(userTypeRepositoryGateway.findByName("Customer")).thenReturn(Optional.empty());
        when(userTypeRepositoryGateway.save(any(UserType.class))).thenReturn(userType);

        // Act
        UserType result = createUserTypeUseCase.execute(userType);

        // Assert
        assertNotNull(result);
        assertEquals(providedUuid, userType.getUuid());
        verify(userTypeRepositoryGateway).save(userType);
    }
}
