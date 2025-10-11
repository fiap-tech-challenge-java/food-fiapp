package com.fiap.foodfiapp.infrastructure.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BusinessExceptionTest {

    @Test
    void shouldCreateExceptionWithMessage() {
        // Arrange
        String message = "Business rule violation";

        // Act
        BusinessException exception = new BusinessException(message);

        // Assert
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo(message);
    }

    @Test
    void shouldCreateExceptionWithMessageAndCause() {
        // Arrange
        String message = "Business rule violation";
        Throwable cause = new RuntimeException("Original cause");

        // Act
        BusinessException exception = new BusinessException(message, cause);

        // Assert
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo(message);
        assertThat(exception.getCause()).isEqualTo(cause);
    }

    @Test
    void shouldBeRuntimeException() {
        // Arrange
        BusinessException exception = new BusinessException("Test");

        // Assert
        assertThat(exception).isInstanceOf(RuntimeException.class);
    }
}
