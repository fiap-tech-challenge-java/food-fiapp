package com.fiap.foodfiapp.core.application.usecases.addresses;

import com.fiap.foodfiapp.core.domain.entity.Addresses;
import java.util.UUID;

public interface UpdateAddressesUseCase {
    // A assinatura foi ajustada para receber o contexto do proprietário (owner)
    Addresses execute(UUID addressId, Addresses addressesUpdates, UUID ownerId, String ownerType);
}