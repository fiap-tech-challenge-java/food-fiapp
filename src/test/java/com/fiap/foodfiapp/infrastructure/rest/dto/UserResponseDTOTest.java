package com.fiap.foodfiapp.infrastructure.rest.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserResponseDTOTest {
    @Test
    void shouldCreateAndSetFields() {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(1L);
        dto.setName("Test");
        dto.setEmail("test@email.com");

        assertEquals(1L, dto.getId());
        assertEquals("Test", dto.getName());
        assertEquals("test@email.com", dto.getEmail());
    }

    @Test
    void shouldCreateWithAllArgsConstructor() {
        UserResponseDTO dto = new UserResponseDTO(2L, "Other", "other@email.com");
        assertEquals(2L, dto.getId());
        assertEquals("Other", dto.getName());
        assertEquals("other@email.com", dto.getEmail());
    }
}

