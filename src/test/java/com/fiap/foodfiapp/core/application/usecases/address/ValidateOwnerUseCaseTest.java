package com.fiap.foodfiapp.core.application.usecases.address;

import com.fiap.foodfiapp.core.application.usecases.addresses.impl.ValidateOwnerUseCaseImpl;
import com.fiap.foodfiapp.core.domain.entity.Restaurant;
import com.fiap.foodfiapp.core.domain.entity.User;
import com.fiap.foodfiapp.core.domain.entity.UserType;
import com.fiap.foodfiapp.core.domain.enums.AddressOwnerTypeEnum;
import com.fiap.foodfiapp.core.domain.exception.BusinessException;
import com.fiap.foodfiapp.core.domain.exception.UnauthorizedAccessException;
import com.fiap.foodfiapp.core.domain.exception.UserNotFoundException;
import com.fiap.foodfiapp.core.domain.port.RestaurantRepository;
import com.fiap.foodfiapp.core.domain.port.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ValidateOwnerUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private ValidateOwnerUseCaseImpl validateOwnerUseCase;

    private UUID ownerId;
    private User validUser;
    private Restaurant validRestaurant;
    private UserType customerType;

    @BeforeEach
    void setUp() {
        ownerId = UUID.randomUUID();
        
        customerType = new UserType(UUID.randomUUID(), "CUSTOMER");
        
        validUser = new User(
            ownerId,
            "John Doe",
            "john@example.com",
            "john123",
            "12345678900",
            null,
            customerType,
            true,
            null,
            null,
            "password123"
        );
        
        validRestaurant = new Restaurant(
            ownerId,
            "Test Restaurant",
            "Italian",
            "09:00-22:00",
            UUID.randomUUID(),
            "Test Description",
            null
        );
    }

    @Test
    void shouldValidateUserOwnerSuccessfully() {
        // Arrange
        when(userRepository.findById(ownerId)).thenReturn(Optional.of(validUser));

        // Act & Assert
        assertDoesNotThrow(() -> validateOwnerUseCase.execute(ownerId, AddressOwnerTypeEnum.USER.getDescription()));
        verify(userRepository).findById(ownerId);
        verify(restaurantRepository, never()).findById(any());
    }

    @Test
    void shouldValidateRestaurantOwnerSuccessfully() {
        // Arrange
        when(restaurantRepository.findById(ownerId)).thenReturn(validRestaurant);

        // Act & Assert
        assertDoesNotThrow(() -> validateOwnerUseCase.execute(ownerId, AddressOwnerTypeEnum.RESTAURANT.getDescription()));
        verify(restaurantRepository).findById(ownerId);
        verify(userRepository, never()).findById(any());
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        // Arrange
        when(userRepository.findById(ownerId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(
            UserNotFoundException.class,
            () -> validateOwnerUseCase.execute(ownerId, AddressOwnerTypeEnum.USER.getDescription())
        );
        verify(userRepository).findById(ownerId);
    }

    @Test
    void shouldThrowExceptionWhenUserIsInactive() {
        // Arrange
        validUser.setIsActive(false);
        when(userRepository.findById(ownerId)).thenReturn(Optional.of(validUser));

        // Act & Assert
        assertThrows(
            UserNotFoundException.class,
            () -> validateOwnerUseCase.execute(ownerId, AddressOwnerTypeEnum.USER.getDescription())
        );
    }

    @Test
    void shouldThrowExceptionWhenUserIsNotCustomer() {
        // Arrange
        UserType ownerType = new UserType(UUID.randomUUID(), "OWNER");
        validUser.setUserType(ownerType);
        when(userRepository.findById(ownerId)).thenReturn(Optional.of(validUser));

        // Act & Assert
        assertThrows(
            UnauthorizedAccessException.class,
            () -> validateOwnerUseCase.execute(ownerId, AddressOwnerTypeEnum.USER.getDescription())
        );
    }

    @Test
    void shouldThrowExceptionWhenUserHasNoUserType() {
        // Arrange
        validUser.setUserType(null);
        when(userRepository.findById(ownerId)).thenReturn(Optional.of(validUser));

        // Act & Assert
        assertThrows(
            UnauthorizedAccessException.class,
            () -> validateOwnerUseCase.execute(ownerId, AddressOwnerTypeEnum.USER.getDescription())
        );
    }

    @Test
    void shouldThrowExceptionWhenRestaurantNotFound() {
        // Arrange
        when(restaurantRepository.findById(ownerId)).thenReturn(null);

        // Act & Assert
        assertThrows(
            BusinessException.class,
            () -> validateOwnerUseCase.execute(ownerId, AddressOwnerTypeEnum.RESTAURANT.getDescription())
        );
        verify(restaurantRepository).findById(ownerId);
    }

    @Test
    void shouldThrowExceptionWhenOwnerTypeIsInvalid() {
        // Act & Assert
        assertThrows(
            IllegalArgumentException.class,
            () -> validateOwnerUseCase.execute(ownerId, "INVALID_TYPE")
        );
        verify(userRepository, never()).findById(any());
        verify(restaurantRepository, never()).findById(any());
    }

    @Test
    void shouldThrowExceptionWhenOwnerTypeIsNull() {
        // Act & Assert
        assertThrows(
            IllegalArgumentException.class,
            () -> validateOwnerUseCase.execute(ownerId, null)
        );
    }

    @Test
    void shouldThrowExceptionWhenOwnerTypeIsEmpty() {
        // Act & Assert
        assertThrows(
            IllegalArgumentException.class,
            () -> validateOwnerUseCase.execute(ownerId, "")
        );
    }
}
