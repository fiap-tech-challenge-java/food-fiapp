package com.fiap.foodfiapp.core.domain.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UnauthorizedAccessExceptionTest {

    @Test
    void shouldCreateExceptionWithMessage() {
        String message = "User not authorized";

        UnauthorizedAccessException exception = new UnauthorizedAccessException(message);

        assertEquals(message, exception.getMessage());
    }

    @Test
    void shouldCreateExceptionWithDefaultMessage() {
        UnauthorizedAccessException exception = new UnauthorizedAccessException();

        assertEquals("Access denied. You are not authorized to perform this action", exception.getMessage());
    }

    @Test
    void shouldExtendBusinessException() {
        UnauthorizedAccessException exception = new UnauthorizedAccessException("Test");

        assertTrue(exception instanceof BusinessException);
        assertTrue(exception instanceof RuntimeException);
    }
}
