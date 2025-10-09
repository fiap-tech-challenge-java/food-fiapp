package com.fiap.foodfiapp.core.application.usecases.menuitem;

import com.fiap.foodfiapp.core.application.usecases.menuitem.impl.ValidateMenuItemOwnershipUseCaseImpl;
import com.fiap.foodfiapp.core.domain.entity.MenuItem;
import com.fiap.foodfiapp.core.domain.exception.MenuItemNotFoundException;
import com.fiap.foodfiapp.core.domain.port.MenuItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ValidateMenuItemOwnershipUseCaseTest {

    @Mock
    private MenuItemRepository menuItemRepository;

    @InjectMocks
    private ValidateMenuItemOwnershipUseCaseImpl validateMenuItemOwnershipUseCase;

    private UUID itemId;
    private UUID restaurantId;
    private MenuItem menuItem;

    @BeforeEach
    void setUp() {
        itemId = UUID.randomUUID();
        restaurantId = UUID.randomUUID();
        
        menuItem = new MenuItem(
            itemId,
            "Test Burger",
            "Delicious burger",
            25.99,
            false,
            "http://example.com/photo.jpg",
            restaurantId,
            null,
            null
        );
    }

    @Test
    void shouldReturnTrueWhenMenuItemBelongsToRestaurant() {
        // Arrange
        when(menuItemRepository.findById(itemId)).thenReturn(Optional.of(menuItem));

        // Act
        boolean result = validateMenuItemOwnershipUseCase.execute(itemId, restaurantId);

        // Assert
        assertTrue(result);
        verify(menuItemRepository).findById(itemId);
    }

    @Test
    void shouldReturnFalseWhenMenuItemDoesNotBelongToRestaurant() {
        // Arrange
        UUID differentRestaurantId = UUID.randomUUID();
        when(menuItemRepository.findById(itemId)).thenReturn(Optional.of(menuItem));

        // Act
        boolean result = validateMenuItemOwnershipUseCase.execute(itemId, differentRestaurantId);

        // Assert
        assertFalse(result);
        verify(menuItemRepository).findById(itemId);
    }

    @Test
    void shouldThrowExceptionWhenMenuItemNotFound() {
        // Arrange
        when(menuItemRepository.findById(itemId)).thenReturn(Optional.empty());

        // Act & Assert
        MenuItemNotFoundException exception = assertThrows(
            MenuItemNotFoundException.class,
            () -> validateMenuItemOwnershipUseCase.execute(itemId, restaurantId)
        );
        
        assertTrue(exception.getMessage().contains(itemId.toString()));
        verify(menuItemRepository).findById(itemId);
    }

    @Test
    void shouldValidateOwnershipForMultipleMenuItems() {
        // Arrange
        UUID item2Id = UUID.randomUUID();
        MenuItem menuItem2 = new MenuItem(
            item2Id,
            "Test Pizza",
            "Delicious pizza",
            30.99,
            false,
            "http://example.com/pizza.jpg",
            restaurantId,
            null,
            null
        );
        
        when(menuItemRepository.findById(itemId)).thenReturn(Optional.of(menuItem));
        when(menuItemRepository.findById(item2Id)).thenReturn(Optional.of(menuItem2));

        // Act
        boolean result1 = validateMenuItemOwnershipUseCase.execute(itemId, restaurantId);
        boolean result2 = validateMenuItemOwnershipUseCase.execute(item2Id, restaurantId);

        // Assert
        assertTrue(result1);
        assertTrue(result2);
        verify(menuItemRepository).findById(itemId);
        verify(menuItemRepository).findById(item2Id);
    }

    @Test
    void shouldHandleNullRestaurantIdInMenuItem() {
        // Arrange
        menuItem.setRestaurantId(null);
        when(menuItemRepository.findById(itemId)).thenReturn(Optional.of(menuItem));

        // Act & Assert
        assertThrows(
            NullPointerException.class,
            () -> validateMenuItemOwnershipUseCase.execute(itemId, restaurantId)
        );
    }
}
