package com.fiap.foodfiapp.core.application.usecases.restaurant;

import com.fiap.foodfiapp.core.application.usecases.restaurant.impl.FindAllPublicRestaurantsUseCaseImpl;
import com.fiap.foodfiapp.core.domain.entity.Addresses;
import com.fiap.foodfiapp.core.domain.entity.Restaurant;
import com.fiap.foodfiapp.core.domain.port.RestaurantRepository;
import com.fiap.foodfiapp.core.domain.port.MenuItemRepository;
import com.fiap.foodfiapp.core.domain.port.AddressRepository;
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
class FindAllPublicRestaurantsUseCaseTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private MenuItemRepository menuItemRepository;

    @Mock
    private AddressRepository addressRepository;

    @InjectMocks
    private FindAllPublicRestaurantsUseCaseImpl findAllPublicRestaurantsUseCase;

    private List<Restaurant> testRestaurants;

    @BeforeEach
    void setUp() {
        // Setup test restaurants
        Restaurant restaurant1 = new Restaurant(
            UUID.randomUUID(),
            "Restaurant 1",
            "Cuisine 1",
            "09:00-22:00",
            UUID.randomUUID(),
            "Description 1",
            null
        );
        
        Restaurant restaurant2 = new Restaurant(
            UUID.randomUUID(),
            "Restaurant 2",
            "Cuisine 2",
            "10:00-23:00",
            UUID.randomUUID(),
            "Description 2",
            null
        );
        
        testRestaurants = Arrays.asList(restaurant1, restaurant2);
    }

    @Test
    void shouldReturnAllActiveRestaurants() {
        // Arrange
        UUID userId = UUID.randomUUID();
        when(restaurantRepository.findAllActive()).thenReturn(testRestaurants);
        when(menuItemRepository.findAllByRestaurantId(any())).thenReturn(Collections.emptyList());
        when(addressRepository.findByOwner(any(), anyString())).thenReturn(Collections.emptyList());
        
        // Act
        List<Restaurant> result = findAllPublicRestaurantsUseCase.execute(userId);
        
        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Restaurant 1", result.get(0).getName());
        assertEquals("Restaurant 2", result.get(1).getName());
        verify(restaurantRepository).findAllActive();
    }

    @Test
    void shouldReturnEmptyListWhenNoActiveRestaurants() {
        // Arrange
        UUID userId = UUID.randomUUID();
        when(restaurantRepository.findAllActive()).thenReturn(Collections.emptyList());
        
        // Act
        List<Restaurant> result = findAllPublicRestaurantsUseCase.execute(userId);
        
        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(restaurantRepository).findAllActive();
    }

    @Test
    void shouldReturnUnmodifiableList() {
        // Arrange
        UUID userId = UUID.randomUUID();
        when(restaurantRepository.findAllActive()).thenReturn(testRestaurants);
        when(menuItemRepository.findAllByRestaurantId(any())).thenReturn(Collections.emptyList());
        when(addressRepository.findByOwner(any(), anyString())).thenReturn(Collections.emptyList());
        
        // Act
        List<Restaurant> result = findAllPublicRestaurantsUseCase.execute(userId);
        
        // Assert
        assertThrows(UnsupportedOperationException.class, 
            () -> result.add(new Restaurant()));
    }

    @Test
    void shouldHandleRepositoryException() {
        // Arrange
        UUID userId = UUID.randomUUID();
        when(restaurantRepository.findAllActive()).thenThrow(new RuntimeException("Database error"));
        
        // Act & Assert
        Exception exception = assertThrows(
            RuntimeException.class,
            () -> findAllPublicRestaurantsUseCase.execute(userId)
        );
        assertEquals("Database error", exception.getMessage());
    }

    @Test
    void shouldReturnRestaurantsInOriginalOrder() {
        // Arrange
        UUID userId = UUID.randomUUID();
        when(restaurantRepository.findAllActive()).thenReturn(testRestaurants);
        when(menuItemRepository.findAllByRestaurantId(any())).thenReturn(Collections.emptyList());
        when(addressRepository.findByOwner(any(), anyString())).thenReturn(Collections.emptyList());
        
        // Act
        List<Restaurant> result = findAllPublicRestaurantsUseCase.execute(userId);
        
        // Assert
        assertEquals(2, result.size());
        assertEquals("Restaurant 1", result.get(0).getName());
        assertEquals("Restaurant 2", result.get(1).getName());
    }

    @Test
    void shouldReturnAllActiveRestaurantsFromRepository() {
        // Arrange
        UUID userId = UUID.randomUUID();
        when(restaurantRepository.findAllActive()).thenReturn(Collections.singletonList(testRestaurants.get(1)));
        when(menuItemRepository.findAllByRestaurantId(any())).thenReturn(Collections.emptyList());
        when(addressRepository.findByOwner(any(), anyString())).thenReturn(Collections.emptyList());
        
        // Act
        List<Restaurant> result = findAllPublicRestaurantsUseCase.execute(userId);
        
        // Assert
        assertEquals(1, result.size());
        assertEquals("Restaurant 2", result.get(0).getName());
    }

    @Test
    void shouldReturnCopyOfRestaurantList() {
        // Arrange
        UUID userId = UUID.randomUUID();
        when(restaurantRepository.findAllActive()).thenReturn(testRestaurants);
        when(menuItemRepository.findAllByRestaurantId(any())).thenReturn(Collections.emptyList());
        when(addressRepository.findByOwner(any(), anyString())).thenReturn(Collections.emptyList());
        
        // Act
        List<Restaurant> result1 = findAllPublicRestaurantsUseCase.execute(userId);
        List<Restaurant> result2 = findAllPublicRestaurantsUseCase.execute(userId);
        
        // Assert - Should return new list instances
        assertNotSame(result1, result2);
        assertEquals(result1, result2);
    }

    @Test
    void shouldHandleNullFieldsInRestaurants() {
        // Arrange
        UUID userId = UUID.randomUUID();
        Restaurant restaurantWithNullFields = new Restaurant(
            null,   // id
            null,   // name
            null,   // cuisine
            null,   // openingHours
            null,   // userOwnerId
            null,   // description
            null    // address
        );
        
        when(restaurantRepository.findAllActive()).thenReturn(Collections.singletonList(restaurantWithNullFields));
        when(menuItemRepository.findAllByRestaurantId(any())).thenReturn(Collections.emptyList());
        when(addressRepository.findByOwner(any(), anyString())).thenReturn(Collections.emptyList());
        
        // Act
        List<Restaurant> result = findAllPublicRestaurantsUseCase.execute(userId);
        
        // Assert
        assertEquals(1, result.size());
        assertNull(result.get(0).getId());
        assertNull(result.get(0).getName());
    }

    @Test
    void shouldHandleLargeNumberOfRestaurants() {
        // Arrange
        UUID userId = UUID.randomUUID();
        List<Restaurant> largeList = Collections.nCopies(1000, testRestaurants.get(0));
        when(restaurantRepository.findAllActive()).thenReturn(largeList);
        when(menuItemRepository.findAllByRestaurantId(any())).thenReturn(Collections.emptyList());
        when(addressRepository.findByOwner(any(), anyString())).thenReturn(Collections.emptyList());
        
        // Act
        List<Restaurant> result = findAllPublicRestaurantsUseCase.execute(userId);
        
        // Assert
        assertEquals(1000, result.size());
    }

    @Test
    void shouldHandleRestaurantsWithMultipleAddresses() {
        // Arrange
        UUID userId = UUID.randomUUID();
        Addresses address1 = new Addresses(UUID.randomUUID(), "Main St", "123", "Apt 1", "Downtown", "City", "ST", "12345678");
        Addresses address2 = new Addresses(UUID.randomUUID(), "Second St", "456", "Apt 2", "Uptown", "City", "ST", "87654321");
        List<Addresses> addresses = Arrays.asList(address1, address2);
        
        when(restaurantRepository.findAllActive()).thenReturn(testRestaurants);
        when(menuItemRepository.findAllByRestaurantId(any())).thenReturn(Collections.emptyList());
        when(addressRepository.findByOwner(any(), anyString())).thenReturn(addresses);
        
        // Act
        List<Restaurant> result = findAllPublicRestaurantsUseCase.execute(userId);
        
        // Assert
        assertEquals(2, result.size());
        assertNotNull(result.get(0).getAddress());
        assertEquals(address1, result.get(0).getAddress()); // Should use first address
    }
}
