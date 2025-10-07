package com.fiap.foodfiapp.core.domain.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InvalidPasswordExceptionTest {

    @Test
    void shouldCreateExceptionWithMessage() {
        String message = "Password is too weak";

        InvalidPasswordException exception = new InvalidPasswordException(message);

        assertEquals(message, exception.getMessage());
    }

    @Test
    void shouldCreateExceptionWithDefaultMessage() {
        InvalidPasswordException exception = new InvalidPasswordException();

        assertEquals("Password does not meet security requirements", exception.getMessage());
    }

    @Test
    void shouldCreateExceptionWithMessageAndCause() {
        String message = "Password validation failed";
        Throwable cause = new IllegalArgumentException("Invalid format");

        InvalidPasswordException exception = new InvalidPasswordException(message, cause);

        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void shouldExtendBusinessException() {
        InvalidPasswordException exception = new InvalidPasswordException("Test");

        assertTrue(exception instanceof BusinessException);
        assertTrue(exception instanceof RuntimeException);
    }
}
