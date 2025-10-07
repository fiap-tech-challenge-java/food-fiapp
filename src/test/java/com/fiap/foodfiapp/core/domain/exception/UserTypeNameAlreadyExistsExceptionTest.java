package com.fiap.foodfiapp.core.domain.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserTypeNameAlreadyExistsExceptionTest {

    @Test
    void shouldCreateExceptionWithName() {
        String name = "ADMIN";

        UserTypeNameAlreadyExistsException exception = new UserTypeNameAlreadyExistsException(name);

        assertEquals("User type with name 'ADMIN' already exists.", exception.getMessage());
    }

    @Test
    void shouldExtendBusinessException() {
        UserTypeNameAlreadyExistsException exception = new UserTypeNameAlreadyExistsException("Test");

        assertTrue(exception instanceof BusinessException);
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    void shouldHandleNullName() {
        UserTypeNameAlreadyExistsException exception = new UserTypeNameAlreadyExistsException(null);

        assertEquals("User type with name 'null' already exists.", exception.getMessage());
    }

    @Test
    void shouldHandleEmptyName() {
        UserTypeNameAlreadyExistsException exception = new UserTypeNameAlreadyExistsException("");

        assertEquals("User type with name '' already exists.", exception.getMessage());
    }
}
