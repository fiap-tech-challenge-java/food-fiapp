package com.fiap.foodfiapp.core.domain.entity;

import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class RestaurantTest {

    @Test
    void shouldCreateEmptyRestaurant() {
        Restaurant restaurant = new Restaurant();

        assertNull(restaurant.getId());
        assertNull(restaurant.getName());
        assertNull(restaurant.getCuisineType());
        assertNull(restaurant.getOpeningHours());
        assertNull(restaurant.getUserOwnerId());
        assertNull(restaurant.getActive());
        assertNull(restaurant.getAddress());
    }

    @Test
    void shouldCreateCompleteRestaurant() {
        UUID id = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();
        Address address = new Address(UUID.randomUUID(), "Rua da Comida", "100", "Loja 1", "Centro", "São Paulo", "SP", "01000-000");

        Restaurant restaurant = new Restaurant(id, "Pizzaria Bella", "Italiana", "18:00-23:00", ownerId, true, address);

        assertEquals(id, restaurant.getId());
        assertEquals("Pizzaria Bella", restaurant.getName());
        assertEquals("Italiana", restaurant.getCuisineType());
        assertEquals("18:00-23:00", restaurant.getOpeningHours());
        assertEquals(ownerId, restaurant.getUserOwnerId());
        assertTrue(restaurant.getActive());
        assertEquals(address, restaurant.getAddress());
    }

    @Test
    void shouldSetAllProperties() {
        Restaurant restaurant = new Restaurant();
        UUID id = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();
        Address address = new Address(UUID.randomUUID(), "Avenida Gastronômica", "200", null, "Vila Olímpia", "São Paulo", "SP", "04551-000");

        restaurant.setId(id);
        restaurant.setName("Sushi Bar Tokyo");
        restaurant.setCuisineType("Japonesa");
        restaurant.setOpeningHours("19:00-00:00");
        restaurant.setUserOwnerId(ownerId);
        restaurant.setActive(true);
        restaurant.setAddress(address);

        assertEquals(id, restaurant.getId());
        assertEquals("Sushi Bar Tokyo", restaurant.getName());
        assertEquals("Japonesa", restaurant.getCuisineType());
        assertEquals("19:00-00:00", restaurant.getOpeningHours());
        assertEquals(ownerId, restaurant.getUserOwnerId());
        assertTrue(restaurant.getActive());
        assertEquals(address, restaurant.getAddress());
    }

    @Test
    void shouldHandleNullValues() {
        Restaurant restaurant = new Restaurant();

        restaurant.setId(null);
        restaurant.setName(null);
        restaurant.setCuisineType(null);
        restaurant.setOpeningHours(null);
        restaurant.setUserOwnerId(null);
        restaurant.setActive(null);
        restaurant.setAddress(null);

        assertNull(restaurant.getId());
        assertNull(restaurant.getName());
        assertNull(restaurant.getCuisineType());
        assertNull(restaurant.getOpeningHours());
        assertNull(restaurant.getUserOwnerId());
        assertNull(restaurant.getActive());
        assertNull(restaurant.getAddress());
    }

    @Test
    void shouldToggleActiveStatus() {
        Restaurant restaurant = new Restaurant();

        restaurant.setActive(true);
        assertTrue(restaurant.getActive());

        restaurant.setActive(false);
        assertFalse(restaurant.getActive());
    }

    @Test
    void shouldInheritFromBaseEntity() {
        Restaurant restaurant = new Restaurant();

        assertTrue(restaurant.getIsActive());

        restaurant.setIsActive(false);
        assertFalse(restaurant.getIsActive());
    }

    @Test
    void shouldCreateRestaurantWithNullAddress() {
        UUID id = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();

        Restaurant restaurant = new Restaurant(id, "Lanchonete Express", "Fast Food", "10:00-22:00", ownerId, true, null);

        assertNull(restaurant.getAddress());
    }

    @Test
    void shouldCreateRestaurantWithDifferentCuisineTypes() {
        UUID id = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();

        Restaurant restaurant = new Restaurant(id, "Taco Bell", "Mexicana", "11:00-22:00", ownerId, true, null);
        assertEquals("Mexicana", restaurant.getCuisineType());

        restaurant.setCuisineType("Brasileira");
        assertEquals("Brasileira", restaurant.getCuisineType());
    }

    @Test
    void shouldHandleEmptyStrings() {
        Restaurant restaurant = new Restaurant();

        restaurant.setName("");
        restaurant.setCuisineType("");
        restaurant.setOpeningHours("");

        assertEquals("", restaurant.getName());
        assertEquals("", restaurant.getCuisineType());
        assertEquals("", restaurant.getOpeningHours());
    }
}
