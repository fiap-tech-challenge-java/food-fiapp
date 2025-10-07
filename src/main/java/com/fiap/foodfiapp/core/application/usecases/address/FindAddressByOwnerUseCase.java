package com.fiap.foodfiapp.core.application.usecases.address;

import com.fiap.foodfiapp.core.domain.entity.Address;
import java.util.List;
import java.util.UUID;

public interface FindAddressByOwnerUseCase {
    List<Address> execute(UUID ownerId, String ownerType);
}