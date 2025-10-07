package com.fiap.foodfiapp.core.domain.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CpfAlreadyExistsExceptionTest {

    @Test
    void shouldCreateExceptionWithCpf() {
        String cpf = "12345678901";

        CpfAlreadyExistsException exception = new CpfAlreadyExistsException(cpf);

        assertEquals("CPF '12345678901' is already registered", exception.getMessage());
    }

    @Test
    void shouldExtendBusinessException() {
        CpfAlreadyExistsException exception = new CpfAlreadyExistsException("12345678901");

        assertTrue(exception instanceof BusinessException);
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    void shouldHandleNullCpf() {
        CpfAlreadyExistsException exception = new CpfAlreadyExistsException(null);

        assertEquals("CPF 'null' is already registered", exception.getMessage());
    }

    @Test
    void shouldHandleEmptyCpf() {
        CpfAlreadyExistsException exception = new CpfAlreadyExistsException("");

        assertEquals("CPF '' is already registered", exception.getMessage());
    }
}
