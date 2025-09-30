package com.fiap.foodfiapp.model;

import org.junit.jupiter.api.Test;
import java.time.OffsetDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class MenuItemResponseTest {

    @Test
    void shouldCreateMenuItemResponseWithAllFields() {
        UUID id = UUID.randomUUID();
        UUID restaurantId = UUID.randomUUID();
        OffsetDateTime now = OffsetDateTime.now();

        MenuItemResponse response = new MenuItemResponse()
                .id(id)
                .name("Test Menu Item")
                .description("Test Description")
                .price(25.99)
                .availableForInStoreOnly(true)
                .photoUrl("http://example.com/photo.jpg")
                .restaurantId(restaurantId)
                .createdAt(now)
                .updatedAt(now);

        assertThat(response.getId()).isEqualTo(id);
        assertThat(response.getName()).isEqualTo("Test Menu Item");
        assertThat(response.getDescription()).isEqualTo("Test Description");
        assertThat(response.getPrice()).isEqualTo(25.99);
        assertThat(response.getAvailableForInStoreOnly()).isTrue();
        assertThat(response.getPhotoUrl()).isEqualTo("http://example.com/photo.jpg");
        assertThat(response.getRestaurantId()).isEqualTo(restaurantId);
        assertThat(response.getCreatedAt()).isEqualTo(now);
        assertThat(response.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    void shouldHandleNullValues() {
        MenuItemResponse response = new MenuItemResponse();

        assertThat(response.getId()).isNull();
        assertThat(response.getName()).isNull();
        assertThat(response.getDescription()).isNull();
        assertThat(response.getPrice()).isNull();
        assertThat(response.getAvailableForInStoreOnly()).isNull();
        assertThat(response.getPhotoUrl()).isNull();
        assertThat(response.getRestaurantId()).isNull();
        assertThat(response.getCreatedAt()).isNull();
        assertThat(response.getUpdatedAt()).isNull();
    }

    @Test
    void shouldSupportFluentInterface() {
        MenuItemResponse response = new MenuItemResponse()
                .id(UUID.randomUUID())
                .name("Fluent Test")
                .price(15.50);

        assertThat(response).isNotNull();
        assertThat(response.getName()).isEqualTo("Fluent Test");
        assertThat(response.getPrice()).isEqualTo(15.50);
    }

    @Test
    void shouldImplementEqualsAndHashCode() {
        UUID id = UUID.randomUUID();
        MenuItemResponse response1 = new MenuItemResponse().id(id).name("Test");
        MenuItemResponse response2 = new MenuItemResponse().id(id).name("Test");

        assertThat(response1).isEqualTo(response2);
        assertThat(response1.hashCode()).isEqualTo(response2.hashCode());
    }

    @Test
    void shouldImplementToString() {
        MenuItemResponse response = new MenuItemResponse().name("Test Item");

        assertThat(response.toString()).contains("Test Item");
    }
}
