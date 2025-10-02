package com.fiap.foodfiapp.infrastructure.rest.mapper;

import com.fiap.foodfiapp.core.domain.entity.UserType;
import com.fiap.foodfiapp.model.CreateUserTypeRequest;
import com.fiap.foodfiapp.model.UpdateUserTypeRequest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserTypeRequestMapperTest {

    @Test
    void shouldMapCreateUserTypeRequestToEntity() {
        CreateUserTypeRequest request = new CreateUserTypeRequest();
        request.setName("ADMIN");

        UserType userType = UserTypeRequestMapper.toEntity(request);

        assertThat(userType).isNotNull();
        assertThat(userType.getName()).isEqualTo("ADMIN");
        assertThat(userType.getUuid()).isNull();
    }

    @Test
    void shouldMapCreateUserTypeRequestWithClientRole() {
        CreateUserTypeRequest request = new CreateUserTypeRequest();
        request.setName("CLIENT");

        UserType userType = UserTypeRequestMapper.toEntity(request);

        assertThat(userType).isNotNull();
        assertThat(userType.getName()).isEqualTo("CLIENT");
    }

    @Test
    void shouldMapUpdateUserTypeRequestWithManagerRole() {
        UpdateUserTypeRequest request = new UpdateUserTypeRequest();
        request.setName("MANAGER");

        UserType userType = UserTypeRequestMapper.toEntity(request);

        assertThat(userType).isNotNull();
        assertThat(userType.getName()).isEqualTo("MANAGER");
    }

    @Test
    void shouldMapCreateUserTypeRequestWithNullName() {
        CreateUserTypeRequest request = new CreateUserTypeRequest();
        request.setName(null);

        UserType userType = UserTypeRequestMapper.toEntity(request);

        assertThat(userType).isNotNull();
        assertThat(userType.getName()).isNull();
    }

    @Test
    void shouldMapUpdateUserTypeRequestWithNullName() {
        UpdateUserTypeRequest request = new UpdateUserTypeRequest();
        request.setName(null);

        UserType userType = UserTypeRequestMapper.toEntity(request);

        assertThat(userType).isNotNull();
        assertThat(userType.getName()).isNull();
    }
}
