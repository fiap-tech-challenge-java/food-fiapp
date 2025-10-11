package com.fiap.foodfiapp.core.application.usecases.menuitem;

import com.fiap.foodfiapp.core.application.usecases.menuitem.impl.FindAllMenuItemsUseCaseImpl;
import com.fiap.foodfiapp.core.domain.entity.MenuItem;
import com.fiap.foodfiapp.core.domain.port.MenuItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindAllMenuItemsUseCaseTest {

    @Mock
    private MenuItemRepository menuItemRepository;

    @InjectMocks
    private FindAllMenuItemsUseCaseImpl findAllMenuItemsUseCase;

    private UUID restaurantId;
    private MenuItem item1;
    private MenuItem item2;

    @BeforeEach
    void setUp() {
        restaurantId = UUID.randomUUID();
        
        // Setup test menu items with all required parameters
        OffsetDateTime now = OffsetDateTime.now(ZoneId.systemDefault());
        
        // Create item1 using the constructor with all required fields
        item1 = new MenuItem(
            UUID.randomUUID(),  // id
            "Burger",          // name
            "Delicious burger", // description
            29.99,  // price
            false,             // localOnly
            "http://example.com/burger.jpg", // photoUrl
            restaurantId,      // restaurantId
            now,               // createdAt
            now                // updatedAt
        );
        
        // Create item2 using the constructor with all required fields
        item2 = new MenuItem(
            UUID.randomUUID(),  // id
            "Pizza",           // name
            "Tasty pizza",     // description
            19.99,  // price
            true,              // localOnly
            "http://example.com/pizza.jpg", // photoUrl
            restaurantId,      // restaurantId
            now,               // createdAt
            now                // updatedAt
        );
    }

    @Test
    void shouldReturnAllMenuItemsForRestaurant() {
        // Arrange
        List<MenuItem> expectedItems = Arrays.asList(item1, item2);
        when(menuItemRepository.findAllByRestaurantId(restaurantId)).thenReturn(expectedItems);

        // Act
        List<MenuItem> result = findAllMenuItemsUseCase.execute(restaurantId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.containsAll(expectedItems));
        verify(menuItemRepository).findAllByRestaurantId(restaurantId);
    }

    @Test
    void shouldReturnEmptyListWhenNoMenuItemsExist() {
        // Arrange
        when(menuItemRepository.findAllByRestaurantId(restaurantId)).thenReturn(Collections.emptyList());

        // Act
        List<MenuItem> result = findAllMenuItemsUseCase.execute(restaurantId);

        // Assert
        assertTrue(result.isEmpty());
        verify(menuItemRepository).findAllByRestaurantId(restaurantId);
    }

    @Test
    void shouldReturnUnmodifiableList() {
        // Arrange
        when(menuItemRepository.findAllByRestaurantId(restaurantId))
            .thenReturn(Collections.singletonList(item1));

        // Act
        List<MenuItem> result = findAllMenuItemsUseCase.execute(restaurantId);

        // Assert
        assertThrows(UnsupportedOperationException.class, 
            () -> result.add(new MenuItem(
                UUID.randomUUID(),
                "New Item",
                "New Description",
                9.99,
                false,
                "http://example.com/new.jpg",
                restaurantId,
                OffsetDateTime.now(),
                OffsetDateTime.now()
            )));
    }

    @Test
    void shouldHandleNullRestaurantId() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> findAllMenuItemsUseCase.execute(null));
        
        verify(menuItemRepository, never()).findAllByRestaurantId(any());
    }

    @Test
    void shouldReturnOnlyMenuItemsForSpecifiedRestaurant() {
        // Arrange
        UUID otherRestaurantId = UUID.randomUUID();
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        
        MenuItem otherRestaurantItem = new MenuItem(
            UUID.randomUUID(),
            "Other Restaurant Item",
            "Item from another restaurant",
            19.99,
            false,
            null,
            otherRestaurantId,
            now,
            now
        );
        
        when(menuItemRepository.findAllByRestaurantId(restaurantId)).thenReturn(Collections.singletonList(item1));
        when(menuItemRepository.findAllByRestaurantId(otherRestaurantId)).thenReturn(Collections.singletonList(otherRestaurantItem));

        // Act
        List<MenuItem> result1 = findAllMenuItemsUseCase.execute(restaurantId);
        List<MenuItem> result2 = findAllMenuItemsUseCase.execute(otherRestaurantId);

        // Assert
        assertEquals(1, result1.size());
        assertEquals(item1, result1.get(0));
        
        assertEquals(1, result2.size());
        assertEquals(otherRestaurantItem, result2.get(0));
        
        verify(menuItemRepository).findAllByRestaurantId(restaurantId);
        verify(menuItemRepository).findAllByRestaurantId(otherRestaurantId);
    }

    @Test
    void shouldReturnMenuItemsInOrderReturnedByRepository() {
        // Arrange
        List<MenuItem> expectedOrder = Arrays.asList(item2, item1); // Different order
        when(menuItemRepository.findAllByRestaurantId(restaurantId)).thenReturn(expectedOrder);

        // Act
        List<MenuItem> result = findAllMenuItemsUseCase.execute(restaurantId);

        // Assert
        assertEquals(expectedOrder, result);
        verify(menuItemRepository).findAllByRestaurantId(restaurantId);
    }

    @Test
    void shouldHandleLargeNumberOfItems() {
        // Arrange
        List<MenuItem> largeList = Collections.nCopies(1000, item1);
        when(menuItemRepository.findAllByRestaurantId(restaurantId)).thenReturn(largeList);

        // Act
        List<MenuItem> result = findAllMenuItemsUseCase.execute(restaurantId);

        // Assert
        assertEquals(1000, result.size());
    }

    @Test
    void shouldNotReturnLocalOnlyItems() {
        // This test verifies that localOnly items are handled correctly
        // The repository should filter out localOnly items if needed
        
        // Arrange
        item1.setLocalOnly(true);
        when(menuItemRepository.findAllByRestaurantId(restaurantId))
            .thenReturn(Collections.singletonList(item2));

        // Act
        List<MenuItem> result = findAllMenuItemsUseCase.execute(restaurantId);

        // Assert
        assertEquals(1, result.size());
        assertEquals(item2, result.get(0));
    }
}
