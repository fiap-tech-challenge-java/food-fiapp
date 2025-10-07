package com.fiap.foodfiapp.core.domain.entity;

import org.junit.jupiter.api.Test;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void shouldCreateEmptyUser() {
        User user = new User();

        assertNull(user.getId());
        assertNull(user.getName());
        assertNull(user.getEmail());
        assertNull(user.getCpf());
        assertNull(user.getLogin());
        assertNull(user.getUserType());
        assertNull(user.getPassword());
        assertNull(user.getAddresses());
    }

    @Test
    void shouldCreateCompleteUser() {
        UUID id = UUID.randomUUID();
        UserType userType = new UserType(UUID.randomUUID(), "CLIENT");
        List<Address> addresses = new ArrayList<>();
        OffsetDateTime now = OffsetDateTime.now();

        User user = new User(id, "João Silva", "joao@email.com", "joao.silva", "12345678901",
                           addresses, userType, true, now, now, "password123");

        assertEquals(id, user.getId());
        assertEquals("João Silva", user.getName());
        assertEquals("joao@email.com", user.getEmail());
        assertEquals("joao.silva", user.getLogin());
        assertEquals("12345678901", user.getCpf());
        assertEquals(addresses, user.getAddresses());
        assertEquals(userType, user.getUserType());
        assertTrue(user.getIsActive());
        assertEquals(now, user.getCreatedAt());
        assertEquals(now, user.getUpdatedAt());
        assertEquals("password123", user.getPassword());
    }

    @Test
    void shouldSetAllProperties() {
        User user = new User();
        UUID id = UUID.randomUUID();
        UserType userType = new UserType(UUID.randomUUID(), "ADMIN");
        Address address = new Address(UUID.randomUUID(), "Rua Test", "123", null, "Centro", "São Paulo", "SP", "01000-000");
        List<Address> addresses = Arrays.asList(address);

        user.setId(id);
        user.setName("Maria Santos");
        user.setEmail("maria@email.com");
        user.setLogin("maria.santos");
        user.setCpf("98765432100");
        user.setUserType(userType);
        user.setPassword("newpassword");
        user.setAddresses(addresses);

        assertEquals(id, user.getId());
        assertEquals("Maria Santos", user.getName());
        assertEquals("maria@email.com", user.getEmail());
        assertEquals("maria.santos", user.getLogin());
        assertEquals("98765432100", user.getCpf());
        assertEquals(userType, user.getUserType());
        assertEquals("newpassword", user.getPassword());
        assertEquals(addresses, user.getAddresses());
    }

    @Test
    void shouldHandleNullValues() {
        User user = new User();

        user.setId(null);
        user.setName(null);
        user.setEmail(null);
        user.setLogin(null);
        user.setCpf(null);
        user.setUserType(null);
        user.setPassword(null);
        user.setAddresses(null);

        assertNull(user.getId());
        assertNull(user.getName());
        assertNull(user.getEmail());
        assertNull(user.getLogin());
        assertNull(user.getCpf());
        assertNull(user.getUserType());
        assertNull(user.getPassword());
        assertNull(user.getAddresses());
    }

    @Test
    void shouldInheritFromBaseEntity() {
        User user = new User();
        OffsetDateTime now = OffsetDateTime.now();

        user.setCreatedAt(now);
        user.setUpdatedAt(now);
        user.setIsActive(false);

        assertEquals(now, user.getCreatedAt());
        assertEquals(now, user.getUpdatedAt());
        assertFalse(user.getIsActive());
    }

    @Test
    void shouldHandleEmptyAddressList() {
        UUID id = UUID.randomUUID();
        UserType userType = new UserType(UUID.randomUUID(), "CLIENT");
        List<Address> emptyAddresses = new ArrayList<>();
        OffsetDateTime now = OffsetDateTime.now();

        User user = new User(id, "Test User", "test@email.com", "test.user", "12345678901",
                           emptyAddresses, userType, true, now, now, "password");

        assertNotNull(user.getAddresses());
        assertTrue(user.getAddresses().isEmpty());
    }
}
