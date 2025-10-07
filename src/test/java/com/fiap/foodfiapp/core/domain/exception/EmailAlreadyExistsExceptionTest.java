package com.fiap.foodfiapp.core.domain.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EmailAlreadyExistsExceptionTest {

    @Test
    void shouldCreateExceptionWithEmail() {
        String email = "test@example.com";

        EmailAlreadyExistsException exception = new EmailAlreadyExistsException(email);

        assertEquals("Email 'test@example.com' is already registered", exception.getMessage());
    }

    @Test
    void shouldCreateExceptionWithDefaultMessage() {
        EmailAlreadyExistsException exception = new EmailAlreadyExistsException();

        assertEquals("Email is already registered", exception.getMessage());
    }

    @Test
    void shouldCreateExceptionWithMessageAndCause() {
        String message = "Database constraint violation";
        Throwable cause = new RuntimeException("Unique constraint");

        EmailAlreadyExistsException exception = new EmailAlreadyExistsException(message, cause);

        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void shouldExtendBusinessException() {
        EmailAlreadyExistsException exception = new EmailAlreadyExistsException("test@example.com");

        assertTrue(exception instanceof BusinessException);
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    void shouldHandleNullEmail() {
        EmailAlreadyExistsException exception = new EmailAlreadyExistsException(null);

        assertEquals("Email 'null' is already registered", exception.getMessage());
    }
}
