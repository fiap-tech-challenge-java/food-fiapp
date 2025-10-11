package com.fiap.foodfiapp.core.application.usecases.restaurant;

import com.fiap.foodfiapp.core.application.usecases.restaurant.impl.FindAllRestaurantsByUserIdUseCaseImpl;
import com.fiap.foodfiapp.core.domain.entity.Restaurant;
import com.fiap.foodfiapp.core.domain.port.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FindAllRestaurantsByUserIdUseCaseTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    private FindAllRestaurantsByUserIdUseCaseImpl findAllRestaurantsByUserIdUseCase;

    private UUID userId;
    private Restaurant restaurant1;
    private Restaurant restaurant2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        findAllRestaurantsByUserIdUseCase = new FindAllRestaurantsByUserIdUseCaseImpl(restaurantRepository);

        userId = UUID.randomUUID();

        // Create test restaurants
        restaurant1 = new Restaurant();
        restaurant1.setId(UUID.randomUUID());
        restaurant1.setName("Restaurant 1");
        restaurant1.setUserOwnerId(userId);

        restaurant2 = new Restaurant();
        restaurant2.setId(UUID.randomUUID());
        restaurant2.setName("Restaurant 2");
        restaurant2.setUserOwnerId(userId);
    }

    @Test
    void shouldReturnListOfRestaurantsForValidUserId() {
        // Arrange
        List<Restaurant> expectedRestaurants = Arrays.asList(restaurant1, restaurant2);
        when(restaurantRepository.findAllByUserId(userId)).thenReturn(expectedRestaurants);

        // Act
        List<Restaurant> result = findAllRestaurantsByUserIdUseCase.execute(userId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(restaurant1));
        assertTrue(result.contains(restaurant2));
        verify(restaurantRepository).findAllByUserId(userId);
    }

    @Test
    void shouldReturnEmptyListWhenNoRestaurantsFound() {
        // Arrange
        when(restaurantRepository.findAllByUserId(userId)).thenReturn(Collections.emptyList());

        // Act
        List<Restaurant> result = findAllRestaurantsByUserIdUseCase.execute(userId);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(restaurantRepository).findAllByUserId(userId);
    }

    @Test
    void shouldReturnOnlyRestaurantsForSpecifiedUser() {
        // Arrange
        UUID otherUserId = UUID.randomUUID();
        Restaurant otherUserRestaurant = new Restaurant();
        otherUserRestaurant.setId(UUID.randomUUID());
        otherUserRestaurant.setName("Other User's Restaurant");
        otherUserRestaurant.setUserOwnerId(otherUserId);

        when(restaurantRepository.findAllByUserId(userId)).thenReturn(Collections.singletonList(restaurant1));

        // Act
        List<Restaurant> result = findAllRestaurantsByUserIdUseCase.execute(userId);

        // Assert
        assertEquals(1, result.size());
        assertEquals(restaurant1, result.get(0));
        verify(restaurantRepository).findAllByUserId(userId);
    }

    @Test
    void shouldHandleNullUserId() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, 
            () -> findAllRestaurantsByUserIdUseCase.execute(null));
        verify(restaurantRepository, never()).findAllByUserId(any());
    }

    @Test
    void shouldReturnUnmodifiableList() {
        // Arrange
        when(restaurantRepository.findAllByUserId(userId))
            .thenReturn(Collections.singletonList(restaurant1));

        // Act
        List<Restaurant> result = findAllRestaurantsByUserIdUseCase.execute(userId);

        // Assert
        assertThrows(UnsupportedOperationException.class, 
            () -> result.add(new Restaurant()));
    }
}
