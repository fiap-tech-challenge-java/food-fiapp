package com.fiap.foodfiapp.core.domain.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BusinessExceptionTest {

    @Test
    void shouldCreateExceptionWithMessage() {
        String message = "Business rule violation";

        BusinessException exception = new BusinessException(message);

        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void shouldCreateExceptionWithMessageAndCause() {
        String message = "Business rule violation";
        Throwable cause = new RuntimeException("Root cause");

        BusinessException exception = new BusinessException(message, cause);

        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void shouldCreateExceptionWithNullMessage() {
        BusinessException exception = new BusinessException(null);

        assertNull(exception.getMessage());
    }

    @Test
    void shouldCreateExceptionWithEmptyMessage() {
        BusinessException exception = new BusinessException("");

        assertEquals("", exception.getMessage());
    }

    @Test
    void shouldBeRuntimeException() {
        BusinessException exception = new BusinessException("Test");

        assertTrue(exception instanceof RuntimeException);
    }
}
