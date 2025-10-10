package com.fiap.foodfiapp.core.application.usecases.restaurant;

import com.fiap.foodfiapp.core.application.usecases.restaurant.impl.ValidateRestaurantOwnershipUseCaseImpl;
import com.fiap.foodfiapp.core.domain.entity.Restaurant;
import com.fiap.foodfiapp.core.domain.entity.User;
import com.fiap.foodfiapp.core.domain.entity.UserType;
import com.fiap.foodfiapp.core.domain.exception.RestaurantNotFoundException;
import com.fiap.foodfiapp.core.domain.port.RestaurantRepository;
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
class ValidateRestaurantOwnershipUseCaseTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private UserRepository userRepository;

    private ValidateRestaurantOwnershipUseCaseImpl validateRestaurantOwnershipUseCase;

    private UUID userId;
    private UUID restaurantId;
    private Restaurant restaurant;

    @BeforeEach
    void setUp() {
        // Initialize the use case with all required dependencies
        validateRestaurantOwnershipUseCase = new ValidateRestaurantOwnershipUseCaseImpl(restaurantRepository, userRepository);
        
        userId = UUID.randomUUID();
        restaurantId = UUID.randomUUID();
        
        restaurant = new Restaurant(
            restaurantId,
            "Test Restaurant",
            "Italian",
            "09:00-22:00",
            userId,
            "Test Description",
            null
        );
    }

    @Test
    void shouldReturnTrueWhenUserIsOwner() {
        // Arrange
        // Mock user as non-admin owner
        User ownerUser = new User();
        ownerUser.setId(userId);
        UserType ownerType = new UserType();
        ownerType.setName("OWNER");
        ownerUser.setUserType(ownerType);
        when(userRepository.findById(userId)).thenReturn(Optional.of(ownerUser));
        
        when(restaurantRepository.findById(restaurantId)).thenReturn(restaurant);

        // Act
        boolean result = validateRestaurantOwnershipUseCase.execute(userId, restaurantId);

        // Assert
        assertTrue(result);
        verify(restaurantRepository).findById(restaurantId);
    }

    @Test
    void shouldReturnFalseWhenUserIsNotOwner() {
        // Arrange
        UUID differentUserId = UUID.randomUUID();
        
        // Mock user as non-admin, non-owner
        User differentUser = new User();
        differentUser.setId(differentUserId);
        UserType customerType = new UserType();
        customerType.setName("CUSTOMER");
        differentUser.setUserType(customerType);
        when(userRepository.findById(differentUserId)).thenReturn(Optional.of(differentUser));
        
        when(restaurantRepository.findById(restaurantId)).thenReturn(restaurant);

        // Act
        boolean result = validateRestaurantOwnershipUseCase.execute(differentUserId, restaurantId);

        // Assert
        assertFalse(result);
        verify(restaurantRepository).findById(restaurantId);
    }

    @Test
    void shouldThrowExceptionWhenRestaurantNotFound() {
        // Arrange
        // Mock user as non-admin
        User user = new User();
        user.setId(userId);
        UserType userType = new UserType();
        userType.setName("OWNER");
        user.setUserType(userType);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        
        when(restaurantRepository.findById(restaurantId)).thenReturn(null);

        // Act & Assert
        RestaurantNotFoundException exception = assertThrows(
            RestaurantNotFoundException.class,
            () -> validateRestaurantOwnershipUseCase.execute(userId, restaurantId)
        );
        
        assertTrue(exception.getMessage().contains(restaurantId.toString()));
        verify(restaurantRepository).findById(restaurantId);
    }

    @Test
    void shouldValidateOwnershipForDifferentRestaurants() {
        // Arrange
        UUID restaurant2Id = UUID.randomUUID();
        Restaurant restaurant2 = new Restaurant(
            restaurant2Id,
            "Another Restaurant",
            "Japanese",
            "10:00-23:00",
            userId,
            "Another Description",
            null
        );
        
        // Mock user as non-admin owner
        User ownerUser = new User();
        ownerUser.setId(userId);
        UserType ownerType = new UserType();
        ownerType.setName("OWNER");
        ownerUser.setUserType(ownerType);
        when(userRepository.findById(userId)).thenReturn(Optional.of(ownerUser));
        
        when(restaurantRepository.findById(restaurantId)).thenReturn(restaurant);
        when(restaurantRepository.findById(restaurant2Id)).thenReturn(restaurant2);

        // Act
        boolean result1 = validateRestaurantOwnershipUseCase.execute(userId, restaurantId);
        boolean result2 = validateRestaurantOwnershipUseCase.execute(userId, restaurant2Id);

        // Assert
        assertTrue(result1);
        assertTrue(result2);
        verify(restaurantRepository).findById(restaurantId);
        verify(restaurantRepository).findById(restaurant2Id);
    }

    @Test
    void shouldHandleNullUserOwnerId() {
        // Arrange
        restaurant.setUserOwnerId(null);
        
        // Mock user as non-admin
        User user = new User();
        user.setId(userId);
        UserType userType = new UserType();
        userType.setName("OWNER");
        user.setUserType(userType);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        
        when(restaurantRepository.findById(restaurantId)).thenReturn(restaurant);

        // Act & Assert
        assertThrows(
            NullPointerException.class,
            () -> validateRestaurantOwnershipUseCase.execute(userId, restaurantId)
        );
    }
}
