package com.fiap.foodfiapp.core.application.usecases.menuitem;

import com.fiap.foodfiapp.core.application.usecases.menuitem.impl.FindMenuItemByIdUseCaseImpl;
import com.fiap.foodfiapp.core.domain.entity.MenuItem;
import com.fiap.foodfiapp.core.domain.port.MenuItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindMenuItemByIdUseCaseTest {

    @Mock
    private MenuItemRepository menuItemRepository;

    @InjectMocks
    private FindMenuItemByIdUseCaseImpl findMenuItemByIdUseCase;

    private UUID menuItemId;
    private MenuItem testMenuItem;

    @BeforeEach
    void setUp() {
        menuItemId = UUID.randomUUID();
        UUID restaurantId = UUID.randomUUID();
        OffsetDateTime now = OffsetDateTime.now(ZoneId.systemDefault());
        
        testMenuItem = new MenuItem(
            menuItemId,                  // id
            "Test Burger",              // name
            "Delicious test burger",    // description
            29.99,    // price
            false,                      // localOnly
            "http://example.com/test-burger.jpg", // photoUrl
            restaurantId,               // restaurantId
            now,                        // createdAt
            now                         // updatedAt
        );
    }

    @Test
    void shouldReturnMenuItemWhenFound() {
        // Arrange
        when(menuItemRepository.findById(menuItemId)).thenReturn(Optional.of(testMenuItem));

        // Act
        Optional<MenuItem> result = findMenuItemByIdUseCase.execute(menuItemId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(testMenuItem, result.get());
        verify(menuItemRepository).findById(menuItemId);
    }

    @Test
    void shouldReturnEmptyWhenMenuItemNotFound() {
        // Arrange
        when(menuItemRepository.findById(menuItemId)).thenReturn(Optional.empty());

        // Act
        Optional<MenuItem> result = findMenuItemByIdUseCase.execute(menuItemId);

        // Assert
        assertTrue(result.isEmpty());
        verify(menuItemRepository).findById(menuItemId);
    }

    @Test
    void shouldThrowExceptionWhenIdIsNull() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, 
            () -> findMenuItemByIdUseCase.execute(null));
        
        verify(menuItemRepository, never()).findById(any());
    }

    @Test
    void shouldReturnMenuItemWithCorrectId() {
        // Arrange
        when(menuItemRepository.findById(menuItemId)).thenReturn(Optional.of(testMenuItem));

        // Act
        Optional<MenuItem> result = findMenuItemByIdUseCase.execute(menuItemId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(menuItemId, result.get().getId());
    }

    @Test
    void shouldReturnMenuItemWithAllFields() {
        // Arrange
        when(menuItemRepository.findById(menuItemId)).thenReturn(Optional.of(testMenuItem));

        // Act
        Optional<MenuItem> result = findMenuItemByIdUseCase.execute(menuItemId);

        // Assert
        assertTrue(result.isPresent());
        MenuItem menuItem = result.get();
        assertEquals(menuItemId, menuItem.getId());
        assertEquals("Test Burger", menuItem.getName());
        assertEquals("Delicious test burger", menuItem.getDescription());
        assertEquals(29.99, menuItem.getPrice());
        assertFalse(menuItem.isLocalOnly());
        assertEquals("http://example.com/test-burger.jpg", menuItem.getPhotoUrl());
        assertNotNull(menuItem.getRestaurantId());
        assertNotNull(menuItem.getCreatedAt());
        assertNotNull(menuItem.getUpdatedAt());
    }

    @Test
    void shouldNotModifyReturnedMenuItem() {
        // Arrange
        when(menuItemRepository.findById(menuItemId)).thenReturn(Optional.of(testMenuItem));

        // Act
        Optional<MenuItem> result = findMenuItemByIdUseCase.execute(menuItemId);

        // Assert
        assertTrue(result.isPresent());
        assertDoesNotThrow(() -> {
            MenuItem menuItem = result.get();
            menuItem.setName("Modified Name");
        });
    }

    @Test
    void shouldHandleMultipleCallsWithDifferentIds() {
        // Arrange
        UUID secondId = UUID.randomUUID();
        OffsetDateTime now = OffsetDateTime.now();
        MenuItem secondItem = new MenuItem(
            secondId,                      // id
            "Second Item",                 // name
            "Description for second item", // description
            39.99,                        // price
            true,                         // localOnly
            "http://example.com/second-item.jpg", // photoUrl
            UUID.randomUUID(),            // restaurantId
            now,                          // createdAt
            now                           // updatedAt
        );
        
        when(menuItemRepository.findById(menuItemId)).thenReturn(Optional.of(testMenuItem));
        when(menuItemRepository.findById(secondId)).thenReturn(Optional.of(secondItem));

        // Act
        Optional<MenuItem> result1 = findMenuItemByIdUseCase.execute(menuItemId);
        Optional<MenuItem> result2 = findMenuItemByIdUseCase.execute(secondId);

        // Assert
        assertTrue(result1.isPresent());
        assertEquals("Test Burger", result1.get().getName());
        
        assertTrue(result2.isPresent());
        assertEquals("Second Item", result2.get().getName());
        
        verify(menuItemRepository).findById(menuItemId);
        verify(menuItemRepository).findById(secondId);
    }

    @Test
    void shouldReturnEmptyForNonExistentId() {
        // Arrange
        UUID nonExistentId = UUID.randomUUID();
        when(menuItemRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Act
        Optional<MenuItem> result = findMenuItemByIdUseCase.execute(nonExistentId);

        // Assert
        assertTrue(result.isEmpty());
        verify(menuItemRepository).findById(nonExistentId);
    }
}
