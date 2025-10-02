package com.fiap.foodfiapp.infrastructure.rest.mapper;

import com.fiap.foodfiapp.core.domain.entity.UserType;
import com.fiap.foodfiapp.model.UserTypeRequest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserTypeRequestMapperTest {

    @Test
    void shouldMapUserTypeRequestToEntity() {
        UserTypeRequest request = new UserTypeRequest();
        request.setName("ADMIN");

        UserType userType = UserTypeRequestMapper.toEntity(request);

        assertThat(userType).isNotNull();
        assertThat(userType.getName()).isEqualTo("ADMIN");
        assertThat(userType.getUuid()).isNull(); // UUID is not set in the mapper
    }

    @Test
    void shouldMapUserTypeRequestWithClientRole() {
        UserTypeRequest request = new UserTypeRequest();
        request.setName("CUSTOMER");

        UserType userType = UserTypeRequestMapper.toEntity(request);

        assertThat(userType).isNotNull();
        assertThat(userType.getName()).isEqualTo("CUSTOMER");
    }

    @Test
    void shouldMapUpdateUserTypeRequestWithManagerRole() {
        UserTypeRequest request = new UserTypeRequest();
        request.setName("OWNER");

        UserType userType = UserTypeRequestMapper.toEntity(request);

        assertThat(userType).isNotNull();
        assertThat(userType.getName()).isEqualTo("OWNER");
    }

    @Test
    void shouldMapUserTypeRequestWithNullName() {
        UserTypeRequest request = new UserTypeRequest();
        request.setName(null);

        UserType userType = UserTypeRequestMapper.toEntity(request);

        assertThat(userType).isNotNull();
        assertThat(userType.getName()).isNull();
    }

    @Test
    void shouldMapUserTypeRequestWithEmptyName() {
        UserTypeRequest request = new UserTypeRequest();
        request.setName("");

        UserType userType = UserTypeRequestMapper.toEntity(request);

        assertThat(userType).isNotNull();
        assertThat(userType.getName()).isEmpty();
    }
}
