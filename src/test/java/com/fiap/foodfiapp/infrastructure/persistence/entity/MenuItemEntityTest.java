package com.fiap.foodfiapp.infrastructure.persistence.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class MenuItemEntityTest {

    @Test
    void shouldCreateMenuItemEntityWithAllFields() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        BigDecimal price = BigDecimal.valueOf(29.99);
        RestaurantEntity restaurant = new RestaurantEntity();
        restaurant.setId(UUID.randomUUID());

        MenuItemEntity entity = new MenuItemEntity(
                id, "Test Item", "Description", price, true, "photo.jpg", restaurant, now, now
        );

        assertThat(entity.getId()).isEqualTo(id);
        assertThat(entity.getName()).isEqualTo("Test Item");
        assertThat(entity.getDescription()).isEqualTo("Description");
        assertThat(entity.getPrice()).isEqualTo(price);
        assertThat(entity.isLocalOnly()).isTrue();
        assertThat(entity.getPhotoUrl()).isEqualTo("photo.jpg");
        assertThat(entity.getRestaurant()).isEqualTo(restaurant);
        assertThat(entity.getCreatedAt()).isEqualTo(now);
        assertThat(entity.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    void shouldCreateEmptyMenuItemEntity() {
        MenuItemEntity entity = new MenuItemEntity();

        assertThat(entity.getId()).isNull();
        assertThat(entity.getName()).isNull();
        assertThat(entity.getDescription()).isNull();
        assertThat(entity.getPrice()).isNull();
        assertThat(entity.isLocalOnly()).isFalse();
        assertThat(entity.getPhotoUrl()).isNull();
        assertThat(entity.getRestaurant()).isNull();
        assertThat(entity.getCreatedAt()).isNull();
        assertThat(entity.getUpdatedAt()).isNull();
    }

    @Test
    void shouldSetAndGetAllFields() {
        MenuItemEntity entity = new MenuItemEntity();
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        BigDecimal price = BigDecimal.valueOf(15.50);
        RestaurantEntity restaurant = new RestaurantEntity();

        entity.setId(id);
        entity.setName("Updated Item");
        entity.setDescription("Updated Description");
        entity.setPrice(price);
        entity.setLocalOnly(false);
        entity.setPhotoUrl("updated-photo.jpg");
        entity.setRestaurant(restaurant);
        entity.setCreatedAt(now);
        entity.setUpdatedAt(now);

        assertThat(entity.getId()).isEqualTo(id);
        assertThat(entity.getName()).isEqualTo("Updated Item");
        assertThat(entity.getDescription()).isEqualTo("Updated Description");
        assertThat(entity.getPrice()).isEqualTo(price);
        assertThat(entity.isLocalOnly()).isFalse();
        assertThat(entity.getPhotoUrl()).isEqualTo("updated-photo.jpg");
        assertThat(entity.getRestaurant()).isEqualTo(restaurant);
        assertThat(entity.getCreatedAt()).isEqualTo(now);
        assertThat(entity.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    void shouldHandleNullRestaurant() {
        MenuItemEntity entity = new MenuItemEntity();
        entity.setRestaurant(null);

        assertThat(entity.getRestaurant()).isNull();
    }

    @Test
    void shouldImplementEqualsAndHashCode() {
        UUID id = UUID.randomUUID();
        LocalDateTime timestamp = LocalDateTime.now();
        RestaurantEntity restaurant = new RestaurantEntity();
        restaurant.setId(UUID.randomUUID());

        MenuItemEntity entity1 = new MenuItemEntity(
                id, "Test", "Description", BigDecimal.valueOf(10.00), false, "photo.jpg", restaurant, timestamp, timestamp
        );

        MenuItemEntity entity2 = new MenuItemEntity(
                id, "Test", "Description", BigDecimal.valueOf(10.00), false, "photo.jpg", restaurant, timestamp, timestamp
        );

        assertThat(entity1).isEqualTo(entity2);
        assertThat(entity1.hashCode()).isEqualTo(entity2.hashCode());
    }
}
