package com.fiap.foodfiapp.infrastructure.persistence.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class MenuItemEntityTest {

    @Test
    void shouldCreateMenuItemEntityWithAllFields() {
        UUID id = UUID.randomUUID();
        OffsetDateTime now = OffsetDateTime.now();
        BigDecimal price = BigDecimal.valueOf(29.99);
        RestaurantEntity restaurant = RestaurantEntity.builder()
                .id(UUID.randomUUID())
                .build();

        MenuItemEntity entity = MenuItemEntity.builder()
                .id(id)
                .name("Test Item")
                .description("Description")
                .price(price)
                .availableForInStoreOnly(true)
                .photoUrl("photo.jpg")
                .active(true)
                .restaurant(restaurant)
                .build();

        assertThat(entity.getId()).isEqualTo(id);
        assertThat(entity.getName()).isEqualTo("Test Item");
        assertThat(entity.getDescription()).isEqualTo("Description");
        assertThat(entity.getPrice()).isEqualTo(price);
        assertThat(entity.isAvailableForInStoreOnly()).isTrue();
        assertThat(entity.getPhotoUrl()).isEqualTo("photo.jpg");
        assertThat(entity.getRestaurant()).isEqualTo(restaurant);
        assertThat(entity.isActive()).isTrue();
    }

    @Test
    void shouldCreateEmptyMenuItemEntity() {
        MenuItemEntity entity = new MenuItemEntity();

        assertThat(entity.getId()).isNull();
        assertThat(entity.getName()).isNull();
        assertThat(entity.getDescription()).isNull();
        assertThat(entity.getPrice()).isNull();
        assertThat(entity.isAvailableForInStoreOnly()).isFalse();
        assertThat(entity.getPhotoUrl()).isNull();
        assertThat(entity.getRestaurant()).isNull();
        assertThat(entity.isActive()).isTrue();
    }

    @Test
    void shouldSetAndGetMenuItemEntityFields() {
        MenuItemEntity entity = new MenuItemEntity();
        UUID id = UUID.randomUUID();
        BigDecimal price = BigDecimal.valueOf(15.50);
        RestaurantEntity restaurant = new RestaurantEntity();

        entity.setId(id);
        entity.setName("Updated Item");
        entity.setDescription("Updated Description");
        entity.setPrice(price);
        entity.setAvailableForInStoreOnly(false);
        entity.setPhotoUrl("updated-photo.jpg");
        entity.setActive(true);
        entity.setRestaurant(restaurant);

        assertThat(entity.getId()).isEqualTo(id);
        assertThat(entity.getName()).isEqualTo("Updated Item");
        assertThat(entity.getDescription()).isEqualTo("Updated Description");
        assertThat(entity.getPrice()).isEqualTo(price);
        assertThat(entity.isAvailableForInStoreOnly()).isFalse();
        assertThat(entity.getPhotoUrl()).isEqualTo("updated-photo.jpg");
        assertThat(entity.getRestaurant()).isEqualTo(restaurant);
        assertThat(entity.isActive()).isTrue();
    }

    @Test
    void shouldCreateMenuItemEntityWithBuilder() {
        UUID id = UUID.randomUUID();
        BigDecimal price = BigDecimal.valueOf(25.00);
        RestaurantEntity restaurant = RestaurantEntity.builder()
                .id(UUID.randomUUID())
                .build();

        MenuItemEntity entity = MenuItemEntity.builder()
                .id(id)
                .name("Builder Item")
                .description("Builder Description")
                .price(price)
                .availableForInStoreOnly(false)
                .photoUrl("builder-photo.jpg")
                .active(true)
                .restaurant(restaurant)
                .build();

        assertThat(entity.getId()).isEqualTo(id);
        assertThat(entity.getName()).isEqualTo("Builder Item");
        assertThat(entity.getDescription()).isEqualTo("Builder Description");
        assertThat(entity.getPrice()).isEqualTo(price);
        assertThat(entity.isAvailableForInStoreOnly()).isFalse();
        assertThat(entity.getPhotoUrl()).isEqualTo("builder-photo.jpg");
        assertThat(entity.getRestaurant()).isEqualTo(restaurant);
        assertThat(entity.isActive()).isTrue();
    }
}
