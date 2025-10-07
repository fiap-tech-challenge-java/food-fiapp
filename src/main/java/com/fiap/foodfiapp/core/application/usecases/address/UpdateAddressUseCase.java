package com.fiap.foodfiapp.core.application.usecases.address;

import com.fiap.foodfiapp.core.domain.entity.Address;
import java.util.UUID;

public interface UpdateAddressUseCase {
    Address execute(UUID addressId, Address address);
}