package com.fiap.foodfiapp.core.domain.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class MenuItemTest {

    @Test
    void shouldCreateMenuItemWithAllFields() {
        UUID id = UUID.randomUUID();
        UUID restaurantId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        BigDecimal price = BigDecimal.valueOf(25.99);

        MenuItem menuItem = new MenuItem(
                id,
                "Test Menu Item",
                "Test Description",
                price,
                false,
                "photo-url.jpg",
                restaurantId,
                now,
                now
        );

        assertThat(menuItem.id()).isEqualTo(id);
        assertThat(menuItem.name()).isEqualTo("Test Menu Item");
        assertThat(menuItem.description()).isEqualTo("Test Description");
        assertThat(menuItem.price()).isEqualTo(price);
        assertThat(menuItem.localOnly()).isFalse();
        assertThat(menuItem.photoUrl()).isEqualTo("photo-url.jpg");
        assertThat(menuItem.restaurantId()).isEqualTo(restaurantId);
        assertThat(menuItem.createdAt()).isEqualTo(now);
        assertThat(menuItem.updatedAt()).isEqualTo(now);
    }

    @Test
    void shouldCreateMenuItemWithNullValues() {
        MenuItem menuItem = new MenuItem(
                null,
                null,
                null,
                null,
                true,
                null,
                null,
                null,
                null
        );

        assertThat(menuItem.id()).isNull();
        assertThat(menuItem.name()).isNull();
        assertThat(menuItem.description()).isNull();
        assertThat(menuItem.price()).isNull();
        assertThat(menuItem.localOnly()).isTrue();
        assertThat(menuItem.photoUrl()).isNull();
        assertThat(menuItem.restaurantId()).isNull();
        assertThat(menuItem.createdAt()).isNull();
        assertThat(menuItem.updatedAt()).isNull();
    }

    @Test
    void shouldUpdatePhotoUrlWithWithPhotoUrlMethod() {
        MenuItem original = new MenuItem(
                UUID.randomUUID(),
                "Test Item",
                "Description",
                BigDecimal.valueOf(10.00),
                false,
                null,
                UUID.randomUUID(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        MenuItem updated = original.withPhotoUrl("new-photo-url.jpg");

        assertThat(updated.photoUrl()).isEqualTo("new-photo-url.jpg");
        assertThat(updated.id()).isEqualTo(original.id());
        assertThat(updated.name()).isEqualTo(original.name());
        assertThat(updated.description()).isEqualTo(original.description());
        assertThat(updated.price()).isEqualTo(original.price());
        assertThat(updated.localOnly()).isEqualTo(original.localOnly());
        assertThat(updated.restaurantId()).isEqualTo(original.restaurantId());
        assertThat(updated.createdAt()).isEqualTo(original.createdAt());
        assertThat(updated.updatedAt()).isEqualTo(original.updatedAt());
    }

    @Test
    void shouldImplementEqualsAndHashCode() {
        UUID id = UUID.randomUUID();
        UUID restaurantId = UUID.randomUUID();
        LocalDateTime timestamp = LocalDateTime.now();

        MenuItem menuItem1 = new MenuItem(
                id,
                "Test Item",
                "Description",
                BigDecimal.valueOf(15.00),
                false,
                "photo.jpg",
                restaurantId,
                timestamp,
                timestamp
        );

        MenuItem menuItem2 = new MenuItem(
                id,
                "Test Item",
                "Description",
                BigDecimal.valueOf(15.00),
                false,
                "photo.jpg",
                restaurantId,
                timestamp,
                timestamp
        );

        assertThat(menuItem1).isEqualTo(menuItem2);
        assertThat(menuItem1.hashCode()).isEqualTo(menuItem2.hashCode());
    }

    @Test
    void shouldImplementToString() {
        MenuItem menuItem = new MenuItem(
                UUID.randomUUID(),
                "Test Item",
                "Description",
                BigDecimal.valueOf(20.00),
                true,
                "photo.jpg",
                UUID.randomUUID(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        String result = menuItem.toString();

        assertThat(result).contains("Test Item");
        assertThat(result).contains("Description");
        assertThat(result).contains("20.0");
    }
}
