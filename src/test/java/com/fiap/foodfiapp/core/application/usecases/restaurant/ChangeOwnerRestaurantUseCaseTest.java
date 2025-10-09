package com.fiap.foodfiapp.core.application.usecases.restaurant;

import com.fiap.foodfiapp.core.application.usecases.restaurant.impl.ChangeOwnerRestaurantUseCaseImpl;
import com.fiap.foodfiapp.core.domain.entity.Restaurant;
import com.fiap.foodfiapp.core.domain.port.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChangeOwnerRestaurantUseCaseTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private ChangeOwnerRestaurantUseCaseImpl changeOwnerRestaurantUseCase;

    private UUID restaurantId;
    private UUID newOwnerId;
    private Restaurant testRestaurant;

    @BeforeEach
    void setUp() {
        restaurantId = UUID.randomUUID();
        newOwnerId = UUID.randomUUID();
        
        testRestaurant = new Restaurant(
            restaurantId,
            "Test Restaurant",
            "Italian",
            "09:00-22:00",
            UUID.randomUUID(), // Original owner
            "Test Restaurant Description",
            null // address can be null for this test
        );
    }

    @Test
    void shouldChangeRestaurantOwnerSuccessfully() {
        // Arrange
        Restaurant updatedRestaurant = new Restaurant(
            restaurantId,
            "Test Restaurant",
            "Italian",
            "09:00-22:00",
            newOwnerId, // New owner
            "Test Restaurant Description",
            null // address can be null for this test
        );

        when(restaurantRepository.changeOwner(restaurantId, newOwnerId)).thenReturn(updatedRestaurant);
        
        // Act
        Restaurant result = changeOwnerRestaurantUseCase.execute(restaurantId, newOwnerId);
        
        // Assert
        assertNotNull(result);
        assertEquals(newOwnerId, result.getUserOwnerId());
        assertEquals(restaurantId, result.getId());
        verify(restaurantRepository).changeOwner(restaurantId, newOwnerId);
    }

    @Test
    void shouldHandleNullRestaurantId() {
        // Act & Assert
        assertThrows(
            NullPointerException.class,
            () -> changeOwnerRestaurantUseCase.execute(null, newOwnerId)
        );
        
        verify(restaurantRepository, never()).changeOwner(any(), any());
    }

    @Test
    void shouldHandleNullNewOwnerId() {
        // Act & Assert
        assertThrows(
            NullPointerException.class,
            () -> changeOwnerRestaurantUseCase.execute(restaurantId, null)
        );
        
        verify(restaurantRepository, never()).changeOwner(any(), any());
    }

    @Test
    void shouldHandleRepositoryReturningNull() {
        // Arrange
        when(restaurantRepository.changeOwner(any(), any())).thenReturn(null);
        
        // Act
        Restaurant result = changeOwnerRestaurantUseCase.execute(restaurantId, newOwnerId);
        
        // Assert
        assertNull(result);
        verify(restaurantRepository).changeOwner(restaurantId, newOwnerId);
    }

    @Test
    void shouldHandleRepositoryException() {
        // Arrange
        when(restaurantRepository.changeOwner(any(), any()))
            .thenThrow(new RuntimeException("Database error"));
        
        // Act & Assert
        assertThrows(
            RuntimeException.class,
            () -> changeOwnerRestaurantUseCase.execute(restaurantId, newOwnerId),
            "Database error"
        );
    }

    @Test
    void shouldNotChangeOtherRestaurantFields() {
        // Arrange
        Restaurant updatedRestaurant = new Restaurant(
            restaurantId,
            "Test Restaurant",
            "Italian",
            "09:00-22:00",
            newOwnerId,
            "Test Restaurant Description",
            null
        );
        
        when(restaurantRepository.changeOwner(restaurantId, newOwnerId)).thenReturn(updatedRestaurant);
        
        // Act
        Restaurant result = changeOwnerRestaurantUseCase.execute(restaurantId, newOwnerId);
        
        // Assert
        assertNotNull(result);
        assertEquals("Test Restaurant", result.getName());
        assertEquals("Italian", result.getCuisineType());
        assertEquals("09:00-22:00", result.getOpeningHours());
        assertEquals("Test Restaurant Description", result.getDescription());
        assertNull(result.getAddress());
    }

    @Test
    void shouldHandleSameOwnerId() {
        // Arrange - Trying to change to the same owner
        Restaurant updatedRestaurant = new Restaurant();
        updatedRestaurant.setId(restaurantId);
        updatedRestaurant.setUserOwnerId(testRestaurant.getUserOwnerId());
        
        when(restaurantRepository.changeOwner(restaurantId, testRestaurant.getUserOwnerId()))
            .thenReturn(updatedRestaurant);
        
        // Act
        Restaurant result = changeOwnerRestaurantUseCase.execute(restaurantId, testRestaurant.getUserOwnerId());
        
        // Assert - Should still call the repository even if owner is the same
        assertNotNull(result);
        assertEquals(testRestaurant.getUserOwnerId(), result.getUserOwnerId());
        verify(restaurantRepository).changeOwner(restaurantId, testRestaurant.getUserOwnerId());
    }

    @Test
    void shouldHandleMultipleCalls() {
        // Arrange
        UUID secondRestaurantId = UUID.randomUUID();
        UUID secondNewOwnerId = UUID.randomUUID();
        
        Restaurant firstResult = new Restaurant();
        firstResult.setId(restaurantId);
        firstResult.setUserOwnerId(newOwnerId);
        
        Restaurant secondResult = new Restaurant();
        secondResult.setId(secondRestaurantId);
        secondResult.setUserOwnerId(secondNewOwnerId);
        
        when(restaurantRepository.changeOwner(restaurantId, newOwnerId)).thenReturn(firstResult);
        when(restaurantRepository.changeOwner(secondRestaurantId, secondNewOwnerId)).thenReturn(secondResult);
        
        // Act
        Restaurant result1 = changeOwnerRestaurantUseCase.execute(restaurantId, newOwnerId);
        Restaurant result2 = changeOwnerRestaurantUseCase.execute(secondRestaurantId, secondNewOwnerId);
        
        // Assert
        assertEquals(newOwnerId, result1.getUserOwnerId());
        assertEquals(secondNewOwnerId, result2.getUserOwnerId());
        
        verify(restaurantRepository).changeOwner(restaurantId, newOwnerId);
        verify(restaurantRepository).changeOwner(secondRestaurantId, secondNewOwnerId);
    }
}
