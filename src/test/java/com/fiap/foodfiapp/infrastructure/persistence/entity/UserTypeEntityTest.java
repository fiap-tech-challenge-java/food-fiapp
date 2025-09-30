package com.fiap.foodfiapp.infrastructure.persistence.entity;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class UserTypeEntityTest {

    @Test
    void shouldCreateUserTypeEntityWithAllFields() {
        UUID uuid = UUID.randomUUID();
        UserTypeEntity entity = new UserTypeEntity();
        entity.setUuid(uuid);
        entity.setName("CLIENT");

        assertThat(entity.getUuid()).isEqualTo(uuid);
        assertThat(entity.getName()).isEqualTo("CLIENT");
    }

    @Test
    void shouldCreateEmptyUserTypeEntity() {
        UserTypeEntity entity = new UserTypeEntity();

        assertThat(entity.getUuid()).isNull();
        assertThat(entity.getName()).isNull();
    }

    @Test
    void shouldSetAndGetAllFields() {
        UserTypeEntity entity = new UserTypeEntity();
        UUID uuid = UUID.randomUUID();

        entity.setUuid(uuid);
        entity.setName("ADMIN");

        assertThat(entity.getUuid()).isEqualTo(uuid);
        assertThat(entity.getName()).isEqualTo("ADMIN");
    }

    @Test
    void shouldHandleDifferentUserTypeNames() {
        UserTypeEntity clientEntity = new UserTypeEntity();
        clientEntity.setName("CLIENT");

        UserTypeEntity adminEntity = new UserTypeEntity();
        adminEntity.setName("ADMIN");

        UserTypeEntity ownerEntity = new UserTypeEntity();
        ownerEntity.setName("OWNER");

        assertThat(clientEntity.getName()).isEqualTo("CLIENT");
        assertThat(adminEntity.getName()).isEqualTo("ADMIN");
        assertThat(ownerEntity.getName()).isEqualTo("OWNER");
    }

    @Test
    void shouldImplementEqualsAndHashCode() {
        UUID uuid = UUID.randomUUID();
        UserTypeEntity entity1 = new UserTypeEntity();
        entity1.setUuid(uuid);
        entity1.setName("CLIENT");

        UserTypeEntity entity2 = new UserTypeEntity();
        entity2.setUuid(uuid);
        entity2.setName("CLIENT");

        assertThat(entity1).isEqualTo(entity2);
        assertThat(entity1.hashCode()).isEqualTo(entity2.hashCode());
    }

    @Test
    void shouldImplementToString() {
        UserTypeEntity entity = new UserTypeEntity();
        entity.setName("MANAGER");

        String result = entity.toString();

        assertThat(result).contains("MANAGER");
    }
}
