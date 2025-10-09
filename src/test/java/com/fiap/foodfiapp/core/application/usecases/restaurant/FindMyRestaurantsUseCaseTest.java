package com.fiap.foodfiapp.core.application.usecases.restaurant;

import com.fiap.foodfiapp.core.application.usecases.restaurant.impl.FindMyRestaurantsUseCaseImpl;
import com.fiap.foodfiapp.core.domain.entity.Restaurant;
import com.fiap.foodfiapp.core.domain.port.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindMyRestaurantsUseCaseTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private FindMyRestaurantsUseCaseImpl findMyRestaurantsUseCase;

    private UUID ownerId;
    private Restaurant restaurant1;
    private Restaurant restaurant2;

    @BeforeEach
    void setUp() {
        ownerId = UUID.randomUUID();
        
        restaurant1 = new Restaurant(
            UUID.randomUUID(),
            "My Restaurant 1",
            "Italian",
            "09:00-22:00",
            ownerId,
            "Description 1",
            null
        );
        
        restaurant2 = new Restaurant(
            UUID.randomUUID(),
            "My Restaurant 2",
            "Japanese",
            "10:00-23:00",
            ownerId,
            "Description 2",
            null
        );
    }

    @Test
    void shouldReturnAllRestaurantsByOwnerId() {
        // Arrange
        List<Restaurant> expectedRestaurants = Arrays.asList(restaurant1, restaurant2);
        when(restaurantRepository.findAllByUserId(ownerId)).thenReturn(expectedRestaurants);

        // Act
        List<Restaurant> result = findMyRestaurantsUseCase.execute(ownerId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("My Restaurant 1", result.get(0).getName());
        assertEquals("My Restaurant 2", result.get(1).getName());
        verify(restaurantRepository).findAllByUserId(ownerId);
    }

    @Test
    void shouldReturnEmptyListWhenNoRestaurantsFound() {
        // Arrange
        when(restaurantRepository.findAllByUserId(ownerId)).thenReturn(Collections.emptyList());

        // Act
        List<Restaurant> result = findMyRestaurantsUseCase.execute(ownerId);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(restaurantRepository).findAllByUserId(ownerId);
    }

    @Test
    void shouldReturnOnlyRestaurantsForSpecifiedOwner() {
        // Arrange
        when(restaurantRepository.findAllByUserId(ownerId)).thenReturn(Collections.singletonList(restaurant1));

        // Act
        List<Restaurant> result = findMyRestaurantsUseCase.execute(ownerId);

        // Assert
        assertEquals(1, result.size());
        assertEquals(ownerId, result.get(0).getUserOwnerId());
        verify(restaurantRepository).findAllByUserId(ownerId);
    }

    @Test
    void shouldHandleMultipleRestaurants() {
        // Arrange
        List<Restaurant> manyRestaurants = Arrays.asList(restaurant1, restaurant2, restaurant1, restaurant2);
        when(restaurantRepository.findAllByUserId(ownerId)).thenReturn(manyRestaurants);

        // Act
        List<Restaurant> result = findMyRestaurantsUseCase.execute(ownerId);

        // Assert
        assertEquals(4, result.size());
        verify(restaurantRepository).findAllByUserId(ownerId);
    }

    @Test
    void shouldCallRepositoryWithCorrectOwnerId() {
        // Arrange
        UUID differentOwnerId = UUID.randomUUID();
        when(restaurantRepository.findAllByUserId(differentOwnerId)).thenReturn(Collections.emptyList());

        // Act
        findMyRestaurantsUseCase.execute(differentOwnerId);

        // Assert
        verify(restaurantRepository).findAllByUserId(differentOwnerId);
        verify(restaurantRepository, never()).findAllByUserId(ownerId);
    }
}
