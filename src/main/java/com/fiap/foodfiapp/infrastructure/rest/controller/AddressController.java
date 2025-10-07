package com.fiap.foodfiapp.infrastructure.rest.controller;

import com.fiap.foodfiapp.api.AddressesApi;
import com.fiap.foodfiapp.core.application.usecases.address.CreateAddressUseCase;
import com.fiap.foodfiapp.core.application.usecases.address.DeleteAddressUseCase;
import com.fiap.foodfiapp.core.application.usecases.address.FindAddressesByOwnerUseCase;
import com.fiap.foodfiapp.core.application.usecases.address.UpdateAddressUseCase;
import com.fiap.foodfiapp.infrastructure.persistence.enums.AddressesOwnerTypeEnum;
import com.fiap.foodfiapp.infrastructure.rest.mapper.AddressMapper;
import com.fiap.foodfiapp.model.AddressResponse;
import com.fiap.foodfiapp.model.CreateAddressRequest;
import com.fiap.foodfiapp.model.UpdateAddressRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class AddressController implements AddressesApi {

    private final CreateAddressUseCase createAddressUseCase;
    private final UpdateAddressUseCase updateAddressUseCase;
    private final DeleteAddressUseCase deleteAddressUseCase;
    private final FindAddressesByOwnerUseCase findAddressesByOwnerUseCase;

    private final AddressMapper addressMapper = AddressMapper.INSTANCE;

    @Override
    public ResponseEntity<AddressResponse> createAddressForUser(UUID userId, CreateAddressRequest createAddressRequest) {
        var address = addressMapper.toAddress(createAddressRequest);
        var createdAddress = createAddressUseCase.execute(address, userId, AddressesOwnerTypeEnum.USER.getDescription());
        return ResponseEntity.status(HttpStatus.CREATED).body(addressMapper.toAddressResponse(createdAddress));
    }

    @Override
    public ResponseEntity<List<AddressResponse>> listAddressesByUserId(UUID userId) {
        var addresses = findAddressesByOwnerUseCase.execute(userId, AddressesOwnerTypeEnum.USER.getDescription());
        return ResponseEntity.ok(addressMapper.toAddressResponseList(addresses));
    }

    @Override
    public ResponseEntity<AddressResponse> updateAddressForUser(UUID userId, UUID addressId, UpdateAddressRequest updateAddressRequest) {
        var addressUpdates = addressMapper.toAddress(updateAddressRequest);
        var updatedAddress = updateAddressUseCase.execute(addressId, addressUpdates, userId, AddressesOwnerTypeEnum.USER.getDescription());
        return ResponseEntity.ok(addressMapper.toAddressResponse(updatedAddress));
    }

    @Override
    public ResponseEntity<Void> deleteAddressForUser(UUID userId, UUID addressId) {
        // A lógica do use case já valida se o endereço existe.
        // Poderíamos adicionar uma validação extra para garantir que o endereço pertence ao userId, se necessário.
        deleteAddressUseCase.execute(addressId);
        return ResponseEntity.noContent().build();
    }
}