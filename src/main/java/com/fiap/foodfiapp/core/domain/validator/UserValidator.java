package com.fiap.foodfiapp.core.domain.validator;

import com.fiap.foodfiapp.core.domain.entity.User;
import com.fiap.foodfiapp.core.domain.exception.InvalidCpfException;
import com.fiap.foodfiapp.core.domain.exception.InvalidDataException;
import com.fiap.foodfiapp.core.domain.exception.InvalidEmailException;
import com.fiap.foodfiapp.core.domain.exception.InvalidNameException;

public class UserValidator {

    public static void validate(User user) {
        validateName(user.getName());
        validateEmail(user.getEmail());
        validateCpf(user.getCpf());
        validateLogin(user.getLogin());
    }

    public static void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidNameException("User name is required");
        }

        if (name.trim().length() < 3) {
            throw new InvalidNameException("User name must be at least 3 characters long");
        }

        if (name.length() > 100) {
            throw new InvalidNameException("User name cannot exceed 100 characters");
        }

        if (!name.matches(".*[a-zA-Z]+.*")) {
            throw new InvalidNameException("User name must contain at least one letter");
        }
    }

    public static void validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new InvalidEmailException("Email is required");
        }

        // Basic email validation regex
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        if (!email.matches(emailRegex)) {
            throw new InvalidEmailException("Invalid email format");
        }

        if (email.length() > 100) {
            throw new InvalidEmailException("Email cannot exceed 100 characters");
        }
    }

    public static void validateCpf(String cpf) {
        if (cpf == null || cpf.trim().isEmpty()) {
            throw new InvalidCpfException("CPF is required");
        }

        // Remove common formatting characters
        String cleanCpf = cpf.replaceAll("[.\\-/]", "");

        // Must have exactly 11 digits
        if (!cleanCpf.matches("\\d{11}")) {
            throw new InvalidCpfException("CPF must contain exactly 11 digits");
        }

        // Check if all digits are the same (invalid CPF)
        if (cleanCpf.matches("(\\d)\\1{10}")) {
            throw new InvalidCpfException("Invalid CPF format");
        }

        // Validate CPF algorithm
        if (!isValidCpfAlgorithm(cleanCpf)) {
            throw new InvalidCpfException("Invalid CPF");
        }
    }

    public static void validateLogin(String login) {
        if (login == null || login.trim().isEmpty()) {
            throw new InvalidDataException("Login is required");
        }

        if (login.trim().length() < 3) {
            throw new InvalidDataException("Login must be at least 3 characters long");
        }

        if (login.length() > 50) {
            throw new InvalidDataException("Login cannot exceed 50 characters");
        }

        // Login should contain only alphanumeric characters, dots, and underscores
        if (!login.matches("^[a-zA-Z0-9._]+$")) {
            throw new InvalidDataException("Login can only contain letters, numbers, dots, and underscores");
        }
    }

    private static boolean isValidCpfAlgorithm(String cpf) {
        try {
            // Calculate first verification digit
            int sum = 0;
            for (int i = 0; i < 9; i++) {
                sum += Character.getNumericValue(cpf.charAt(i)) * (10 - i);
            }
            int firstDigit = 11 - (sum % 11);
            if (firstDigit >= 10) firstDigit = 0;

            // Calculate second verification digit
            sum = 0;
            for (int i = 0; i < 10; i++) {
                sum += Character.getNumericValue(cpf.charAt(i)) * (11 - i);
            }
            int secondDigit = 11 - (sum % 11);
            if (secondDigit >= 10) secondDigit = 0;

            // Verify digits
            return Character.getNumericValue(cpf.charAt(9)) == firstDigit &&
                   Character.getNumericValue(cpf.charAt(10)) == secondDigit;
        } catch (Exception e) {
            return false;
        }
    }
}

