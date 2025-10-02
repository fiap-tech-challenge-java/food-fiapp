package com.fiap.foodfiapp.infrastructure.rest.mapper;

import com.fiap.foodfiapp.core.domain.entity.UserType;
import com.fiap.foodfiapp.model.UserTypeResponse;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class UserTypeResponseMapperTest {

    @Test
    void shouldMapUserTypeToResponse() {
        UUID uuid = UUID.randomUUID();
        UserType userType = new UserType(uuid, "CUSTOMER");

        UserTypeResponse response = UserTypeResponseMapper.toDTO(userType);

        assertThat(response).isNotNull();
        assertThat(response.getUuid()).isEqualTo(uuid);
        assertThat(response.getName()).isEqualTo("CUSTOMER");
    }

    @Test
    void shouldMapUserTypeWithClientRole() {
        UUID uuid = UUID.randomUUID();
        UserType userType = new UserType(uuid, "CUSTOMER");

        UserTypeResponse response = UserTypeResponseMapper.toDTO(userType);

        assertThat(response).isNotNull();
        assertThat(response.getUuid()).isEqualTo(uuid);
        assertThat(response.getName()).isEqualTo("CUSTOMER");
    }

    @Test
    void shouldMapUserTypeWithManagerRole() {
        UUID uuid = UUID.randomUUID();
        UserType userType = new UserType(uuid, "OWNER");

        UserTypeResponse response = UserTypeResponseMapper.toDTO(userType);

        assertThat(response).isNotNull();
        assertThat(response.getUuid()).isEqualTo(uuid);
        assertThat(response.getName()).isEqualTo("OWNER");
    }

    @Test
    void shouldMapUserTypeWithNullUuid() {
        UserType userType = new UserType(null, "ADMIN");

        UserTypeResponse response = UserTypeResponseMapper.toDTO(userType);

        assertThat(response).isNotNull();
        assertThat(response.getUuid()).isNull();
        assertThat(response.getName()).isEqualTo("ADMIN");
    }

    @Test
    void shouldMapUserTypeWithNullName() {
        UUID uuid = UUID.randomUUID();
        UserType userType = new UserType(uuid, null);

        UserTypeResponse response = UserTypeResponseMapper.toDTO(userType);

        assertThat(response).isNotNull();
        assertThat(response.getUuid()).isEqualTo(uuid);
        assertThat(response.getName()).isNull();
    }
}
