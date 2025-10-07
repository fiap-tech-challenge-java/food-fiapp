package com.fiap.foodfiapp.core.domain.enums;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AddressOwnerTypeEnumTest {

    @Test
    void shouldReturnCorrectDescriptionForUser() {
        assertEquals("USER", AddressOwnerTypeEnum.USER.getDescription());
    }

    @Test
    void shouldReturnCorrectDescriptionForRestaurant() {
        assertEquals("RESTAURANT", AddressOwnerTypeEnum.RESTAURANT.getDescription());
    }

    @Test
    void shouldHaveExactlyTwoValues() {
        AddressOwnerTypeEnum[] values = AddressOwnerTypeEnum.values();
        assertEquals(2, values.length);
    }

    @Test
    void shouldContainUserAndRestaurantValues() {
        AddressOwnerTypeEnum[] values = AddressOwnerTypeEnum.values();
        assertTrue(java.util.Arrays.asList(values).contains(AddressOwnerTypeEnum.USER));
        assertTrue(java.util.Arrays.asList(values).contains(AddressOwnerTypeEnum.RESTAURANT));
    }

    @Test
    void shouldValueOfReturnCorrectEnum() {
        assertEquals(AddressOwnerTypeEnum.USER, AddressOwnerTypeEnum.valueOf("USER"));
        assertEquals(AddressOwnerTypeEnum.RESTAURANT, AddressOwnerTypeEnum.valueOf("RESTAURANT"));
    }

    @Test
    void shouldThrowExceptionForInvalidValue() {
        assertThrows(IllegalArgumentException.class, () ->
            AddressOwnerTypeEnum.valueOf("INVALID"));
    }
}
