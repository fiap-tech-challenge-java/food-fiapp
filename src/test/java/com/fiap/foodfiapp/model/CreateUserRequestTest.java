package com.fiap.foodfiapp.model;

import org.junit.jupiter.api.Test;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class CreateUserRequestTest {

    @Test
    void shouldCreateCreateUserRequestWithAllFields() {
        UUID userTypeUuid = UUID.randomUUID();

        CreateUserRequest request = new CreateUserRequest()
                .name("John Doe")
                .email("john@example.com")
                .login("johndoe")
                .password("password123")
                .cpf("12345678901")
                .userTypeUuid(userTypeUuid)
                .active(true);

        assertThat(request.getName()).isEqualTo("John Doe");
        assertThat(request.getEmail()).isEqualTo("john@example.com");
        assertThat(request.getLogin()).isEqualTo("johndoe");
        assertThat(request.getPassword()).isEqualTo("password123");
        assertThat(request.getCpf()).isEqualTo("12345678901");
        assertThat(request.getUserTypeUuid()).isEqualTo(userTypeUuid);
        assertThat(request.getActive()).isTrue();
    }

    @Test
    void shouldHandleNullValues() {
        CreateUserRequest request = new CreateUserRequest();

        assertThat(request.getName()).isNull();
        assertThat(request.getEmail()).isNull();
        assertThat(request.getLogin()).isNull();
        assertThat(request.getPassword()).isNull();
        assertThat(request.getCpf()).isNull();
        assertThat(request.getUserTypeUuid()).isNull();
        assertThat(request.getActive()).isNull();
    }

    @Test
    void shouldSupportFluentInterface() {
        CreateUserRequest request = new CreateUserRequest()
                .name("Jane Doe")
                .email("jane@example.com")
                .active(false);

        assertThat(request.getName()).isEqualTo("Jane Doe");
        assertThat(request.getEmail()).isEqualTo("jane@example.com");
        assertThat(request.getActive()).isFalse();
    }

    @Test
    void shouldImplementEqualsAndHashCode() {
        CreateUserRequest request1 = new CreateUserRequest().name("Test").email("test@example.com");
        CreateUserRequest request2 = new CreateUserRequest().name("Test").email("test@example.com");

        assertThat(request1).isEqualTo(request2);
        assertThat(request1.hashCode()).isEqualTo(request2.hashCode());
    }

    @Test
    void shouldImplementToString() {
        CreateUserRequest request = new CreateUserRequest().name("Test User");

        assertThat(request.toString()).contains("Test User");
    }
}
