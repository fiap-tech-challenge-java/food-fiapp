package com.fiap.foodfiapp.core.domain.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LoginAlreadyExistsExceptionTest {

    @Test
    void shouldCreateExceptionWithLogin() {
        String login = "user123";

        LoginAlreadyExistsException exception = new LoginAlreadyExistsException(login);

        assertEquals("Login 'user123' is already registered", exception.getMessage());
    }

    @Test
    void shouldCreateExceptionWithDefaultMessage() {
        LoginAlreadyExistsException exception = new LoginAlreadyExistsException();

        assertEquals("Login is already registered", exception.getMessage());
    }

    @Test
    void shouldCreateExceptionWithMessageAndCause() {
        String message = "Database constraint violation";
        Throwable cause = new RuntimeException("Unique constraint");

        LoginAlreadyExistsException exception = new LoginAlreadyExistsException(message, cause);

        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void shouldExtendBusinessException() {
        LoginAlreadyExistsException exception = new LoginAlreadyExistsException("test");

        assertTrue(exception instanceof BusinessException);
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    void shouldHandleNullLogin() {
        LoginAlreadyExistsException exception = new LoginAlreadyExistsException(null);

        assertEquals("Login 'null' is already registered", exception.getMessage());
    }
}
