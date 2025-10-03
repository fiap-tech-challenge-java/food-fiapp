package com.fiap.foodfiapp.infrastructure.rest.dto.addressesrestaurant;

import java.util.UUID;

public record CreateAddressesRestaurantRequestDTO(
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
