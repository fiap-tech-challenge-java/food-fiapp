package com.fiap.foodfiapp.model;

import org.junit.jupiter.api.Test;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class UserTypeResponseTest {

    @Test
    void shouldCreateUserTypeResponseWithAllFields() {
        UUID uuid = UUID.randomUUID();

        UserTypeResponse response = new UserTypeResponse()
                .uuid(uuid)
                .name("CLIENT");

        assertThat(response.getUuid()).isEqualTo(uuid);
        assertThat(response.getName()).isEqualTo("CLIENT");
    }

    @Test
    void shouldHandleNullValues() {
        UserTypeResponse response = new UserTypeResponse();

        assertThat(response.getUuid()).isNull();
        assertThat(response.getName()).isNull();
    }

    @Test
    void shouldSupportFluentInterface() {
        UserTypeResponse response = new UserTypeResponse()
                .name("ADMIN")
                .uuid(UUID.randomUUID());

        assertThat(response.getName()).isEqualTo("ADMIN");
        assertThat(response.getUuid()).isNotNull();
    }

    @Test
    void shouldImplementEqualsAndHashCode() {
        UUID uuid = UUID.randomUUID();
        UserTypeResponse response1 = new UserTypeResponse().uuid(uuid).name("CLIENT");
        UserTypeResponse response2 = new UserTypeResponse().uuid(uuid).name("CLIENT");

        assertThat(response1).isEqualTo(response2);
        assertThat(response1.hashCode()).isEqualTo(response2.hashCode());
    }

    @Test
    void shouldImplementToString() {
        UserTypeResponse response = new UserTypeResponse().name("ADMIN");

        assertThat(response.toString()).contains("ADMIN");
    }
}
