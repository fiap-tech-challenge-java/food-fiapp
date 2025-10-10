package com.fiap.foodfiapp.core.application.usecases.menuitem;

import com.fiap.foodfiapp.core.application.dto.FileUploadRequest;
import com.fiap.foodfiapp.core.application.usecases.menuitem.impl.UpdateMenuItemUseCaseImpl;
import com.fiap.foodfiapp.core.domain.entity.MenuItem;
import com.fiap.foodfiapp.core.domain.entity.Restaurant;
import com.fiap.foodfiapp.core.domain.entity.User;
import com.fiap.foodfiapp.core.domain.entity.UserType;
import com.fiap.foodfiapp.core.domain.exception.UnauthorizedAccessException;
import com.fiap.foodfiapp.core.domain.port.FileStorageRepository;
import com.fiap.foodfiapp.core.domain.port.MenuItemRepository;
import com.fiap.foodfiapp.core.domain.port.RestaurantRepository;
import com.fiap.foodfiapp.core.domain.port.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateMenuItemUseCaseTest {

    @Mock
    private MenuItemRepository menuItemRepository;

    @Mock
    private FileStorageRepository fileStorageRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private UserRepository userRepository;

    private UpdateMenuItemUseCaseImpl updateMenuItemUseCase;

    private UUID authenticatedUserId;
    private UUID menuItemId;
    private UUID restaurantId;
    private MenuItem existingMenuItem;
    private Restaurant restaurant;
    private FileUploadRequest photoRequest;

    @BeforeEach
    void setUp() {
        // Initialize the use case with all required dependencies
        updateMenuItemUseCase = new UpdateMenuItemUseCaseImpl(menuItemRepository, fileStorageRepository, restaurantRepository, userRepository);
        
        authenticatedUserId = UUID.randomUUID();
        menuItemId = UUID.randomUUID();
        restaurantId = UUID.randomUUID();
        
        // Setup existing menu item
        existingMenuItem = new MenuItem(
            menuItemId,
            "Old Burger",
            "Old description",
            25.99,
            false,
            "http://example.com/old-photo.jpg",
            restaurantId,
            java.time.OffsetDateTime.now(),
            java.time.OffsetDateTime.now()
        );
        
        // Setup restaurant
        restaurant = new Restaurant();
        restaurant.setId(restaurantId);
        restaurant.setUserOwnerId(authenticatedUserId);
        
        // Setup photo request
        byte[] photoBytes = "test photo content".getBytes();
        InputStream photoStream = new ByteArrayInputStream(photoBytes);
        photoRequest = new FileUploadRequest(
            photoStream,
            photoBytes.length,
            "image/jpeg",
            "test-photo.jpg"
        );
    }

    @Test
    void shouldUpdateMenuItemSuccessfully() throws IOException {
        // Arrange
        // Mock user as owner
        User ownerUser = new User();
        ownerUser.setId(authenticatedUserId);
        UserType ownerType = new UserType();
        ownerType.setName("OWNER");
        ownerUser.setUserType(ownerType);
        when(userRepository.findById(authenticatedUserId)).thenReturn(Optional.of(ownerUser));
        
        when(menuItemRepository.findById(menuItemId)).thenReturn(Optional.of(existingMenuItem));
        when(restaurantRepository.findById(restaurantId)).thenReturn(restaurant);
        when(fileStorageRepository.store(any(), anyLong(), anyString(), anyString()))
            .thenReturn("http://example.com/new-photo.jpg");
        when(menuItemRepository.save(any(MenuItem.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        // Act
        MenuItem updatedItem = updateMenuItemUseCase.execute(
            authenticatedUserId,
            menuItemId,
            "New Burger",
            "New description",
            29.99,
            true,
            photoRequest
        );
        
        // Assert
        assertNotNull(updatedItem);
        assertEquals("New Burger", updatedItem.getName());
        assertEquals("New description", updatedItem.getDescription());
        assertEquals(29.99, updatedItem.getPrice());
        assertTrue(updatedItem.isLocalOnly());
        
        verify(menuItemRepository).findById(menuItemId);
        verify(restaurantRepository).findById(restaurantId);
        verify(menuItemRepository).save(any(MenuItem.class));
    }

    @Test
    void shouldUpdateMenuItemWithoutPhoto() throws IOException {
        // Arrange
        // Mock user as owner
        User ownerUser = new User();
        ownerUser.setId(authenticatedUserId);
        UserType ownerType = new UserType();
        ownerType.setName("OWNER");
        ownerUser.setUserType(ownerType);
        when(userRepository.findById(authenticatedUserId)).thenReturn(Optional.of(ownerUser));
        
        when(menuItemRepository.findById(menuItemId)).thenReturn(Optional.of(existingMenuItem));
        when(restaurantRepository.findById(restaurantId)).thenReturn(restaurant);
        when(menuItemRepository.save(any(MenuItem.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        // Act
        MenuItem updatedItem = updateMenuItemUseCase.execute(
            authenticatedUserId,
            menuItemId,
            "New Burger",
            "New description",
            29.99,
            true,
            null
        );
        
        // Assert
        assertNotNull(updatedItem);
        assertEquals("http://example.com/old-photo.jpg", updatedItem.getPhotoUrl());
        verify(fileStorageRepository, never()).store(any(), anyLong(), anyString(), anyString());
    }

    @Test
    void shouldDeleteOldPhotoWhenUpdatingWithNewOne() throws IOException {
        // Arrange
        String oldPhotoUrl = "http://example.com/old-photo.jpg";
        String oldFileName = "old-photo.jpg";
        existingMenuItem.setPhotoUrl(oldPhotoUrl);
        
        // Mock user as owner
        User ownerUser = new User();
        ownerUser.setId(authenticatedUserId);
        UserType ownerType = new UserType();
        ownerType.setName("OWNER");
        ownerUser.setUserType(ownerType);
        when(userRepository.findById(authenticatedUserId)).thenReturn(Optional.of(ownerUser));
        
        when(menuItemRepository.findById(menuItemId)).thenReturn(Optional.of(existingMenuItem));
        when(restaurantRepository.findById(restaurantId)).thenReturn(restaurant);
        when(fileStorageRepository.store(any(), anyLong(), anyString(), anyString()))
            .thenReturn("http://example.com/new-photo.jpg");
        when(menuItemRepository.save(any(MenuItem.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        // Act
        updateMenuItemUseCase.execute(
            authenticatedUserId,
            menuItemId,
            "New Burger",
            "New description",
            29.99,
            true,
            photoRequest
        );
        
        // Assert
        verify(fileStorageRepository).delete(oldFileName);
    }

    @Test
    void shouldThrowExceptionWhenMenuItemNotFound() {
        // Arrange
        when(menuItemRepository.findById(menuItemId)).thenReturn(Optional.empty());
        
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> updateMenuItemUseCase.execute(
                authenticatedUserId,
                menuItemId,
                "New Name",
                "New description",
                29.99,
                true,
                null
            )
        );
        
        assertEquals("Menu item not found with id: " + menuItemId, exception.getMessage());
        verify(menuItemRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenNotAuthorized() {
        // Arrange
        UUID differentOwnerId = UUID.randomUUID();
        restaurant.setUserOwnerId(differentOwnerId);
        
        when(menuItemRepository.findById(menuItemId)).thenReturn(Optional.of(existingMenuItem));
        when(restaurantRepository.findById(restaurantId)).thenReturn(restaurant);
        
        // Act & Assert
        UnauthorizedAccessException exception = assertThrows(
            UnauthorizedAccessException.class,
            () -> updateMenuItemUseCase.execute(
                authenticatedUserId,
                menuItemId,
                "New Name",
                "New description",
                29.99,
                true,
                null
            )
        );
        
        assertEquals("Only the restaurant owner or ADMIN can update this menu item", exception.getMessage());
        verify(menuItemRepository, never()).save(any());
    }

    @Test
    void shouldHandleNullAuthenticatedUser() {
        // Arrange
        when(menuItemRepository.findById(menuItemId)).thenReturn(Optional.of(existingMenuItem));
        
        // Act & Assert
        UnauthorizedAccessException exception = assertThrows(
            UnauthorizedAccessException.class,
            () -> updateMenuItemUseCase.execute(
                null,
                menuItemId,
                "New Name",
                "New description",
                29.99,
                true,
                null
            )
        );
        
        assertEquals("Authenticated user not found", exception.getMessage());
    }

    @Test
    void shouldHandlePhotoUploadFailure() throws IOException {
        // Arrange
        when(menuItemRepository.findById(menuItemId)).thenReturn(Optional.of(existingMenuItem));
        when(restaurantRepository.findById(restaurantId)).thenReturn(restaurant);
        when(fileStorageRepository.store(any(), anyLong(), anyString(), anyString()))
            .thenThrow(new IOException("Failed to upload photo"));
        
        // Act & Assert
        assertThrows(
            IOException.class,
            () -> updateMenuItemUseCase.execute(
                authenticatedUserId,
                menuItemId,
                "New Burger",
                "New description",
                29.99,
                true,
                photoRequest
            )
        );
        
        // Verify old photo is not deleted if new upload fails
        verify(fileStorageRepository, never()).delete(anyString());
    }

    @Test
    void shouldHandleNullFields() throws IOException {
        // Arrange
        // Mock user as owner
        User ownerUser = new User();
        ownerUser.setId(authenticatedUserId);
        UserType ownerType = new UserType();
        ownerType.setName("OWNER");
        ownerUser.setUserType(ownerType);
        when(userRepository.findById(authenticatedUserId)).thenReturn(Optional.of(ownerUser));
        
        when(menuItemRepository.findById(menuItemId)).thenReturn(Optional.of(existingMenuItem));
        when(restaurantRepository.findById(restaurantId)).thenReturn(restaurant);
        when(menuItemRepository.save(any(MenuItem.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        // Act - null name, description, and price should be handled
        MenuItem updatedItem = updateMenuItemUseCase.execute(
            authenticatedUserId,
            menuItemId,
            null,
            null,
            null,
            null,
            null
        );
        
        // Assert - Should not throw and should keep original values for null fields
        assertNotNull(updatedItem);
        assertEquals("Old Burger", updatedItem.getName());
        assertEquals("Old description", updatedItem.getDescription());
        assertEquals(25.99, updatedItem.getPrice());
        assertFalse(updatedItem.isLocalOnly());
    }

    @Test
    void shouldHandleEmptyPhotoFilename() throws IOException {
        // Arrange
        FileUploadRequest emptyFilenamePhoto = new FileUploadRequest(
            new ByteArrayInputStream("test".getBytes()),
            4,
            "image/jpeg",
            ""
        );
        
        when(menuItemRepository.findById(menuItemId)).thenReturn(Optional.of(existingMenuItem));
        when(restaurantRepository.findById(restaurantId)).thenReturn(restaurant);
        when(fileStorageRepository.store(any(), anyLong(), anyString(), anyString()))
            .thenReturn("http://example.com/new-photo.jpg");
        when(menuItemRepository.save(any(MenuItem.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        // Act
        assertDoesNotThrow(() -> updateMenuItemUseCase.execute(
            authenticatedUserId,
            menuItemId,
            "New Burger",
            "New description",
            29.99,
            true,
            emptyFilenamePhoto
        ));
    }
}
