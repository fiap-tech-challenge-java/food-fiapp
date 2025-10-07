package com.fiap.foodfiapp.core.domain.entity;

import org.junit.jupiter.api.Test;
import java.time.OffsetDateTime;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class MenuItemTest {

    @Test
    void shouldCreateMenuItem() {
        UUID id = UUID.randomUUID();
        UUID restaurantId = UUID.randomUUID();
        OffsetDateTime createdAt = OffsetDateTime.now();
        OffsetDateTime updatedAt = OffsetDateTime.now();

        MenuItem menuItem = new MenuItem(id, "Pizza Margherita", "Delicious pizza with tomato and mozzarella",
                                       29.99, false, "http://example.com/pizza.jpg", restaurantId, createdAt, updatedAt);

        assertEquals(id, menuItem.getId());
        assertEquals("Pizza Margherita", menuItem.getName());
        assertEquals("Delicious pizza with tomato and mozzarella", menuItem.getDescription());
        assertEquals(29.99, menuItem.getPrice());
        assertFalse(menuItem.isLocalOnly());
        assertEquals("http://example.com/pizza.jpg", menuItem.getPhotoUrl());
        assertEquals(restaurantId, menuItem.getRestaurantId());
        assertEquals(createdAt, menuItem.getCreatedAt());
        assertEquals(updatedAt, menuItem.getUpdatedAt());
    }

    @Test
    void shouldSetAllProperties() {
        UUID id = UUID.randomUUID();
        UUID restaurantId = UUID.randomUUID();
        OffsetDateTime createdAt = OffsetDateTime.now();
        OffsetDateTime updatedAt = OffsetDateTime.now();

        MenuItem menuItem = new MenuItem(UUID.randomUUID(), "Initial", "Initial desc", 0.0, false, "", UUID.randomUUID(), createdAt, updatedAt);

        menuItem.setId(id);
        menuItem.setName("Hambúrguer Artesanal");
        menuItem.setDescription("Hambúrguer com carne premium e ingredientes frescos");
        menuItem.setPrice(35.50);
        menuItem.setLocalOnly(true);
        menuItem.setPhotoUrl("http://example.com/burger.jpg");
        menuItem.setRestaurantId(restaurantId);

        assertEquals(id, menuItem.getId());
        assertEquals("Hambúrguer Artesanal", menuItem.getName());
        assertEquals("Hambúrguer com carne premium e ingredientes frescos", menuItem.getDescription());
        assertEquals(35.50, menuItem.getPrice());
        assertTrue(menuItem.isLocalOnly());
        assertEquals("http://example.com/burger.jpg", menuItem.getPhotoUrl());
        assertEquals(restaurantId, menuItem.getRestaurantId());
    }

    @Test
    void shouldHandleNullValues() {
        OffsetDateTime createdAt = OffsetDateTime.now();
        OffsetDateTime updatedAt = OffsetDateTime.now();

        MenuItem menuItem = new MenuItem(null, null, null, null, false, null, null, createdAt, updatedAt);

        assertNull(menuItem.getId());
        assertNull(menuItem.getName());
        assertNull(menuItem.getDescription());
        assertNull(menuItem.getPrice());
        assertFalse(menuItem.isLocalOnly());
        assertNull(menuItem.getPhotoUrl());
        assertNull(menuItem.getRestaurantId());
    }

    @Test
    void shouldHandleDifferentPriceValues() {
        UUID id = UUID.randomUUID();
        UUID restaurantId = UUID.randomUUID();
        OffsetDateTime now = OffsetDateTime.now();

        MenuItem menuItem = new MenuItem(id, "Test Item", "Test Description", 0.0, false, "", restaurantId, now, now);
        assertEquals(0.0, menuItem.getPrice());

        menuItem.setPrice(99.99);
        assertEquals(99.99, menuItem.getPrice());

        menuItem.setPrice(1.50);
        assertEquals(1.50, menuItem.getPrice());
    }

    @Test
    void shouldToggleLocalOnlyFlag() {
        UUID id = UUID.randomUUID();
        UUID restaurantId = UUID.randomUUID();
        OffsetDateTime now = OffsetDateTime.now();

        MenuItem menuItem = new MenuItem(id, "Test Item", "Test Description", 10.0, false, "", restaurantId, now, now);
        assertFalse(menuItem.isLocalOnly());

        menuItem.setLocalOnly(true);
        assertTrue(menuItem.isLocalOnly());

        menuItem.setLocalOnly(false);
        assertFalse(menuItem.isLocalOnly());
    }

    @Test
    void shouldInheritFromBaseEntity() {
        UUID id = UUID.randomUUID();
        UUID restaurantId = UUID.randomUUID();
        OffsetDateTime createdAt = OffsetDateTime.now();
        OffsetDateTime updatedAt = OffsetDateTime.now().plusDays(1);

        MenuItem menuItem = new MenuItem(id, "Test Item", "Test Description", 15.0, true, "photo.jpg", restaurantId, createdAt, updatedAt);

        assertEquals(createdAt, menuItem.getCreatedAt());
        assertEquals(updatedAt, menuItem.getUpdatedAt());
        assertTrue(menuItem.getIsActive());

        menuItem.setIsActive(false);
        assertFalse(menuItem.getIsActive());
    }
}
