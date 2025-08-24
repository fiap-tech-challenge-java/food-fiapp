package com.fiap.foodfiapp.domain.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BusinessExceptionTest {
    @Test
    void shouldCreateExceptionWithMessage() {
        BusinessException ex = new BusinessException("msg");
        assertEquals("msg", ex.getMessage());
    }

    @Test
    void shouldCreateExceptionWithMessageAndCause() {
        Throwable cause = new RuntimeException();
        BusinessException ex = new BusinessException("msg", cause);
        assertEquals("msg", ex.getMessage());
        assertEquals(cause, ex.getCause());
    }
}

