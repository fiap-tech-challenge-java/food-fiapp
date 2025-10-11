package com.fiap.foodfiapp.infrastructure.rest.mapper;

import com.fiap.foodfiapp.core.domain.entity.UserType;
import com.fiap.foodfiapp.model.CreateUserTypeRequest;
import com.fiap.foodfiapp.model.UpdateUserTypeRequest;
import com.fiap.foodfiapp.model.UserTypeResponse;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class UserTypeMapperTest {

    private final UserTypeMapper mapper = UserTypeMapper.INSTANCE;

    @Test
    void shouldMapCreateUserTypeRequestToUserType() {
        // Given
        CreateUserTypeRequest request = new CreateUserTypeRequest();
        request.setName("ADMIN");

        // When
        UserType result = mapper.toUserType(request);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("ADMIN");
    }

    @Test
    void shouldMapUpdateUserTypeRequestToUserType() {
        // Given
        UpdateUserTypeRequest request = new UpdateUserTypeRequest();
        request.setName("CLIENT");

        // When
        UserType result = mapper.toUserType(request);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("CLIENT");
    }

    @Test
    void shouldMapUserTypeToUserTypeResponse() {
        // Given
        UserType userType = new UserType();
        userType.setUuid(UUID.randomUUID());
        userType.setName("ADMIN");
        userType.setIsActive(true);
        userType.setCreatedAt(OffsetDateTime.now());
        userType.setUpdatedAt(OffsetDateTime.now());

        // When
        UserTypeResponse result = mapper.toUserTypeResponse(userType);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("ADMIN");
    }

    @Test
    void shouldMapUserTypeListToResponseList() {
        // Given
        UserType userType1 = new UserType();
        userType1.setUuid(UUID.randomUUID());
        userType1.setName("ADMIN");

        UserType userType2 = new UserType();
        userType2.setUuid(UUID.randomUUID());
        userType2.setName("CLIENT");

        List<UserType> userTypes = Arrays.asList(userType1, userType2);

        // When
        List<UserTypeResponse> result = mapper.toUserTypeResponseList(userTypes);

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.getFirst().getName()).isEqualTo("ADMIN");
        assertThat(result.get(1).getName()).isEqualTo("CLIENT");
    }
}
