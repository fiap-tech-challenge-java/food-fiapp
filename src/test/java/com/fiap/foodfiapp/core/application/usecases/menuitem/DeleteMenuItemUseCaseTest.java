package com.fiap.foodfiapp.core.application.usecases.menuitem;

import com.fiap.foodfiapp.core.application.usecases.menuitem.impl.DeleteMenuItemUseCaseImpl;
import com.fiap.foodfiapp.core.domain.entity.MenuItem;
import com.fiap.foodfiapp.core.domain.entity.Restaurant;
import com.fiap.foodfiapp.core.domain.entity.Addresses;
import com.fiap.foodfiapp.core.domain.exception.BusinessException;
import com.fiap.foodfiapp.core.domain.port.FileStorageRepository;
import com.fiap.foodfiapp.core.domain.port.MenuItemRepository;
import com.fiap.foodfiapp.core.domain.port.RestaurantRepository;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class DeleteMenuItemUseCaseTest {

    @Mock
    private MenuItemRepository menuItemRepository;

    @Mock
    private FileStorageRepository fileStorageRepository;
    
    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private DeleteMenuItemUseCaseImpl deleteMenuItemUseCase;

    private UUID menuItemId;
    private MenuItem testMenuItem;
    private UUID authenticatedUserId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        menuItemId = UUID.randomUUID();
        UUID restaurantId = UUID.randomUUID();
        authenticatedUserId = UUID.randomUUID();
        
        // Create a test restaurant with the correct constructor parameters
        Restaurant testRestaurant = new Restaurant(
            restaurantId,                    // id
            "Test Restaurant",               // name
            "Test Cuisine",                  // cuisineType
            "10:00-22:00",                   // openingHours
            authenticatedUserId,              // userOwnerId - must match authenticated user
            "Test Description",              // description
            null                             // address
        );
        
        // Set timestamps
        testRestaurant.setCreatedAt(java.time.OffsetDateTime.now());
        testRestaurant.setUpdatedAt(java.time.OffsetDateTime.now());
        
        // Create test menu item with all required parameters
        testMenuItem = new MenuItem(
            menuItemId,                      // id
            "Test Item",                     // name
            "Test Description",              // description
            10.0,                           // price
            false,                          // localOnly
            "http://example.com/photos/item1.jpg", // photoUrl
            restaurantId,                    // restaurantId
            java.time.OffsetDateTime.now(),  // createdAt
            java.time.OffsetDateTime.now()   // updatedAt
        );
        
        // Setup restaurant repository mock to return the test restaurant
        when(restaurantRepository.findById(any(UUID.class))).thenReturn(testRestaurant);
        
        // Setup menu item repository mock to return the test menu item
        when(menuItemRepository.findById(any(UUID.class))).thenReturn(Optional.of(testMenuItem));
    }

    @Test
    void shouldDeleteMenuItemWithoutPhoto() {
        // Arrange
        testMenuItem.setPhotoUrl(null);
        
        // Act - Use the authenticated user ID that matches the restaurant owner
        deleteMenuItemUseCase.execute(authenticatedUserId, menuItemId);
        
        // Assert
        verify(menuItemRepository).findById(menuItemId);
        verify(menuItemRepository).deleteById(menuItemId);
        verifyNoInteractions(fileStorageRepository);
    }

    @Test
    void shouldDeleteMenuItemWithPhoto() throws IOException {
        // Arrange
        String photoUrl = "http://example.com/photos/item1.jpg";
        String fileName = "item1.jpg";
        testMenuItem.setPhotoUrl(photoUrl);
        
        when(menuItemRepository.findById(menuItemId)).thenReturn(Optional.of(testMenuItem));
        doNothing().when(fileStorageRepository).delete(fileName);
        
        // Act - Use the authenticated user ID that matches the restaurant owner
        deleteMenuItemUseCase.execute(authenticatedUserId, menuItemId);
        
        // Assert
        verify(menuItemRepository).findById(menuItemId);
        verify(fileStorageRepository).delete(fileName);
        verify(menuItemRepository).deleteById(menuItemId);
    }

    @Test
    void shouldHandleEmptyPhotoUrl() {
        // Arrange
        testMenuItem.setPhotoUrl("");
        when(menuItemRepository.findById(menuItemId)).thenReturn(Optional.of(testMenuItem));
        
        // Act - Use the authenticated user ID that matches the restaurant owner
        deleteMenuItemUseCase.execute(authenticatedUserId, menuItemId);
        
        // Assert
        verify(menuItemRepository).findById(menuItemId);
        verify(menuItemRepository).deleteById(menuItemId);
        verifyNoInteractions(fileStorageRepository);
    }

    @Test
    void shouldHandleBlankPhotoUrl() {
        // Arrange
        testMenuItem.setPhotoUrl("   ");
        when(menuItemRepository.findById(menuItemId)).thenReturn(Optional.of(testMenuItem));
        
        // Act - Use the authenticated user ID that matches the restaurant owner
        deleteMenuItemUseCase.execute(authenticatedUserId, menuItemId);
        
        // Assert
        verify(menuItemRepository).findById(menuItemId);
        verify(menuItemRepository).deleteById(menuItemId);
        verifyNoInteractions(fileStorageRepository);
    }

    @Test
    void shouldThrowExceptionWhenMenuItemNotFound() {
        // Arrange
        when(menuItemRepository.findById(menuItemId)).thenReturn(Optional.empty());
        
        // Act & Assert
        BusinessException exception = assertThrows(
            BusinessException.class,
            () -> deleteMenuItemUseCase.execute(authenticatedUserId, menuItemId)
        );
        
        assertEquals("Item do menu não encontrado.", exception.getMessage());
        verify(menuItemRepository).findById(menuItemId);
        verify(menuItemRepository, never()).deleteById(any());
        verifyNoInteractions(fileStorageRepository);
    }

    @Test
    void shouldHandleFileDeletionFailure() throws IOException {
        // Arrange
        String photoUrl = "http://example.com/photos/photo123.jpg?token=abc";
        testMenuItem.setPhotoUrl(photoUrl);
        
        when(menuItemRepository.findById(menuItemId)).thenReturn(Optional.of(testMenuItem));
        doThrow(new IOException("Failed to delete file")).when(fileStorageRepository).delete("photo123.jpg?token=abc");
        
        // Act & Assert
        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> deleteMenuItemUseCase.execute(authenticatedUserId, menuItemId)
        );
        
        assertEquals("Failed to delete menu item photo", exception.getMessage());
        verify(fileStorageRepository).delete("photo123.jpg?token=abc");
        // The implementation throws an exception before calling deleteById when file deletion fails
        verify(menuItemRepository, never()).deleteById(any());
    }

    @Test
    void shouldHandleNullId() {
        // Arrange
        when(menuItemRepository.findById(null)).thenReturn(Optional.empty());
        
        // Act & Assert
        BusinessException exception = assertThrows(
            BusinessException.class,
            () -> deleteMenuItemUseCase.execute(authenticatedUserId, null)
        );
        
        assertEquals("Item do menu não encontrado.", exception.getMessage());
        verify(menuItemRepository).findById(null);
        verify(menuItemRepository, never()).deleteById(any());
        verifyNoInteractions(fileStorageRepository);
    }

    @Test
    void shouldExtractFileNameFromUrlCorrectly() throws IOException {
        // Arrange
        String photoUrl = "http://example.com/photos/photo123.jpg?token=abc";
        testMenuItem.setPhotoUrl(photoUrl);
        
        when(menuItemRepository.findById(menuItemId)).thenReturn(Optional.of(testMenuItem));
        
        // Act
        deleteMenuItemUseCase.execute(authenticatedUserId, menuItemId);
        
        // Assert - Verify the correct file name was extracted and passed to delete
        // The implementation keeps the query parameters in the filename
        verify(fileStorageRepository).delete("photo123.jpg?token=abc");
    }

    @Test
    void shouldHandleMalformedUrl() throws IOException {
        // Arrange
        String malformedUrl = "not-a-url";
        testMenuItem.setPhotoUrl(malformedUrl);
        
        when(menuItemRepository.findById(menuItemId)).thenReturn(Optional.of(testMenuItem));
        
        // This test verifies that the code doesn't crash with malformed URLs
        // It will try to delete the entire string as a filename
        // Use the authenticated user ID that matches the restaurant owner
        assertDoesNotThrow(() -> deleteMenuItemUseCase.execute(authenticatedUserId, menuItemId));
        verify(fileStorageRepository).delete(malformedUrl);
    }
}
