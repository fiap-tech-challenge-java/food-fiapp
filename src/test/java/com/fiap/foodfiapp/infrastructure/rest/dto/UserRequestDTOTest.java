package com.fiap.foodfiapp.infrastructure.rest.dto;

import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class UserRequestDTOTest {

    @Test
    void shouldCreateAndSetFields() {
        UserRequestDTO dto = new UserRequestDTO();
        dto.setName("Test");
        dto.setEmail("test@email.com");
        dto.setCpf("12345678901");
        dto.setLogin("test_login");
        dto.setPassword("1234");
        dto.setAddresses(Collections.emptyList());

        assertEquals("Test", dto.getName());
        assertEquals("test@email.com", dto.getEmail());
        assertEquals("12345678901", dto.getCpf());
        assertEquals("test_login", dto.getLogin());
        assertEquals("1234", dto.getPassword());
        assertNotNull(dto.getAddresses());
    }

    @Test
    void shouldCreateWithAllArgsConstructor() {
        UserRequestDTO dto = new UserRequestDTO("Other", "other@email.com", "09876543210", "other_login", "pass", Collections.emptyList(), "client", true);

        assertEquals("Other", dto.getName());
        assertEquals("other@email.com", dto.getEmail());
        assertEquals("09876543210", dto.getCpf());
        assertEquals("other_login", dto.getLogin());
        assertEquals("pass", dto.getPassword());
        assertNotNull(dto.getAddresses());
    }
}

