package com.fiap.foodfiapp.infrastructure.rest.dto;

import com.fiap.foodfiapp.infrastructure.rest.dto.user.UserRequestDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserRequestDTOTest {
    @Test
    void shouldCreateAndSetFields() {
        UserRequestDTO dto = new UserRequestDTO();
        dto.setName("Test");
        dto.setEmail("test@email.com");
        dto.setPassword("1234");

        assertEquals("Test", dto.getName());
        assertEquals("test@email.com", dto.getEmail());
        assertEquals("1234", dto.getPassword());
    }

    @Test
    void shouldCreateWithAllArgsConstructor() {
        UserRequestDTO dto = new UserRequestDTO("Other", "other@email.com", "pass");
        assertEquals("Other", dto.getName());
        assertEquals("other@email.com", dto.getEmail());
        assertEquals("pass", dto.getPassword());
    }
}

