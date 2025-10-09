package com.fiap.foodfiapp.core.application.usecases.restaurant;

import com.fiap.foodfiapp.core.application.usecases.restaurant.impl.UpdateRestaurantUseCaseImpl;
import com.fiap.foodfiapp.core.domain.entity.Restaurant;
import com.fiap.foodfiapp.core.domain.exception.BusinessException;
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
class UpdateRestaurantUseCaseTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private UpdateRestaurantUseCaseImpl updateRestaurantUseCase;

    private UUID restaurantId;
    private Restaurant existingRestaurant;
    private Restaurant updateData;

    @BeforeEach
    void setUp() {
        restaurantId = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();
        
        // Setup existing restaurant
        existingRestaurant = new Restaurant(
            restaurantId,
            "Original Name",
            "ITALIAN",
            "09:00-22:00",
            ownerId,
            "Original Description",
            null
        );
        
        // Setup update data with minimal required fields
        updateData = new Restaurant(
            null, // ID will be set by the use case
            null, // Name will be set in tests
            null, // Cuisine type will be set in tests
            null, // Opening hours will be set in tests
            null, // Owner ID will be set by the use case
            null, // Description will be set in tests
            null  // Address will be set in tests if needed
        );
    }

    @Test
    void shouldUpdateRestaurantName() {
        // Arrange
        UUID authenticatedUserId = existingRestaurant.getUserOwnerId();
        updateData.setName("Updated Name");
        when(restaurantRepository.findById(restaurantId)).thenReturn(existingRestaurant);
        when(restaurantRepository.update(any(Restaurant.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        // Act
        Restaurant result = updateRestaurantUseCase.execute(authenticatedUserId, restaurantId, updateData);
        
        // Assert
        assertNotNull(result);
        assertEquals("Updated Name", result.getName());
        assertEquals("ITALIAN", result.getCuisineType()); // Should remain unchanged
        verify(restaurantRepository).update(existingRestaurant);
    }

    @Test
    void shouldUpdateCuisineType() {
        // Arrange
        UUID authenticatedUserId = existingRestaurant.getUserOwnerId();
        updateData.setCuisineType("JAPANESE");
        when(restaurantRepository.findById(restaurantId)).thenReturn(existingRestaurant);
        when(restaurantRepository.update(any(Restaurant.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        // Act
        Restaurant result = updateRestaurantUseCase.execute(authenticatedUserId, restaurantId, updateData);
        
        // Assert
        assertEquals("JAPANESE", result.getCuisineType());
        assertEquals("Original Name", result.getName()); // Should remain unchanged
        verify(restaurantRepository).update(existingRestaurant);
    }

    @Test
    void shouldUpdateOpeningHours() {
        // Arrange
        UUID authenticatedUserId = existingRestaurant.getUserOwnerId();
        updateData.setOpeningHours("10:00-23:00");
        when(restaurantRepository.findById(restaurantId)).thenReturn(existingRestaurant);
        when(restaurantRepository.update(any(Restaurant.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        // Act
        Restaurant result = updateRestaurantUseCase.execute(authenticatedUserId, restaurantId, updateData);
        
        // Assert
        assertEquals("10:00-23:00", result.getOpeningHours());
        verify(restaurantRepository).update(existingRestaurant);
    }

    @Test
    void shouldUpdateMultipleFields() {
        // Arrange
        UUID authenticatedUserId = existingRestaurant.getUserOwnerId();
        updateData.setName("New Name");
        updateData.setCuisineType("MEXICAN");
        updateData.setOpeningHours("11:00-21:00");
        updateData.setDescription("Updated description");
        
        when(restaurantRepository.findById(restaurantId)).thenReturn(existingRestaurant);
        when(restaurantRepository.update(any(Restaurant.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        // Act
        Restaurant result = updateRestaurantUseCase.execute(authenticatedUserId, restaurantId, updateData);
        
        // Assert
        assertEquals("New Name", result.getName());
        assertEquals("MEXICAN", result.getCuisineType());
        assertEquals("11:00-21:00", result.getOpeningHours());
        assertEquals("Updated description", result.getDescription());
        verify(restaurantRepository).update(existingRestaurant);
    }

    @Test
    void shouldThrowExceptionWhenRestaurantNotFound() {
        // Arrange
        UUID authenticatedUserId = UUID.randomUUID();
        when(restaurantRepository.findById(restaurantId)).thenReturn(null);
        
        // Act & Assert
        BusinessException exception = assertThrows(
            BusinessException.class,
            () -> updateRestaurantUseCase.execute(authenticatedUserId, restaurantId, updateData)
        );
        
        assertEquals("Restaurante não encontrado.", exception.getMessage());
        verify(restaurantRepository, never()).update(any());
    }

    @Test
    void shouldNotAllowUpdateForUnauthorizedUser() {
        // Arrange
        UUID unauthorizedUserId = UUID.randomUUID(); // Different from the owner ID
        updateData.setName("Updated Name");
        when(restaurantRepository.findById(restaurantId)).thenReturn(existingRestaurant);
        
        // Act & Assert
        BusinessException exception = assertThrows(
            BusinessException.class,
            () -> updateRestaurantUseCase.execute(unauthorizedUserId, restaurantId, updateData)
        );
        
        assertEquals("Permissão negada. Você só pode editar seus próprios restaurantes.", exception.getMessage());
        verify(restaurantRepository, never()).update(any());
    }

    @Test
    void shouldHandleNullUpdateData() {
        // Arrange
        UUID authenticatedUserId = existingRestaurant.getUserOwnerId();
        
        // Act & Assert
        assertThrows(
            NullPointerException.class,
            () -> updateRestaurantUseCase.execute(authenticatedUserId, restaurantId, null)
        );
        
        verify(restaurantRepository, never()).update(any());
    }

    @Test
    void shouldHandleNullRestaurantId() {
        // Arrange
        UUID authenticatedUserId = existingRestaurant.getUserOwnerId();
        
        // Act & Assert
        assertThrows(
            NullPointerException.class,
            () -> updateRestaurantUseCase.execute(authenticatedUserId, null, updateData)
        );
        
        verify(restaurantRepository, never()).findById(any());
        verify(restaurantRepository, never()).update(any());
    }

    @Test
    void shouldNotUpdateWithNullFields() {
        // Arrange
        UUID authenticatedUserId = existingRestaurant.getUserOwnerId();
        updateData.setName(null);
        updateData.setCuisineType(null);
        updateData.setOpeningHours(null);
        updateData.setDescription(null);
        
        when(restaurantRepository.findById(restaurantId)).thenReturn(existingRestaurant);
        when(restaurantRepository.update(any(Restaurant.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        // Act
        Restaurant result = updateRestaurantUseCase.execute(authenticatedUserId, restaurantId, updateData);
        
        // Assert - Original values should be preserved
        assertEquals("Original Name", result.getName());
        assertEquals("ITALIAN", result.getCuisineType());
        assertEquals("09:00-22:00", result.getOpeningHours());
        assertEquals("Original Description", result.getDescription());
        verify(restaurantRepository).update(existingRestaurant);
    }

    @Test
    void shouldHandleEmptyStrings() {
        // Arrange
        UUID authenticatedUserId = existingRestaurant.getUserOwnerId();
        updateData.setName("");
        updateData.setCuisineType("");
        updateData.setOpeningHours("");
        updateData.setDescription("");
        
        when(restaurantRepository.findById(restaurantId)).thenReturn(existingRestaurant);
        when(restaurantRepository.update(any(Restaurant.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        // Act
        Restaurant result = updateRestaurantUseCase.execute(authenticatedUserId, restaurantId, updateData);
        
        // Assert - Empty strings should be treated as valid updates
        assertEquals("", result.getName());
        assertEquals("", result.getCuisineType());
        assertEquals("", result.getOpeningHours());
        assertEquals("", result.getDescription());
        verify(restaurantRepository).update(existingRestaurant);
    }

    @Test
    void shouldHandleRepositoryUpdateFailure() {
        // Arrange
        UUID authenticatedUserId = existingRestaurant.getUserOwnerId();
        updateData.setName("New Name");
        when(restaurantRepository.findById(restaurantId)).thenReturn(existingRestaurant);
        when(restaurantRepository.update(any(Restaurant.class))).thenThrow(new RuntimeException("Database error"));
        
        // Act & Assert
        assertThrows(
            RuntimeException.class,
            () -> updateRestaurantUseCase.execute(authenticatedUserId, restaurantId, updateData)
        );
        
        verify(restaurantRepository).update(existingRestaurant);
    }

    @Test
    void shouldNotUpdateImmutableFields() {
        // Arrange
        UUID newId = UUID.randomUUID();
        updateData.setId(newId);
        updateData.setUserOwnerId(UUID.randomUUID());
        
        when(restaurantRepository.findById(restaurantId)).thenReturn(existingRestaurant);
        when(restaurantRepository.update(any(Restaurant.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        // Act
        // Use the authenticated user ID as the restaurant owner ID for this test
        Restaurant result = updateRestaurantUseCase.execute(existingRestaurant.getUserOwnerId(), restaurantId, updateData);
        
        // Assert - Immutable fields should not be updated
        assertEquals(restaurantId, result.getId()); // ID should remain the same
        assertEquals(existingRestaurant.getUserOwnerId(), result.getUserOwnerId()); // Owner ID should remain unchanged
    }
}
