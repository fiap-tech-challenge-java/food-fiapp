package com.fiap.foodfiapp.core.domain.exception;

public class CoreUserTypeModificationException extends BusinessException {

    public CoreUserTypeModificationException() {
        super("Cannot modify or delete a core user type.");
    }
}

