package com.fiap.foodfiapp.infrastructure.rest.dto;

import com.fiap.foodfiapp.model.UserResponse;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserResponseDTOTest {
    @Test
    void shouldCreateAndSetFields() {
        UserResponse dto = new UserResponse();
        UUID userId = UUID.randomUUID();
        dto.setId(userId);
        dto.setName("Test");
        dto.setEmail("test@email.com");

        assertEquals(userId, dto.getId());
        assertEquals("Test", dto.getName());
        assertEquals("test@email.com", dto.getEmail());
    }

    @Test
    void shouldCreateWithAllArgsConstructor() {
        UUID userId = UUID.randomUUID();
        UserResponse dto = new UserResponse();

        dto.setId(userId);
        dto.setName("Test");
        dto.setEmail("teste@gmail.com");
        dto.setCpf("123.456.789-01");
        dto.setLogin("otheruser");
        dto.setAddresses(Collections.emptyList());
        dto.setUserType("client");
        dto.setActive(true);

        assertEquals(userId, dto.getId());
        assertEquals("Test", dto.getName());
        assertEquals("teste@gmail.com", dto.getEmail());
        assertEquals("client", dto.getUserType());
    }
}

