package com.fiap.foodfiapp.core.application.usecases.address;

import com.fiap.foodfiapp.core.domain.entity.Address;
import java.util.UUID;

public interface UpdateAddressUseCase {
    // A assinatura foi ajustada para receber o contexto do proprietário (owner)
    Address execute(UUID addressId, Address addressUpdates, UUID ownerId, String ownerType);
}