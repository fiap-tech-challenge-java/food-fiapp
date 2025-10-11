package com.fiap.foodfiapp.infrastructure.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InfrastructureExceptionsTest {

    @Test
    void testInfrastructureException() {
        InfrastructureException exception = new InfrastructureException("Infrastructure error");
        assertEquals("Infrastructure error", exception.getMessage());
        assertNotNull(exception);
    }

    @Test
    void testBusinessException() {
        BusinessException exception = new BusinessException("Business error");
        assertEquals("Business error", exception.getMessage());
        assertNotNull(exception);
    }

    @Test
    void testResourceNotFoundException() {
        ResourceNotFoundException exception = new ResourceNotFoundException("Resource not found");
        assertEquals("Resource not found", exception.getMessage());
        assertNotNull(exception);
    }

    @Test
    void testResourceNotFoundExceptionWithParams() {
        ResourceNotFoundException exception = new ResourceNotFoundException("Resource", "id", "123");
        assertNotNull(exception);
        assertTrue(exception.getMessage().contains("Resource"));
        assertTrue(exception.getMessage().contains("123"));
    }
}
