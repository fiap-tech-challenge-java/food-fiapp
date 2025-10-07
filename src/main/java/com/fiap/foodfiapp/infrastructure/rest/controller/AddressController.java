package com.fiap.foodfiapp.infrastructure.rest.controller;

import com.fiap.foodfiapp.api.AddressesApi;
import com.fiap.foodfiapp.core.application.usecases.address.CreateAddressUseCase;
import com.fiap.foodfiapp.core.application.usecases.address.DeleteAddressUseCase;
import com.fiap.foodfiapp.core.application.usecases.address.FindAddressByOwnerUseCase;
import com.fiap.foodfiapp.core.application.usecases.address.UpdateAddressUseCase;
import com.fiap.foodfiapp.infrastructure.persistence.enums.AddressOwnerTypeEnum;
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
    private final FindAddressByOwnerUseCase findAddressByOwnerUseCase;

    private final AddressMapper addressMapper = AddressMapper.INSTANCE;

    @Override
    public ResponseEntity<AddressResponse> createAddressForUser(UUID userId, CreateAddressRequest createAddressRequest) {
        var address = addressMapper.toAddress(createAddressRequest);
        var createdAddress = createAddressUseCase.execute(address, userId, AddressOwnerTypeEnum.USER.getDescription());
        return ResponseEntity.status(HttpStatus.CREATED).body(addressMapper.toAddressResponse(createdAddress));
    }

    @Override
    public ResponseEntity<List<AddressResponse>> listAddressesByUserId(UUID userId) {
        var address = findAddressByOwnerUseCase.execute(userId, AddressOwnerTypeEnum.USER.getDescription());
        return ResponseEntity.ok(addressMapper.toAddressResponseList(address));
    }

    @Override
    public ResponseEntity<AddressResponse> updateAddressForUser(UUID userId, UUID addressId, UpdateAddressRequest updateAddressRequest) {
        var addressUpdates = addressMapper.toAddress(updateAddressRequest);
        var updatedAddress = updateAddressUseCase.execute(addressId, addressUpdates, userId, AddressOwnerTypeEnum.USER.getDescription());
        return ResponseEntity.ok(addressMapper.toAddressResponse(updatedAddress));
    }

    @Override
    public ResponseEntity<Void> deleteAddressForUser(UUID userId, UUID addressId) {
        // RN16 é aplicada no use case: impede remover o último endereço ativo
        deleteAddressUseCase.execute(userId, addressId, AddressOwnerTypeEnum.USER.getDescription());
        return ResponseEntity.noContent().build();
    }
}