package com.fiap.foodfiapp.core.domain.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InvalidCpfExceptionTest {

    @Test
    void shouldCreateExceptionWithMessage() {
        String message = "CPF format is invalid";

        InvalidCpfException exception = new InvalidCpfException(message);

        assertEquals(message, exception.getMessage());
    }

    @Test
    void shouldCreateExceptionWithDefaultMessage() {
        InvalidCpfException exception = new InvalidCpfException();

        assertEquals("Invalid CPF format", exception.getMessage());
    }

    @Test
    void shouldCreateExceptionWithMessageAndCause() {
        String message = "CPF validation failed";
        Throwable cause = new IllegalArgumentException("Invalid format");

        InvalidCpfException exception = new InvalidCpfException(message, cause);

        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void shouldExtendBusinessException() {
        InvalidCpfException exception = new InvalidCpfException("Test");

        assertTrue(exception instanceof BusinessException);
        assertTrue(exception instanceof RuntimeException);
    }
}
