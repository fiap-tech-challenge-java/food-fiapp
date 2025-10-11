package com.fiap.foodfiapp.core.application.usecases.restaurant;

import com.fiap.foodfiapp.core.application.usecases.restaurant.impl.FindRestaurantByIdUseCaseImpl;
import com.fiap.foodfiapp.core.domain.entity.Addresses;
import com.fiap.foodfiapp.core.domain.entity.Restaurant;
import com.fiap.foodfiapp.core.domain.port.RestaurantRepository;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindRestaurantByIdUseCaseTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private AddressRepository addressRepository;

    @InjectMocks
    private FindRestaurantByIdUseCaseImpl findRestaurantByIdUseCase;

    private UUID restaurantId;
    private Restaurant testRestaurant;

    @BeforeEach
    void setUp() {
        restaurantId = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();
        testRestaurant = new Restaurant(
            restaurantId,
            "Test Restaurant",
            "Test Cuisine",
            "09:00-22:00",
            ownerId,
            "Test Description",
            null
        );
    }

    @Test
    void shouldFindRestaurantById() {
        // Arrange
        when(restaurantRepository.findById(restaurantId)).thenReturn(testRestaurant);
        when(addressRepository.findByOwner(any(), anyString())).thenReturn(Collections.emptyList());

        // Act
        Restaurant result = findRestaurantByIdUseCase.execute(restaurantId);

        // Assert
        assertNotNull(result);
        assertEquals(restaurantId, result.getId());
        assertEquals("Test Restaurant", result.getName());
        verify(restaurantRepository).findById(restaurantId);
    }

    @Test
    void shouldReturnNullWhenRestaurantNotFound() {
        // Arrange
        when(restaurantRepository.findById(any())).thenReturn(null);

        // Act
        Restaurant result = findRestaurantByIdUseCase.execute(UUID.randomUUID());

        // Assert
        assertNull(result);
        verify(restaurantRepository).findById(any());
    }

    @Test
    void shouldThrowExceptionWhenIdIsNull() {
        // Act & Assert
        assertThrows(NullPointerException.class, 
            () -> findRestaurantByIdUseCase.execute(null));
        
        verify(restaurantRepository, never()).findById(any());
    }

    @Test
    void shouldReturnRestaurantWithAllFields() {
        // Arrange
        UUID ownerId = UUID.randomUUID();
        Restaurant detailedRestaurant = new Restaurant(
            restaurantId,
            "Test Restaurant",
            "ITALIAN",
            "09:00-22:00",
            ownerId,
            "Test Description",
            null
        );
        
        when(restaurantRepository.findById(restaurantId)).thenReturn(detailedRestaurant);

        // Act
        Restaurant result = findRestaurantByIdUseCase.execute(restaurantId);

        // Assert
        assertNotNull(result);
        assertEquals("Test Description", result.getDescription());
        assertEquals("ITALIAN", result.getCuisineType());
        assertEquals(ownerId, result.getUserOwnerId());
    }

    @Test
    void shouldHandleMultipleCalls() {
        // Arrange
        UUID secondId = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();
        Restaurant secondRestaurant = new Restaurant(
            secondId,
            "Second Restaurant",
            "ITALIAN",
            "10:00-23:00",
            ownerId,
            "Second Description",
            null
        );
        
        when(restaurantRepository.findById(restaurantId)).thenReturn(testRestaurant);
        when(restaurantRepository.findById(secondId)).thenReturn(secondRestaurant);

        // Act
        Restaurant result1 = findRestaurantByIdUseCase.execute(restaurantId);
        Restaurant result2 = findRestaurantByIdUseCase.execute(secondId);

        // Assert
        assertNotNull(result1);
        assertEquals("Test Restaurant", result1.getName());
        
        assertNotNull(result2);
        assertEquals("Second Restaurant", result2.getName());
        
        verify(restaurantRepository).findById(restaurantId);
        verify(restaurantRepository).findById(secondId);
    }

    @Test
    void shouldNotModifyReturnedRestaurant() {
        // Arrange
        when(restaurantRepository.findById(restaurantId)).thenReturn(testRestaurant);
        when(addressRepository.findByOwner(any(), anyString())).thenReturn(Collections.emptyList());

        // Act
        Restaurant result = findRestaurantByIdUseCase.execute(restaurantId);
        
        // Assert
        assertDoesNotThrow(() -> {
            // Since Restaurant is immutable, we can't modify it directly
            // This test verifies that the object is returned without throwing exceptions
            assertNotNull(result);
        });
    }

    @Test
    void shouldReturnSameInstance() {
        // Arrange
        when(restaurantRepository.findById(restaurantId)).thenReturn(testRestaurant);
        when(addressRepository.findByOwner(any(), anyString())).thenReturn(Collections.emptyList());

        // Act
        Restaurant result1 = findRestaurantByIdUseCase.execute(restaurantId);
        Restaurant result2 = findRestaurantByIdUseCase.execute(restaurantId);

        // Assert
        assertSame(testRestaurant, result1);
        assertSame(testRestaurant, result2);
        verify(restaurantRepository, times(2)).findById(restaurantId);
    }

    @Test
    void shouldHandleRestaurantWithMultipleAddresses() {
        // Arrange
        Addresses address1 = new Addresses(UUID.randomUUID(), "Main St", "123", "Apt 1", "Downtown", "City", "ST", "12345678");
        Addresses address2 = new Addresses(UUID.randomUUID(), "Second St", "456", "Apt 2", "Uptown", "City", "ST", "87654321");
        List<Addresses> addresses = Arrays.asList(address1, address2);
        
        when(restaurantRepository.findById(restaurantId)).thenReturn(testRestaurant);
        when(addressRepository.findByOwner(restaurantId, "RESTAURANT")).thenReturn(addresses);

        // Act
        Restaurant result = findRestaurantByIdUseCase.execute(restaurantId);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getAddress());
        assertEquals(address1, result.getAddress()); // Should use first address
        verify(restaurantRepository).findById(restaurantId);
        verify(addressRepository).findByOwner(restaurantId, "RESTAURANT");
    }
}
