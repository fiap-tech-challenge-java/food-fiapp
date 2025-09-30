package com.fiap.foodfiapp.model;

import org.junit.jupiter.api.Test;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ErrorResponseTest {

    @Test
    void shouldCreateErrorResponseWithAllFields() {
        OffsetDateTime timestamp = OffsetDateTime.now();
        Map<String, String> fieldErrors = new HashMap<>();
        fieldErrors.put("name", "Name is required");

        ErrorResponse response = new ErrorResponse()
                .message("An error occurred")
                .code("VALIDATION_ERROR")
                .status(400)
                .path("/api/test")
                .timestamp(timestamp)
                .fieldErrors(fieldErrors);

        assertThat(response.getMessage()).isEqualTo("An error occurred");
        assertThat(response.getCode()).isEqualTo("VALIDATION_ERROR");
        assertThat(response.getStatus()).isEqualTo(400);
        assertThat(response.getPath()).isEqualTo("/api/test");
        assertThat(response.getTimestamp()).isEqualTo(timestamp);
        assertThat(response.getFieldErrors()).containsEntry("name", "Name is required");
    }

    @Test
    void shouldHandleNullValues() {
        ErrorResponse response = new ErrorResponse();

        assertThat(response.getMessage()).isNull();
        assertThat(response.getCode()).isNull();
        assertThat(response.getStatus()).isNull();
        assertThat(response.getPath()).isNull();
        assertThat(response.getTimestamp()).isNull();
        assertThat(response.getFieldErrors()).isNotNull();
        assertThat(response.getFieldErrors()).isEmpty();
    }

    @Test
    void shouldSupportFluentInterface() {
        ErrorResponse response = new ErrorResponse()
                .message("Validation failed")
                .code("BAD_REQUEST")
                .status(400);

        assertThat(response.getMessage()).isEqualTo("Validation failed");
        assertThat(response.getCode()).isEqualTo("BAD_REQUEST");
        assertThat(response.getStatus()).isEqualTo(400);
    }

    @Test
    void shouldImplementEqualsAndHashCode() {
        ErrorResponse response1 = new ErrorResponse().message("Error").code("ERR001");
        ErrorResponse response2 = new ErrorResponse().message("Error").code("ERR001");

        assertThat(response1).isEqualTo(response2);
        assertThat(response1.hashCode()).isEqualTo(response2.hashCode());
    }

    @Test
    void shouldImplementToString() {
        ErrorResponse response = new ErrorResponse().message("Test Error");

        assertThat(response.toString()).contains("Test Error");
    }
}
