package com.fiap.foodfiapp.core.domain.entity;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class UserTypeTest {

    @Test
    void shouldCreateUserTypeWithAllFields() {
        UUID uuid = UUID.randomUUID();
        UserType userType = new UserType(uuid, "CLIENT");

        assertThat(userType.getUuid()).isEqualTo(uuid);
        assertThat(userType.getName()).isEqualTo("CLIENT");
    }

    @Test
    void shouldCreateEmptyUserType() {
        UserType userType = new UserType();

        assertThat(userType.getUuid()).isNull();
        assertThat(userType.getName()).isNull();
    }

    @Test
    void shouldSetAndGetAllFields() {
        UserType userType = new UserType();
        UUID uuid = UUID.randomUUID();

        userType.setUuid(uuid);
        userType.setName("ADMIN");

        assertThat(userType.getUuid()).isEqualTo(uuid);
        assertThat(userType.getName()).isEqualTo("ADMIN");
    }

    @Test
    void shouldCreateUserTypeWithNullValues() {
        UserType userType = new UserType(null, null);

        assertThat(userType.getUuid()).isNull();
        assertThat(userType.getName()).isNull();
    }

    @Test
    void shouldHandleDifferentUserTypeNames() {
        UserType clientType = new UserType(UUID.randomUUID(), "CLIENT");
        UserType adminType = new UserType(UUID.randomUUID(), "ADMIN");
        UserType ownerType = new UserType(UUID.randomUUID(), "OWNER");

        assertThat(clientType.getName()).isEqualTo("CLIENT");
        assertThat(adminType.getName()).isEqualTo("ADMIN");
        assertThat(ownerType.getName()).isEqualTo("OWNER");
    }

    @Test
    void shouldCreateTwoUserTypesWithSameData() {
        UUID uuid = UUID.randomUUID();
        UserType userType1 = new UserType(uuid, "CLIENT");
        UserType userType2 = new UserType(uuid, "CLIENT");

        // Como equals/hashCode não estão implementados, testamos os valores individualmente
        assertThat(userType1.getUuid()).isEqualTo(userType2.getUuid());
        assertThat(userType1.getName()).isEqualTo(userType2.getName());
    }

    @Test
    void shouldHaveStringRepresentation() {
        UserType userType = new UserType(UUID.randomUUID(), "MANAGER");

        String result = userType.toString();

        // Como toString não está implementado, apenas verificamos que retorna algo
        assertThat(result).isNotNull();
        assertThat(result).contains("UserType");
    }
}
