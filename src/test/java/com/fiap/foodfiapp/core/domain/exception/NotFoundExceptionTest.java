package com.fiap.foodfiapp.core.domain.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class NotFoundExceptionTest {

    @Test
    void shouldCreateExceptionWithMessage() {
        String message = "Resource not found";

        NotFoundException exception = new NotFoundException(message);

        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void shouldCreateExceptionWithMessageAndCause() {
        String message = "Entity not found";
        Throwable cause = new RuntimeException("Database error");

        NotFoundException exception = new NotFoundException(message, cause);

        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void shouldBeRuntimeException() {
        NotFoundException exception = new NotFoundException("Test");

        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    void shouldHandleNullMessage() {
        NotFoundException exception = new NotFoundException(null);

        assertNull(exception.getMessage());
    }

    @Test
    void shouldHandleEmptyMessage() {
        NotFoundException exception = new NotFoundException("");

        assertEquals("", exception.getMessage());
    }
}
