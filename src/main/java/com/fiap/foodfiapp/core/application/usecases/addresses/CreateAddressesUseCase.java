package com.fiap.foodfiapp.core.application.usecases.addresses;

import com.fiap.foodfiapp.core.domain.entity.Addresses;

import java.util.UUID;

public interface CreateAddressesUseCase {
    Addresses execute(Addresses addresses, UUID ownerId, String ownerType);
}