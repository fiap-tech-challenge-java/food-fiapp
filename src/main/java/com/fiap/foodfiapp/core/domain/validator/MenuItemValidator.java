package com.fiap.foodfiapp.core.domain.validator;

import com.fiap.foodfiapp.core.domain.entity.MenuItem;
import com.fiap.foodfiapp.core.domain.exception.InvalidDataException;
import com.fiap.foodfiapp.core.domain.exception.InvalidMenuItemNameException;
import com.fiap.foodfiapp.core.domain.exception.InvalidPriceException;

public class MenuItemValidator {

    public static void validate(MenuItem menuItem) {
        validateName(menuItem.getName());
        validatePrice(menuItem.getPrice());
        validateDescription(menuItem.getDescription());
    }

    public static void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidMenuItemNameException("Menu item name is required");
        }

        if (name.trim().length() < 3) {
            throw new InvalidMenuItemNameException("Menu item name must be at least 3 characters long");
        }

        if (name.length() > 100) {
            throw new InvalidMenuItemNameException("Menu item name cannot exceed 100 characters");
        }

        if (!name.matches(".*[a-zA-Z]+.*")) {
            throw new InvalidMenuItemNameException("Menu item name must contain at least one letter");
        }
    }

    public static void validatePrice(Double price) {
        if (price == null) {
            throw new InvalidPriceException("Price is required");
        }

        if (price <= 0) {
            throw new InvalidPriceException("Price must be greater than zero");
        }

        if (price > 999999.99) {
            throw new InvalidPriceException("Price cannot exceed R$ 999,999.99");
        }

        String priceStr = String.valueOf(price);
        if (priceStr.contains(".")) {
            String[] parts = priceStr.split("\\.");
            if (parts.length > 1 && parts[1].length() > 2) {
                throw new InvalidPriceException("Price cannot have more than 2 decimal places");
            }
        }

        if (Double.isNaN(price) || Double.isInfinite(price)) {
            throw new InvalidPriceException("Invalid price value");
        }
    }

    public static void validateDescription(String description) {
        if (description != null && description.length() > 500) {
            throw new InvalidDataException("Menu item description cannot exceed 500 characters");
        }
    }
}

