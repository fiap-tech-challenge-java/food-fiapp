package com.fiap.foodfiapp.model;

import org.junit.jupiter.api.Test;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class UserTypeRequestTest {

    @Test
    void shouldCreateCreateUserTypeRequestWithAllFields() {
        CreateUserTypeRequest request = new CreateUserTypeRequest()
                .name("ADMIN");

        assertThat(request.getName()).isEqualTo("ADMIN");
    }

    @Test
    void shouldHandleNullValues() {
        CreateUserTypeRequest request = new CreateUserTypeRequest();

        assertThat(request.getName()).isNull();
    }

    @Test
    void shouldSupportFluentInterface() {
        UpdateUserTypeRequest request = new UpdateUserTypeRequest()
                .name("CLIENT");

        assertThat(request.getName()).isEqualTo("CLIENT");
    }

    @Test
    void shouldImplementEqualsAndHashCode() {
        CreateUserTypeRequest request1 = new CreateUserTypeRequest().name("OWNER");
        CreateUserTypeRequest request2 = new CreateUserTypeRequest().name("OWNER");

        assertThat(request1).isEqualTo(request2);
        assertThat(request1.hashCode()).isEqualTo(request2.hashCode());
    }

    @Test
    void shouldImplementToString() {
        UpdateUserTypeRequest request = new UpdateUserTypeRequest().name("MANAGER");

        assertThat(request.toString()).contains("MANAGER");
    }
}
