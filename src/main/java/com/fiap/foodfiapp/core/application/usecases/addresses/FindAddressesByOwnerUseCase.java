package com.fiap.foodfiapp.core.application.usecases.addresses;

import com.fiap.foodfiapp.core.domain.entity.Addresses;

import java.util.List;
import java.util.UUID;

public interface FindAddressesByOwnerUseCase {
    List<Addresses> execute(UUID ownerId, String ownerType);
}