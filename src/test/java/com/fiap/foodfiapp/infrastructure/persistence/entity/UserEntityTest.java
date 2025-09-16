package com.fiap.foodfiapp.infrastructure.persistence.entity;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserEntityTest {
    @Test
    void shouldCreateAndSetFields() {
        UserEntity entity = new UserEntity();
        UUID userId = UUID.randomUUID();
        UserTypeEntity userType = new UserTypeEntity();
        List<AddressEntity> addresses = new ArrayList<>();

        entity.setId(userId);
        entity.setName("Test User");
        entity.setCpf("12345678901");
        entity.setEmail("test@email.com");
        entity.setLogin("test_login");
        entity.setPassword("password123");
        entity.setUserType(userType);
        entity.setActive(true);
        entity.setAddressesList(addresses);

        assertEquals(userId, entity.getId());
        assertEquals("Test User", entity.getName());
        assertEquals("12345678901", entity.getCpf());
        assertEquals("test@email.com", entity.getEmail());
        assertEquals("test_login", entity.getLogin());
        assertEquals("password123", entity.getPassword());
        assertEquals(userType, entity.getUserType());
        assertTrue(entity.isActive());
        assertEquals(addresses, entity.getAddressesList());
    }

    @Test
    void shouldCreateWithAllArgsConstructor() {
        UUID userId = UUID.randomUUID();
        UserTypeEntity userType = new UserTypeEntity();
        List<AddressEntity> addresses = Collections.emptyList();

        UserEntity entity = new UserEntity(
                userId,
                "Other User",
                "09876543210",
                "other@email.com",
                "other_login",
                "other_password",
                userType,
                false,
                addresses
        );

        assertEquals(userId, entity.getId());
        assertEquals("Other User", entity.getName());
        assertEquals("09876543210", entity.getCpf());
        assertEquals("other@email.com", entity.getEmail());
        assertEquals("other_login", entity.getLogin());
        assertEquals("other_password", entity.getPassword());
        assertEquals(userType, entity.getUserType());
        assertFalse(entity.isActive());
        assertEquals(addresses, entity.getAddressesList());
    }

    @Test
    void shouldCreateWithBuilder() {
        UUID userId = UUID.randomUUID();
        UserTypeEntity userType = new UserTypeEntity();
        List<AddressEntity> addresses = Collections.emptyList();

        UserEntity entity = UserEntity.builder()
                .id(userId)
                .name("Builder User")
                .cpf("11122233344")
                .email("builder@email.com")
                .login("builder_login")
                .password("builder_pass")
                .userType(userType)
                .active(true)
                .addressesList(addresses)
                .build();

        assertEquals(userId, entity.getId());
        assertEquals("Builder User", entity.getName());
        assertEquals("11122233344", entity.getCpf());
        assertEquals("builder@email.com", entity.getEmail());
        assertEquals("builder_login", entity.getLogin());
        assertEquals("builder_pass", entity.getPassword());
        assertEquals(userType, entity.getUserType());
        assertTrue(entity.isActive());
        assertEquals(addresses, entity.getAddressesList());
    }

    @Test
    void shouldHaveDefaultNoArgsConstructor() {
        UserEntity entity = new UserEntity();
        assertNull(entity.getId());
        assertNull(entity.getName());
        assertNull(entity.getCpf());
        assertNull(entity.getEmail());
        assertNull(entity.getLogin());
        assertNull(entity.getPassword());
        assertNull(entity.getUserType());
        assertFalse(entity.isActive());
        assertNotNull(entity.getAddressesList());
        assertTrue(entity.getAddressesList().isEmpty());
    }
}

