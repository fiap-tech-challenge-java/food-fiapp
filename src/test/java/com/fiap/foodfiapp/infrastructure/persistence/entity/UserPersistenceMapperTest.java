package com.fiap.foodfiapp.infrastructure.persistence.entity;

import com.fiap.foodfiapp.core.domain.entity.Address;
import com.fiap.foodfiapp.core.domain.entity.User;
import com.fiap.foodfiapp.core.domain.entity.UserType;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class UserPersistenceMapperTest {

    @Test
    void shouldMapUserToEntity() {
        UUID userId = UUID.randomUUID();
        UUID userTypeId = UUID.randomUUID();
        UUID addressId = UUID.randomUUID();

        UserType userType = new UserType(userTypeId, "CUSTOMER");
        Address address = new Address(addressId, "Rua das Flores", "123", "Apt 1", "Centro", "São Paulo", "SP", "12345-678");

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

        UserEntity entity = UserPersistenceMapper.toEntity(user);

        assertThat(entity.getId()).isEqualTo(userId);
        assertThat(entity.getName()).isEqualTo("John Doe");
        assertThat(entity.getEmail()).isEqualTo("john@example.com");
        assertThat(entity.getLogin()).isEqualTo("johndoe");
        assertThat(entity.getCpf()).isEqualTo("12345678901");
        assertThat(entity.isActive()).isTrue();
        assertThat(entity.getPassword()).isEqualTo("password123");
        assertThat(entity.getUserType()).isNotNull();
        assertThat(entity.getUserType().getUuid()).isEqualTo(userTypeId);
        assertThat(entity.getUserType().getName()).isEqualTo("CUSTOMER");
        assertThat(entity.getAddressesList()).hasSize(1);
        assertThat(entity.getAddressesList().get(0).getId()).isEqualTo(addressId);
    }

    @Test
    void shouldMapEntityToUser() {
        UUID userId = UUID.randomUUID();
        UUID userTypeId = UUID.randomUUID();
        UUID addressId = UUID.randomUUID();

        UserTypeEntity userTypeEntity = UserTypeEntity.builder()
                .uuid(userTypeId)
                .name("CUSTOMER")
                .build();

        AddressEntity addressEntity = new AddressEntity(
                addressId,
                null,
                "Rua das Flores",
                "123",
                "Apt 1",
                "Centro",
                "São Paulo",
                "SP",
                "12345-678",
                true
        );

        UserEntity entity = UserEntity.builder()
                .id(userId)
                .name("John Doe")
                .email("john@example.com")
                .login("johndoe")
                .cpf("12345678901")
                .password("password123")
                .active(true)
                .userType(userTypeEntity)
                .addressesList(Arrays.asList(addressEntity))
                .build();

        User user = UserPersistenceMapper.toDomain(entity);

        assertThat(user.getId()).isEqualTo(userId);
        assertThat(user.getName()).isEqualTo("John Doe");
        assertThat(user.getEmail()).isEqualTo("john@example.com");
        assertThat(user.getLogin()).isEqualTo("johndoe");
        assertThat(user.getCpf()).isEqualTo("12345678901");
        assertThat(user.isActive()).isTrue();
        assertThat(user.getPassword()).isEqualTo("password123");
        assertThat(user.getUserType()).isNotNull();
        assertThat(user.getUserType().getUuid()).isEqualTo(userTypeId);
        assertThat(user.getUserType().getName()).isEqualTo("CUSTOMER");
        assertThat(user.getAddresses()).hasSize(1);
        assertThat(user.getAddresses().get(0).getId()).isEqualTo(addressId);
    }

    @Test
    void shouldReturnNullWhenUserIsNull() {
        UserEntity entity = UserPersistenceMapper.toEntity(null);

        assertThat(entity).isNull();
    }

    @Test
    void shouldReturnNullWhenEntityIsNull() {
        User user = UserPersistenceMapper.toDomain(null);

        assertThat(user).isNull();
    }

    @Test
    void shouldMapUserWithNullAddresses() {
        UUID userId = UUID.randomUUID();
        UUID userTypeId = UUID.randomUUID();

        UserType userType = new UserType(userTypeId, "CUSTOMER");

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

        UserEntity entity = UserPersistenceMapper.toEntity(user);

        assertThat(entity.getAddressesList()).isNull();
    }

    @Test
    void shouldMapEntityWithEmptyAddresses() {
        UUID userId = UUID.randomUUID();
        UUID userTypeId = UUID.randomUUID();

        UserTypeEntity userTypeEntity = UserTypeEntity.builder()
                .uuid(userTypeId)
                .name("CUSTOMER")
                .build();

        UserEntity entity = UserEntity.builder()
                .id(userId)
                .name("John Doe")
                .email("john@example.com")
                .login("johndoe")
                .cpf("12345678901")
                .password("password123")
                .active(true)
                .userType(userTypeEntity)
                .addressesList(Collections.emptyList())
                .build();

        User user = UserPersistenceMapper.toDomain(entity);

        assertThat(user.getAddresses()).isEmpty();
    }
}
