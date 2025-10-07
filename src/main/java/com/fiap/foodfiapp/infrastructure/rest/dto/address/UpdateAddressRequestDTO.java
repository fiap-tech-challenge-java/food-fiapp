package com.fiap.foodfiapp.infrastructure.rest.dto.address;

public record UpdateAddressRequestDTO(
        String publicPlace,
        String number,
        String complement,
        String neighborhood,
        String city,
        String state,
        String postalCode
) {}