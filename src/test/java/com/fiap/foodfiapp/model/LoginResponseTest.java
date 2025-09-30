package com.fiap.foodfiapp.model;

import org.junit.jupiter.api.Test;
import java.time.OffsetDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class LoginResponseTest {

    @Test
    void shouldCreateLoginResponseWithAllFields() {
        UUID userId = UUID.randomUUID();
        OffsetDateTime expiresAt = OffsetDateTime.now().plusHours(1);

        LoginResponse response = new LoginResponse()
                .token("jwt-token-123")
                .login("testuser")
                .email("test@example.com")
                .expiresAt(expiresAt)
                .expiresIn(3600)
                .userId(userId);

        assertThat(response.getToken()).isEqualTo("jwt-token-123");
        assertThat(response.getLogin()).isEqualTo("testuser");
        assertThat(response.getEmail()).isEqualTo("test@example.com");
        assertThat(response.getExpiresAt()).isEqualTo(expiresAt);
        assertThat(response.getExpiresIn()).isEqualTo(3600);
        assertThat(response.getUserId()).isEqualTo(userId);
    }

    @Test
    void shouldHandleNullValues() {
        LoginResponse response = new LoginResponse();

        assertThat(response.getToken()).isNull();
        assertThat(response.getLogin()).isNull();
        assertThat(response.getEmail()).isNull();
        assertThat(response.getExpiresAt()).isNull();
        assertThat(response.getExpiresIn()).isNull();
        assertThat(response.getUserId()).isNull();
    }

    @Test
    void shouldSupportFluentInterface() {
        LoginResponse response = new LoginResponse()
                .token("new-token")
                .login("admin")
                .expiresIn(7200);

        assertThat(response.getToken()).isEqualTo("new-token");
        assertThat(response.getLogin()).isEqualTo("admin");
        assertThat(response.getExpiresIn()).isEqualTo(7200);
    }

    @Test
    void shouldImplementEqualsAndHashCode() {
        LoginResponse response1 = new LoginResponse().token("token").login("user").expiresIn(3600);
        LoginResponse response2 = new LoginResponse().token("token").login("user").expiresIn(3600);

        assertThat(response1).isEqualTo(response2);
        assertThat(response1.hashCode()).isEqualTo(response2.hashCode());
    }

    @Test
    void shouldImplementToString() {
        LoginResponse response = new LoginResponse().token("test-token");

        assertThat(response.toString()).contains("test-token");
    }
}
