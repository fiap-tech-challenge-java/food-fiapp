package com.fiap.foodfiapp.infrastructure.persistence.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserEntityTest {
    @Test
    void shouldCreateAndSetFields() {
        UserEntity entity = new UserEntity();
        entity.setId(1L);
        entity.setName("Test");
        entity.setEmail("test@email.com");
        entity.setPassword("1234");

        assertEquals(1L, entity.getId());
        assertEquals("Test", entity.getName());
        assertEquals("test@email.com", entity.getEmail());
        assertEquals("1234", entity.getPassword());
    }

    @Test
    void shouldCreateWithAllArgsConstructor() {
        UserEntity entity = new UserEntity(2L, "Other", "other@email.com", "pass");
        assertEquals(2L, entity.getId());
        assertEquals("Other", entity.getName());
        assertEquals("other@email.com", entity.getEmail());
        assertEquals("pass", entity.getPassword());
    }
}

