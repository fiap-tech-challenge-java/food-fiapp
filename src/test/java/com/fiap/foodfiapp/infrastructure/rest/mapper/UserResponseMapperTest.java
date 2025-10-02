package com.fiap.foodfiapp.infrastructure.rest.mapper;

import com.fiap.foodfiapp.core.domain.entity.Address;
import com.fiap.foodfiapp.core.domain.entity.User;
import com.fiap.foodfiapp.core.domain.entity.UserType;
import com.fiap.foodfiapp.model.UserResponse;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class UserResponseMapperTest {

    @Test
    void shouldMapUserToUserResponse() {
        UUID userId = UUID.randomUUID();
        UUID userTypeId = UUID.randomUUID();
        UserType userType = new UserType(userTypeId, "CUSTOMER");

        User user = new User(
                userId,
                "John Doe",
                "john@example.com",
                "johndoe",
                "12345678901",
                Collections.emptyList(),
                userType,
                true,
                OffsetDateTime.now(),
                OffsetDateTime.now(),
                "password123"
        );

        UserResponse result = UserResponseMapper.toDTO(user);

        assertThat(result.getId()).isEqualTo(userId);
        assertThat(result.getName()).isEqualTo("John Doe");
        assertThat(result.getEmail()).isEqualTo("john@example.com");
        assertThat(result.getLogin()).isEqualTo("johndoe");
        assertThat(result.getCpf()).isEqualTo("12345678901");
        assertThat(result.getUserType()).isEqualTo("CUSTOMER");
        assertThat(result.getActive()).isTrue();
        assertThat(result.getAddresses()).isEmpty();
    }

    @Test
    void shouldMapUserWithNullUserType() {
        UUID userId = UUID.randomUUID();

        User user = new User(
                userId,
                "John Doe",
                "john@example.com",
                "johndoe",
                "12345678901",
                Collections.emptyList(),
                null,
                true,
                OffsetDateTime.now(),
                OffsetDateTime.now(),
                "password123"
        );

        UserResponse result = UserResponseMapper.toDTO(user);

        assertThat(result.getId()).isEqualTo(userId);
        assertThat(result.getUserType()).isNull();
    }

    @Test
    void shouldMapUserWithNullAddresses() {
        UUID userId = UUID.randomUUID();
        UserType userType = new UserType(UUID.randomUUID(), "CUSTOMER");

        User user = new User(
                userId,
                "John Doe",
                "john@example.com",
                "johndoe",
                "12345678901",
                null,
                userType,
                true,
                OffsetDateTime.now(),
                OffsetDateTime.now(),
                "password123"
        );

        UserResponse result = UserResponseMapper.toDTO(user);

        assertThat(result.getAddresses()).isNull();
    }

    @Test
    void shouldMapUserWithAddresses() {
        UUID userId = UUID.randomUUID();
        UUID addressId = UUID.randomUUID();
        UserType userType = new UserType(UUID.randomUUID(), "CUSTOMER");

        Address address = new Address(
                addressId,
                "Street 123",
                "45",
                "Apt 45",
                "Downtown",
                "City",
                "State",
                "12345-678"
        );

        User user = new User(
                userId,
                "John Doe",
                "john@example.com",
                "johndoe",
                "12345678901",
                Arrays.asList(address),
                userType,
                true,
                OffsetDateTime.now(),
                OffsetDateTime.now(),
                "password123"
        );

        UserResponse result = UserResponseMapper.toDTO(user);

        assertThat(result.getAddresses()).hasSize(1);
        assertThat(result.getAddresses().get(0).getId()).isEqualTo(addressId);
        assertThat(result.getAddresses().get(0).getPublicPlace()).isEqualTo("Street 123");
    }

    @Test
    void shouldHandleInactiveUser() {
        UUID userId = UUID.randomUUID();
        UserType userType = new UserType(UUID.randomUUID(), "CUSTOMER");

        User user = new User(
                userId,
                "John Doe",
                "john@example.com",
                "johndoe",
                "12345678901",
                Collections.emptyList(),
                userType,
                false,
                OffsetDateTime.now(),
                OffsetDateTime.now(),
                "password123"
        );

        UserResponse result = UserResponseMapper.toDTO(user);

        assertThat(result.getActive()).isFalse();
    }
}
