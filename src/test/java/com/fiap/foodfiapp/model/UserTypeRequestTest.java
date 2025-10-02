package com.fiap.foodfiapp.model;

import org.junit.jupiter.api.Test;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class UserTypeRequestTest {

    @Test
    void shouldCreateUserTypeRequestWithAllFields() {
        UserTypeRequest request = new UserTypeRequest()
                .name("ADMIN");

        assertThat(request.getName()).isEqualTo("ADMIN");
    }

    @Test
    void shouldHandleNullValues() {
        UserTypeRequest request = new UserTypeRequest();

        assertThat(request.getName()).isNull();
    }

    @Test
    void shouldSupportFluentInterface() {
        UserTypeRequest request = new UserTypeRequest()
                .name("CUSTOMER");

        assertThat(request.getName()).isEqualTo("CUSTOMER");
    }

    @Test
    void shouldImplementEqualsAndHashCode() {
        UserTypeRequest request1 = new UserTypeRequest().name("OWNER");
        UserTypeRequest request2 = new UserTypeRequest().name("OWNER");

        assertThat(request1).isEqualTo(request2);
        assertThat(request1.hashCode()).isEqualTo(request2.hashCode());
    }

    @Test
    void shouldImplementToString() {
        UserTypeRequest request = new UserTypeRequest().name("OWNER");

        assertThat(request.toString()).contains("OWNER");
    }
}
