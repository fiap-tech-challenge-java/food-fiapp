package com.fiap.foodfiapp.core.domain.entity;

import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    @Test
    void shouldCreateUserWithAllFields() {
        UUID id = UUID.randomUUID();
        OffsetDateTime now = OffsetDateTime.now();
        UserType userType = new UserType(UUID.randomUUID(), "CLIENT");
        Address address = new Address(UUID.randomUUID(), id.toString(), "Street 123", "Apt 1", "Downtown", "City", "State", "12345");

        User user = new User(id, "John Doe", "john@example.com", "johndoe", "12345678901",
                            Arrays.asList(address), userType, true, now, now, "password123");

        assertThat(user.getId()).isEqualTo(id);
        assertThat(user.getName()).isEqualTo("John Doe");
        assertThat(user.getEmail()).isEqualTo("john@example.com");
        assertThat(user.getLogin()).isEqualTo("johndoe");
        assertThat(user.getCpf()).isEqualTo("12345678901");
        assertThat(user.getAddresses()).hasSize(1);
        assertThat(user.getUserType()).isEqualTo(userType);
        assertThat(user.isActive()).isTrue();
        assertThat(user.getUpdatedAt()).isEqualTo(now);
        assertThat(user.getCreatedAt()).isEqualTo(now);
        assertThat(user.getPassword()).isEqualTo("password123");
    }

    @Test
    void shouldCreateEmptyUser() {
        User user = new User();

        assertThat(user.getId()).isNull();
        assertThat(user.getName()).isNull();
        assertThat(user.getEmail()).isNull();
        assertThat(user.getLogin()).isNull();
        assertThat(user.getCpf()).isNull();
        assertThat(user.getAddresses()).isNull();
        assertThat(user.getUserType()).isNull();
        assertThat(user.isActive()).isFalse();
        assertThat(user.getUpdatedAt()).isNull();
        assertThat(user.getCreatedAt()).isNull();
        assertThat(user.getPassword()).isNull();
    }

    @Test
    void shouldSetAndGetAllFields() {
        User user = new User();
        UUID id = UUID.randomUUID();
        OffsetDateTime now = OffsetDateTime.now();
        UserType userType = new UserType(UUID.randomUUID(), "ADMIN");

        user.setId(id);
        user.setName("Jane Doe");
        user.setEmail("jane@example.com");
        user.setLogin("janedoe");
        user.setCpf("98765432100");
        user.setAddresses(Collections.emptyList());
        user.setUserType(userType);
        user.setActive(false);
        user.setUpdatedAt(now);
        user.setCreatedAt(now);
        user.setPassword("newPassword");

        assertThat(user.getId()).isEqualTo(id);
        assertThat(user.getName()).isEqualTo("Jane Doe");
        assertThat(user.getEmail()).isEqualTo("jane@example.com");
        assertThat(user.getLogin()).isEqualTo("janedoe");
        assertThat(user.getCpf()).isEqualTo("98765432100");
        assertThat(user.getAddresses()).isEmpty();
        assertThat(user.getUserType()).isEqualTo(userType);
        assertThat(user.isActive()).isFalse();
        assertThat(user.getUpdatedAt()).isEqualTo(now);
        assertThat(user.getCreatedAt()).isEqualTo(now);
        assertThat(user.getPassword()).isEqualTo("newPassword");
    }

    @Test
    void shouldHandleNullUserType() {
        User user = new User(UUID.randomUUID(), "Test User", "test@example.com", "testuser",
                            "12345678901", Collections.emptyList(), null, true,
                            OffsetDateTime.now(), OffsetDateTime.now(), "password");

        assertThat(user.getUserType()).isNull();
    }

    @Test
    void shouldHandleEmptyAddressList() {
        User user = new User(UUID.randomUUID(), "Test User", "test@example.com", "testuser",
                            "12345678901", Collections.emptyList(), new UserType(UUID.randomUUID(), "CLIENT"),
                            true, OffsetDateTime.now(), OffsetDateTime.now(), "password");

        assertThat(user.getAddresses()).isEmpty();
    }
}
