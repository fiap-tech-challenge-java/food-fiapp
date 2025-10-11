package com.fiap.foodfiapp.core.application.usecases.restaurant;

import com.fiap.foodfiapp.core.application.usecases.restaurant.impl.FindRestaurantByNameUseCaseImpl;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindRestaurantByNameUseCaseTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private FindRestaurantByNameUseCaseImpl findRestaurantByNameUseCase;

    private String restaurantName;
    private UUID userId;
    private Restaurant testRestaurant;

    @BeforeEach
    void setUp() {
        restaurantName = "Test Restaurant";
        userId = UUID.randomUUID();
        
        testRestaurant = new Restaurant(
            UUID.randomUUID(),  // id
            restaurantName,     // name
            "ITALIAN",         // cuisineType
            "09:00-22:00",    // openingHours
            userId,            // userOwnerId
            "Test description", // description
            null               // address
        );
    }

    @Test
    void shouldFindRestaurantByNameAndUserId() {
        // Arrange
        when(restaurantRepository.findByNameAndUser(restaurantName, userId)).thenReturn(testRestaurant);

        // Act
        Restaurant result = findRestaurantByNameUseCase.execute(restaurantName, userId);

        // Assert
        assertNotNull(result);
        assertEquals(restaurantName, result.getName());
        assertEquals(userId, result.getUserOwnerId());
        verify(restaurantRepository).findByNameAndUser(restaurantName, userId);
    }

    @Test
    void shouldReturnNullWhenRestaurantNotFound() {
        // Arrange
        when(restaurantRepository.findByNameAndUser(anyString(), any())).thenReturn(null);

        // Act
        Restaurant result = findRestaurantByNameUseCase.execute("Non-existent Restaurant", userId);

        // Assert
        assertNull(result);
        verify(restaurantRepository).findByNameAndUser("Non-existent Restaurant", userId);
    }

    @Test
    void shouldHandleCaseSensitiveSearch() {
        // Arrange
        when(restaurantRepository.findByNameAndUser(anyString(), any()))
            .thenAnswer(invocation -> 
                invocation.getArgument(0).equals(restaurantName) ? testRestaurant : null);

        // Act
        Restaurant result1 = findRestaurantByNameUseCase.execute(restaurantName.toLowerCase(), userId);
        Restaurant result2 = findRestaurantByNameUseCase.execute(restaurantName.toUpperCase(), userId);
        Restaurant result3 = findRestaurantByNameUseCase.execute(restaurantName, userId);

        // Assert
        assertNull(result1, "Should return null for case-sensitive non-matching name");
        assertNull(result2, "Should return null for case-sensitive non-matching name");
        assertNotNull(result3, "Should find with exact case match");
        
        verify(restaurantRepository, times(3)).findByNameAndUser(anyString(), eq(userId));
    }

    @Test
    void shouldHandleNullName() {
        // Act & Assert
        assertThrows(NullPointerException.class, 
            () -> findRestaurantByNameUseCase.execute(null, userId));
        
        verify(restaurantRepository, never()).findByNameAndUser(any(), any());
    }

    @Test
    void shouldHandleNullUserId() {
        // Act & Assert
        assertThrows(NullPointerException.class, 
            () -> findRestaurantByNameUseCase.execute(restaurantName, null));
        
        verify(restaurantRepository, never()).findByNameAndUser(any(), any());
    }

    @Test
    void shouldHandleEmptyName() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, 
            () -> findRestaurantByNameUseCase.execute("", userId));
        
        verify(restaurantRepository, never()).findByNameAndUser(any(), any());
    }

    @Test
    void shouldHandleWhitespaceName() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, 
            () -> findRestaurantByNameUseCase.execute("   ", userId));
        
        verify(restaurantRepository, never()).findByNameAndUser(any(), any());
    }

    @Test
    void shouldReturnRestaurantWithAllFields() {
        // Arrange
        // Recreate restaurant with all required fields
        testRestaurant = new Restaurant(
            testRestaurant.getId(),
            testRestaurant.getName(),
            "ITALIAN",
            testRestaurant.getOpeningHours(),
            userId,
            "Test Description",
            testRestaurant.getAddress()
        );
        
        when(restaurantRepository.findByNameAndUser(restaurantName, userId)).thenReturn(testRestaurant);

        // Act
        Restaurant result = findRestaurantByNameUseCase.execute(restaurantName, userId);

        // Assert
        assertNotNull(result);
        assertEquals("Test Description", result.getDescription());
        assertEquals("ITALIAN", result.getCuisineType());
        assertTrue(result.getIsActive());
        assertEquals(userId, result.getUserOwnerId());
    }

    @Test
    void shouldNotReturnRestaurantForDifferentUser() {
        // Arrange
        UUID otherUserId = UUID.randomUUID();
        when(restaurantRepository.findByNameAndUser(restaurantName, otherUserId)).thenReturn(null);

        // Act
        Restaurant result = findRestaurantByNameUseCase.execute(restaurantName, otherUserId);

        // Assert
        assertNull(result, "Should not find restaurant for different user");
        verify(restaurantRepository).findByNameAndUser(restaurantName, otherUserId);
    }

    @Test
    void shouldHandleSpecialCharactersInName() {
        // Arrange
        String nameWithSpecialChars = "Café & Bistrô S.A.";
        testRestaurant.setName(nameWithSpecialChars);
        when(restaurantRepository.findByNameAndUser(nameWithSpecialChars, userId)).thenReturn(testRestaurant);

        // Act
        Restaurant result = findRestaurantByNameUseCase.execute(nameWithSpecialChars, userId);

        // Assert
        assertNotNull(result);
        assertEquals(nameWithSpecialChars, result.getName());
        verify(restaurantRepository).findByNameAndUser(nameWithSpecialChars, userId);
    }
}
