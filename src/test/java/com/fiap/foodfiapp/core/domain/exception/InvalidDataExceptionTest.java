package com.fiap.foodfiapp.core.domain.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InvalidDataExceptionTest {

    @Test
    void shouldCreateExceptionWithMessage() {
        String message = "Invalid data provided";

        InvalidDataException exception = new InvalidDataException(message);

        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void shouldCreateExceptionWithFieldAndValue() {
        String field = "email";
        String value = "invalid-email";

        InvalidDataException exception = new InvalidDataException(field, value);

        assertEquals("Invalid data for field 'email': invalid-email", exception.getMessage());
    }

    @Test
    void shouldCreateExceptionWithDefaultMessage() {
        InvalidDataException exception = new InvalidDataException();

        assertEquals("Invalid data provided", exception.getMessage());
    }

    @Test
    void shouldCreateExceptionWithMessageAndCause() {
        String message = "Validation failed";
        Throwable cause = new IllegalArgumentException("Root cause");

        InvalidDataException exception = new InvalidDataException(message, cause);

        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void shouldExtendBusinessException() {
        InvalidDataException exception = new InvalidDataException("Test");

        assertTrue(exception instanceof BusinessException);
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    void shouldHandleNullFieldAndValue() {
        String nullField = null;
        String nullValue = null;
        InvalidDataException exception = new InvalidDataException(nullField, nullValue);

        assertEquals("Invalid data for field 'null': null", exception.getMessage());
    }

    @Test
    void shouldHandleEmptyFieldAndValue() {
        InvalidDataException exception = new InvalidDataException("", "");

        assertEquals("Invalid data for field '': ", exception.getMessage());
    }
}
