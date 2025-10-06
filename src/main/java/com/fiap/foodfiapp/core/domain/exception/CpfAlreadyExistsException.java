package com.fiap.foodfiapp.core.domain.exception;

public class CpfAlreadyExistsException extends BusinessException {

    public CpfAlreadyExistsException(String cpf) {
        super("CPF '" + cpf + "' is already registered");
    }
}

