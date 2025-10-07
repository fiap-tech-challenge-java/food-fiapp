package com.fiap.foodfiapp.core.domain.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InvalidEmailExceptionTest {

    @Test
    void shouldCreateExceptionWithMessage() {
        String message = "Email format is invalid";

        InvalidEmailException exception = new InvalidEmailException(message);

        assertEquals(message, exception.getMessage());
    }

    @Test
    void shouldCreateExceptionWithDefaultMessage() {
        InvalidEmailException exception = new InvalidEmailException();

        assertEquals("Invalid email format", exception.getMessage());
    }

    @Test
    void shouldCreateExceptionWithMessageAndCause() {
        String message = "Email validation failed";
        Throwable cause = new IllegalArgumentException("Invalid format");

        InvalidEmailException exception = new InvalidEmailException(message, cause);

        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void shouldExtendBusinessException() {
        InvalidEmailException exception = new InvalidEmailException("Test");

        assertTrue(exception instanceof BusinessException);
        assertTrue(exception instanceof RuntimeException);
    }
}
