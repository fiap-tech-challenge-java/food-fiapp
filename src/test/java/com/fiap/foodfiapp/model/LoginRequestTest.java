package com.fiap.foodfiapp.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LoginRequestTest {

    @Test
    void shouldCreateLoginRequestWithAllFields() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setLogin("johndoe");
        loginRequest.setPassword("password123");

        assertThat(loginRequest.getLogin()).isEqualTo("johndoe");
        assertThat(loginRequest.getPassword()).isEqualTo("password123");
    }

    @Test
    void shouldCreateEmptyLoginRequest() {
        LoginRequest loginRequest = new LoginRequest();

        assertThat(loginRequest.getLogin()).isNull();
        assertThat(loginRequest.getPassword()).isNull();
    }

    @Test
    void shouldSetAndGetLogin() {
        LoginRequest loginRequest = new LoginRequest();

        loginRequest.setLogin("testuser");

        assertThat(loginRequest.getLogin()).isEqualTo("testuser");
    }

    @Test
    void shouldSetAndGetPassword() {
        LoginRequest loginRequest = new LoginRequest();

        loginRequest.setPassword("mypassword");

        assertThat(loginRequest.getPassword()).isEqualTo("mypassword");
    }

    @Test
    void shouldHandleNullValues() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setLogin(null);
        loginRequest.setPassword(null);

        assertThat(loginRequest.getLogin()).isNull();
        assertThat(loginRequest.getPassword()).isNull();
    }

    @Test
    void shouldHandleEmptyStrings() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setLogin("");
        loginRequest.setPassword("");

        assertThat(loginRequest.getLogin()).isEmpty();
        assertThat(loginRequest.getPassword()).isEmpty();
    }

    @Test
    void shouldCreateWithEmailAsLogin() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setLogin("user@example.com");
        loginRequest.setPassword("securepass");

        assertThat(loginRequest.getLogin()).isEqualTo("user@example.com");
        assertThat(loginRequest.getPassword()).isEqualTo("securepass");
    }
}
