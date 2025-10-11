package com.fiap.foodfiapp.infrastructure.persistence.repository;

import com.fiap.foodfiapp.core.domain.entity.MenuItem;
import com.fiap.foodfiapp.infrastructure.persistence.entity.MenuItemEntity;
import com.fiap.foodfiapp.infrastructure.persistence.entity.RestaurantEntity;
import com.fiap.foodfiapp.infrastructure.persistence.springdata.MenuItemSpringDataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MenuItemRepositoryImplTest {

    @Mock
    private MenuItemSpringDataRepository menuItemSpringDataRepository;

    @InjectMocks
    private MenuItemRepositoryImpl menuItemRepositoryImpl;

    private UUID menuItemId;
    private UUID restaurantId;
    private MenuItem menuItem;
    private MenuItemEntity menuItemEntity;
    private RestaurantEntity restaurantEntity;

    @BeforeEach
    void setUp() {
        menuItemId = UUID.randomUUID();
        restaurantId = UUID.randomUUID();

        menuItem = new MenuItem(
            menuItemId,
            "Test Burger",
            "Delicious test burger",
            25.99,
            false,
            "http://example.com/photo.jpg",
            restaurantId,
            OffsetDateTime.now(),
            OffsetDateTime.now()
        );

        restaurantEntity = new RestaurantEntity();
        restaurantEntity.setId(restaurantId);

        menuItemEntity = new MenuItemEntity();
        menuItemEntity.setId(menuItemId);
        menuItemEntity.setName("Test Burger");
        menuItemEntity.setDescription("Delicious test burger");
        menuItemEntity.setPrice(new BigDecimal("25.99"));
        menuItemEntity.setLocalOnly(false);
        menuItemEntity.setPhotoUrl("http://example.com/photo.jpg");
        menuItemEntity.setRestaurant(restaurantEntity);
    }

    // ========== SAVE TESTS ==========

    @Test
    void shouldSaveMenuItemSuccessfully() {
        // Arrange
        when(menuItemSpringDataRepository.save(any(MenuItemEntity.class))).thenReturn(menuItemEntity);

        // Act
        MenuItem result = menuItemRepositoryImpl.save(menuItem);

        // Assert
        assertNotNull(result);
        assertEquals(menuItemId, result.getId());
        assertEquals("Test Burger", result.getName());
        assertEquals(25.99, result.getPrice());
        assertEquals(false, result.isLocalOnly());

        verify(menuItemSpringDataRepository).save(any(MenuItemEntity.class));
    }

    @Test
    void shouldPropagateExceptionFromSpringDataRepositoryOnSave() {
        // Arrange
        when(menuItemSpringDataRepository.save(any(MenuItemEntity.class)))
            .thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> menuItemRepositoryImpl.save(menuItem));
        verify(menuItemSpringDataRepository).save(any(MenuItemEntity.class));
    }

    // ========== FIND BY ID TESTS ==========

    @Test
    void shouldFindMenuItemByIdSuccessfully() {
        // Arrange
        when(menuItemSpringDataRepository.findById(menuItemId)).thenReturn(Optional.of(menuItemEntity));

        // Act
        Optional<MenuItem> result = menuItemRepositoryImpl.findById(menuItemId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(menuItemId, result.get().getId());
        assertEquals("Test Burger", result.get().getName());

        verify(menuItemSpringDataRepository).findById(menuItemId);
    }

    @Test
    void shouldReturnEmptyWhenMenuItemNotFoundById() {
        // Arrange
        when(menuItemSpringDataRepository.findById(menuItemId)).thenReturn(Optional.empty());

        // Act
        Optional<MenuItem> result = menuItemRepositoryImpl.findById(menuItemId);

        // Assert
        assertFalse(result.isPresent());
        verify(menuItemSpringDataRepository).findById(menuItemId);
    }

    @Test
    void shouldHandleNullIdInFindById() {
        // Arrange
        when(menuItemSpringDataRepository.findById(null)).thenReturn(Optional.empty());

        // Act
        Optional<MenuItem> result = menuItemRepositoryImpl.findById(null);

        // Assert
        assertFalse(result.isPresent());
        verify(menuItemSpringDataRepository).findById(null);
    }

    // ========== FIND ALL BY RESTAURANT ID TESTS ==========

    @Test
    void shouldFindAllMenuItemsByRestaurantIdSuccessfully() {
        // Arrange
        UUID secondItemId = UUID.randomUUID();
        MenuItemEntity secondEntity = new MenuItemEntity();
        secondEntity.setId(secondItemId);
        secondEntity.setName("Pizza");
        secondEntity.setRestaurant(restaurantEntity);

        List<MenuItemEntity> entities = Arrays.asList(menuItemEntity, secondEntity);

        when(menuItemSpringDataRepository.findAllByRestaurantId(restaurantId)).thenReturn(entities);

        // Act
        List<MenuItem> result = menuItemRepositoryImpl.findAllByRestaurantId(restaurantId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Test Burger", result.get(0).getName());
        assertEquals("Pizza", result.get(1).getName());

        verify(menuItemSpringDataRepository).findAllByRestaurantId(restaurantId);
    }

    @Test
    void shouldReturnEmptyListWhenNoMenuItemsFoundByRestaurantId() {
        // Arrange
        when(menuItemSpringDataRepository.findAllByRestaurantId(restaurantId))
            .thenReturn(Collections.emptyList());

        // Act
        List<MenuItem> result = menuItemRepositoryImpl.findAllByRestaurantId(restaurantId);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(menuItemSpringDataRepository).findAllByRestaurantId(restaurantId);
    }

    @Test
    void shouldHandleNullRestaurantIdInFindAll() {
        // Arrange
        when(menuItemSpringDataRepository.findAllByRestaurantId(null))
            .thenReturn(Collections.emptyList());

        // Act
        List<MenuItem> result = menuItemRepositoryImpl.findAllByRestaurantId(null);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(menuItemSpringDataRepository).findAllByRestaurantId(null);
    }

    // ========== DELETE BY ID TESTS ==========

    @Test
    void shouldDeleteMenuItemByIdSuccessfully() {
        // Arrange
        doNothing().when(menuItemSpringDataRepository).deleteById(menuItemId);

        // Act
        menuItemRepositoryImpl.deleteById(menuItemId);

        // Assert
        verify(menuItemSpringDataRepository).deleteById(menuItemId);
    }

    @Test
    void shouldHandleNullIdInDeleteById() {
        // Arrange
        doNothing().when(menuItemSpringDataRepository).deleteById(null);

        // Act
        menuItemRepositoryImpl.deleteById(null);

        // Assert
        verify(menuItemSpringDataRepository).deleteById(null);
    }

    @Test
    void shouldPropagateExceptionFromSpringDataRepositoryOnDelete() {
        // Arrange
        doThrow(new RuntimeException("Delete failed"))
            .when(menuItemSpringDataRepository).deleteById(menuItemId);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> menuItemRepositoryImpl.deleteById(menuItemId));
        verify(menuItemSpringDataRepository).deleteById(menuItemId);
    }

    // ========== EXISTS BY ID TESTS ==========

    @Test
    void shouldReturnTrueWhenMenuItemExists() {
        // Arrange
        when(menuItemSpringDataRepository.existsById(menuItemId)).thenReturn(true);

        // Act
        boolean result = menuItemRepositoryImpl.existsById(menuItemId);

        // Assert
        assertTrue(result);
        verify(menuItemSpringDataRepository).existsById(menuItemId);
    }

    @Test
    void shouldReturnFalseWhenMenuItemDoesNotExist() {
        // Arrange
        when(menuItemSpringDataRepository.existsById(menuItemId)).thenReturn(false);

        // Act
        boolean result = menuItemRepositoryImpl.existsById(menuItemId);

        // Assert
        assertFalse(result);
        verify(menuItemSpringDataRepository).existsById(menuItemId);
    }

    @Test
    void shouldHandleNullIdInExistsById() {
        // Arrange
        when(menuItemSpringDataRepository.existsById(null)).thenReturn(false);

        // Act
        boolean result = menuItemRepositoryImpl.existsById(null);

        // Assert
        assertFalse(result);
        verify(menuItemSpringDataRepository).existsById(null);
    }

    // ========== INTEGRATION AND EDGE CASE TESTS ==========

    @Test
    void shouldHandleMultipleOperationsInSequence() {
        // Arrange
        when(menuItemSpringDataRepository.save(any(MenuItemEntity.class))).thenReturn(menuItemEntity);
        when(menuItemSpringDataRepository.findById(menuItemId)).thenReturn(Optional.of(menuItemEntity));
        when(menuItemSpringDataRepository.existsById(menuItemId)).thenReturn(true);
        doNothing().when(menuItemSpringDataRepository).deleteById(menuItemId);

        // Act & Assert
        // Save
        MenuItem savedItem = menuItemRepositoryImpl.save(menuItem);
        assertNotNull(savedItem);

        // Find
        Optional<MenuItem> foundItem = menuItemRepositoryImpl.findById(menuItemId);
        assertTrue(foundItem.isPresent());

        // Exists
        boolean exists = menuItemRepositoryImpl.existsById(menuItemId);
        assertTrue(exists);

        // Delete
        assertDoesNotThrow(() -> menuItemRepositoryImpl.deleteById(menuItemId));

        // Verify all interactions
        verify(menuItemSpringDataRepository).save(any(MenuItemEntity.class));
        verify(menuItemSpringDataRepository).findById(menuItemId);
        verify(menuItemSpringDataRepository).existsById(menuItemId);
        verify(menuItemSpringDataRepository).deleteById(menuItemId);
    }

    @Test
    void shouldVerifyRepositoryBehaviorWithDifferentMenuItems() {
        // Test with different menu item properties

        // Arrange
        MenuItem veganItem = new MenuItem(
            UUID.randomUUID(),
            "Vegan Salad",
            "Healthy vegan salad",
            15.50,
            true, // available for in-store only
            null, // no photo
            restaurantId,
            OffsetDateTime.now(),
            OffsetDateTime.now()
        );

        MenuItemEntity veganEntity = new MenuItemEntity();
        veganEntity.setId(veganItem.getId());
        veganEntity.setName("Vegan Salad");
        veganEntity.setDescription("Healthy vegan salad");
        veganEntity.setPrice(new BigDecimal("15.50"));
        veganEntity.setLocalOnly(true);
        veganEntity.setPhotoUrl(null);
        veganEntity.setRestaurant(restaurantEntity);

        when(menuItemSpringDataRepository.save(any(MenuItemEntity.class))).thenReturn(veganEntity);

        // Act
        MenuItem result = menuItemRepositoryImpl.save(veganItem);

        // Assert
        assertNotNull(result);
        assertEquals("Vegan Salad", result.getName());
        assertEquals(15.50, result.getPrice());
        assertTrue(result.isLocalOnly());
        assertNull(result.getPhotoUrl());

        verify(menuItemSpringDataRepository).save(any(MenuItemEntity.class));
    }

    @Test
    void shouldHandleEmptyCollectionsCorrectly() {
        // Test behavior with empty collections

        // Arrange
        when(menuItemSpringDataRepository.findAllByRestaurantId(any(UUID.class)))
            .thenReturn(Collections.emptyList());

        // Act
        List<MenuItem> result = menuItemRepositoryImpl.findAllByRestaurantId(UUID.randomUUID());

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
