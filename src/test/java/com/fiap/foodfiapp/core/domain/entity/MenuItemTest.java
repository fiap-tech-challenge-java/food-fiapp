package com.fiap.foodfiapp.core.domain.entity;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class MenuItemTest {

    @Test
    void shouldCreateMenuItemWithAllFields() {
        // Arrange
        UUID id = UUID.randomUUID();
        String name = "Test Burger";
        String description = "Delicious test burger with cheese";
        Double price = 29.99;
        boolean localOnly = true;
        String photoUrl = "http://example.com/burger.jpg";
        UUID restaurantId = UUID.randomUUID();
        OffsetDateTime createdAt = OffsetDateTime.now().minusDays(1);
        OffsetDateTime updatedAt = OffsetDateTime.now();

        // Act
        MenuItem menuItem = new MenuItem(
            id, name, description, price, localOnly, photoUrl, 
            restaurantId, createdAt, updatedAt
        );

        // Assert
        assertAll(
            () -> assertEquals(id, menuItem.getId()),
            () -> assertEquals(name, menuItem.getName()),
            () -> assertEquals(description, menuItem.getDescription()),
            () -> assertEquals(price, menuItem.getPrice()),
            () -> assertTrue(menuItem.isLocalOnly()),
            () -> assertEquals(photoUrl, menuItem.getPhotoUrl()),
            () -> assertEquals(restaurantId, menuItem.getRestaurantId()),
            () -> assertEquals(createdAt, menuItem.getCreatedAt()),
            () -> assertEquals(updatedAt, menuItem.getUpdatedAt())
        );
    }

    @Test
    void shouldCreateMenuItemWithRequiredFields() {
        // Arrange
        String name = "Test Burger";
        Double price = 29.99;
        UUID restaurantId = UUID.randomUUID();

        // Act
        MenuItem menuItem = new MenuItem(
            null, name, null, price, false, null, 
            restaurantId, null, null
        );

        // Assert
        assertAll(
            () -> assertNull(menuItem.getId()),
            () -> assertEquals(name, menuItem.getName()),
            () -> assertNull(menuItem.getDescription()),
            () -> assertEquals(price, menuItem.getPrice()),
            () -> assertFalse(menuItem.isLocalOnly()),
            () -> assertNull(menuItem.getPhotoUrl()),
            () -> assertEquals(restaurantId, menuItem.getRestaurantId()),
            () -> assertNull(menuItem.getCreatedAt()),
            () -> assertNull(menuItem.getUpdatedAt())
        );
    }

    @Test
    void shouldUpdateMenuItemFields() {
        // Arrange
        MenuItem menuItem = new MenuItem(
            UUID.randomUUID(), "Old Name", "Old Description", 19.99, 
            false, "old.jpg", UUID.randomUUID(), null, null
        );

        // Act
        String newName = "Updated Burger";
        String newDescription = "New description with more details";
        Double newPrice = 34.99;
        boolean newLocalOnly = true;
        String newPhotoUrl = "http://example.com/new-burger.jpg";
        UUID newRestaurantId = UUID.randomUUID();
        OffsetDateTime newUpdatedAt = OffsetDateTime.now();

        menuItem.setName(newName);
        menuItem.setDescription(newDescription);
        menuItem.setPrice(newPrice);
        menuItem.setLocalOnly(newLocalOnly);
        menuItem.setPhotoUrl(newPhotoUrl);
        menuItem.setRestaurantId(newRestaurantId);
        menuItem.setUpdatedAt(newUpdatedAt);

        // Assert
        assertAll(
            () -> assertEquals(newName, menuItem.getName()),
            () -> assertEquals(newDescription, menuItem.getDescription()),
            () -> assertEquals(newPrice, menuItem.getPrice()),
            () -> assertTrue(menuItem.isLocalOnly()),
            () -> assertEquals(newPhotoUrl, menuItem.getPhotoUrl()),
            () -> assertEquals(newRestaurantId, menuItem.getRestaurantId()),
            () -> assertEquals(newUpdatedAt, menuItem.getUpdatedAt())
        );
    }

    @Test
    void shouldHandleNullValues() {
        // Arrange & Act
        MenuItem menuItem = new MenuItem(
            null, null, null, null, 
            false, null, null, null, null
        );

        // Assert
        assertAll(
            () -> assertNull(menuItem.getId()),
            () -> assertNull(menuItem.getName()),
            () -> assertNull(menuItem.getDescription()),
            () -> assertNull(menuItem.getPrice()),
            () -> assertFalse(menuItem.isLocalOnly()),
            () -> assertNull(menuItem.getPhotoUrl()),
            () -> assertNull(menuItem.getRestaurantId()),
            () -> assertNull(menuItem.getCreatedAt()),
            () -> assertNull(menuItem.getUpdatedAt())
        );
    }

    @Test
    void shouldHandleNegativePrice() {
        // Arrange & Act
        MenuItem menuItem = new MenuItem(
            null, "Free Burger", "Free sample", -10.0, 
            false, null, UUID.randomUUID(), null, null
        );

        // Assert
        assertEquals(-10.0, menuItem.getPrice());
    }

    @Test
    void shouldHandleZeroPrice() {
        // Arrange & Act
        MenuItem menuItem = new MenuItem(
            null, "Free Sample", "Free item", 0.0, 
            true, null, UUID.randomUUID(), null, null
        );

        // Assert
        assertEquals(0.0, menuItem.getPrice());
    }

    @Test
    void shouldHandleVeryLongName() {
        // Arrange
        String longName = "This is a very long menu item name that exceeds the typical length " +
                         "limit for a menu item name in most applications but should still " +
                         "be handled gracefully by the MenuItem class";

        // Act
        MenuItem menuItem = new MenuItem(
            null, longName, null, 9.99, 
            false, null, UUID.randomUUID(), null, null
        );

        // Assert
        assertEquals(longName, menuItem.getName());
    }

    @Test
    void shouldHandleVeryLongDescription() {
        // Arrange
        String longDescription = "This is a very long description that goes into great detail " +
                               "about the menu item, including all its ingredients, preparation " +
                               "method, potential allergens, and serving suggestions. It's " +
                               "meant to test how the MenuItem class handles long text fields.";

        // Act
        MenuItem menuItem = new MenuItem(
            null, "Test Item", longDescription, 9.99, 
            false, null, UUID.randomUUID(), null, null
        );

        // Assert
        assertEquals(longDescription, menuItem.getDescription());
    }
}
