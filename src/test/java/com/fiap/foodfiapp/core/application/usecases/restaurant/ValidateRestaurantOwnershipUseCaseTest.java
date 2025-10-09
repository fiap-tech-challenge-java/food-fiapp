package com.fiap.foodfiapp.core.application.usecases.restaurant;

import com.fiap.foodfiapp.core.application.usecases.restaurant.impl.ValidateRestaurantOwnershipUseCaseImpl;
import com.fiap.foodfiapp.core.domain.entity.Restaurant;
import com.fiap.foodfiapp.core.domain.exception.RestaurantNotFoundException;
import com.fiap.foodfiapp.core.domain.port.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ValidateRestaurantOwnershipUseCaseTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private ValidateRestaurantOwnershipUseCaseImpl validateRestaurantOwnershipUseCase;

    private UUID userId;
    private UUID restaurantId;
    private Restaurant restaurant;

    @BeforeEach
    void setUp() {
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
        when(restaurantRepository.findById(restaurantId)).thenReturn(restaurant);

        // Act & Assert
        assertThrows(
            NullPointerException.class,
            () -> validateRestaurantOwnershipUseCase.execute(userId, restaurantId)
        );
    }
}
