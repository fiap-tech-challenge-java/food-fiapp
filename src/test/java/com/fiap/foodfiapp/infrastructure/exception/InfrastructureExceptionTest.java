package com.fiap.foodfiapp.infrastructure.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InfrastructureExceptionTest {
    @Test
    void shouldCreateExceptionWithMessage() {
        InfrastructureException ex = new InfrastructureException("infra error");
        assertEquals("infra error", ex.getMessage());
    }

    @Test
    void shouldCreateExceptionWithMessageAndCause() {
        Throwable cause = new RuntimeException();
        InfrastructureException ex = new InfrastructureException("infra error", cause);
        assertEquals("infra error", ex.getMessage());
        assertEquals(cause, ex.getCause());
    }
}

