package com.fiap.foodfiapp.infrastructure.rest.dto;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserResponseDTOTest {
    @Test
    void shouldCreateAndSetFields() {
        UserResponseDTO dto = new UserResponseDTO();
        UUID userId = UUID.randomUUID();
        UUID userTypeId = UUID.randomUUID();

        dto.setId(userId);
        dto.setName("Test");
        dto.setEmail("test@email.com");
        dto.setUserTypeId(userTypeId);
        dto.setUserTypeName("Customer");

        assertEquals(userId, dto.getId());
        assertEquals("Test", dto.getName());
        assertEquals("test@email.com", dto.getEmail());
        assertEquals(userTypeId, dto.getUserTypeId());
        assertEquals("Customer", dto.getUserTypeName());
    }

    @Test
    void shouldCreateWithAllArgsConstructor() {
        UUID userId = UUID.randomUUID();
        UUID userTypeId = UUID.randomUUID();

        UserResponseDTO dto = new UserResponseDTO(
                userId,
                "Other User",
                "other@email.com",
                "123.456.789-01",
                "otheruser",
                Collections.emptyList(),
                userTypeId,
                "Customer",
                true
        );

        assertEquals(userId, dto.getId());
        assertEquals("Other User", dto.getName());
        assertEquals("other@email.com", dto.getEmail());
        assertEquals(userTypeId, dto.getUserTypeId());
        assertEquals("Customer", dto.getUserTypeName());
        assertTrue(dto.isActive());
    }
}
