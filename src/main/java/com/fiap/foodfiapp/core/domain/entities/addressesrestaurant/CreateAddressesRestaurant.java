package com.fiap.foodfiapp.core.domain.entities.addressesrestaurant;

import java.util.UUID;

public record CreateAddressesRestaurant(
        String publicPlace,
        String number,
        String complement,
        String neighborhood,
        String city,
        String state,
        String postalCode,
        UUID restaurantId
) {
}
