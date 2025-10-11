package com.fiap.foodfiapp.core.domain.exception;

public class InvalidOpeningHoursException extends BusinessException {
    public InvalidOpeningHoursException(String message) {
        super(message);
    }

    public InvalidOpeningHoursException() {
        super("Opening hours must be provided and cannot exceed 200 characters");
    }
}

