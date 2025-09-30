package com.fiap.foodfiapp.model;

import org.junit.jupiter.api.Test;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class UserResponseTest {

    @Test
    void shouldCreateUserResponseWithAllFields() {
        UUID id = UUID.randomUUID();

        UserResponse response = new UserResponse()
                .id(id)
                .name("John Doe")
                .email("john@example.com")
                .login("johndoe")
                .cpf("12345678901")
                .userType("CLIENT")
                .active(true);

        assertThat(response.getId()).isEqualTo(id);
        assertThat(response.getName()).isEqualTo("John Doe");
        assertThat(response.getEmail()).isEqualTo("john@example.com");
        assertThat(response.getLogin()).isEqualTo("johndoe");
        assertThat(response.getCpf()).isEqualTo("12345678901");
        assertThat(response.getUserType()).isEqualTo("CLIENT");
        assertThat(response.getActive()).isTrue();
    }

    @Test
    void shouldHandleNullValues() {
        UserResponse response = new UserResponse();

        assertThat(response.getId()).isNull();
        assertThat(response.getName()).isNull();
        assertThat(response.getEmail()).isNull();
        assertThat(response.getLogin()).isNull();
        assertThat(response.getCpf()).isNull();
        assertThat(response.getUserType()).isNull();
        assertThat(response.getActive()).isNull();
    }

    @Test
    void shouldSupportFluentInterface() {
        UserResponse response = new UserResponse()
                .name("Jane Doe")
                .email("jane@example.com")
                .active(false);

        assertThat(response.getName()).isEqualTo("Jane Doe");
        assertThat(response.getEmail()).isEqualTo("jane@example.com");
        assertThat(response.getActive()).isFalse();
    }

    @Test
    void shouldImplementEqualsAndHashCode() {
        UUID id = UUID.randomUUID();
        UserResponse response1 = new UserResponse().id(id).name("Test");
        UserResponse response2 = new UserResponse().id(id).name("Test");

        assertThat(response1).isEqualTo(response2);
        assertThat(response1.hashCode()).isEqualTo(response2.hashCode());
    }

    @Test
    void shouldImplementToString() {
        UserResponse response = new UserResponse().name("Test User");

        assertThat(response.toString()).contains("Test User");
    }
}
