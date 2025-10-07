package com.fiap.foodfiapp.core.application.usecases.address;

import com.fiap.foodfiapp.core.domain.entity.Address;
import java.util.UUID;

public interface CreateAddressUseCase {
    Address execute(Address address, UUID ownerId, String ownerType);
}