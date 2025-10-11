package com.fiap.foodfiapp.infrastructure.rest.controller;

import com.fiap.foodfiapp.api.AddressesApi;
import com.fiap.foodfiapp.core.application.usecases.addresses.CreateAddressesUseCase;
import com.fiap.foodfiapp.core.application.usecases.addresses.DeleteAddressesUseCase;
import com.fiap.foodfiapp.core.application.usecases.addresses.FindAddressesByOwnerUseCase;
import com.fiap.foodfiapp.core.application.usecases.addresses.UpdateAddressesUseCase;
import com.fiap.foodfiapp.core.domain.enums.AddressOwnerTypeEnum;
import com.fiap.foodfiapp.infrastructure.rest.mapper.AddressesMapper;
import com.fiap.foodfiapp.infrastructure.security.AuthenticationService;
import com.fiap.foodfiapp.model.AddressesResponse;
import com.fiap.foodfiapp.model.CreateAddressesRequest;
import com.fiap.foodfiapp.model.UpdateAddressesRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class AddressesController implements AddressesApi {

    private final CreateAddressesUseCase createAddressesUseCase;
    private final UpdateAddressesUseCase updateAddressesUseCase;
    private final DeleteAddressesUseCase deleteAddressesUseCase;
    private final FindAddressesByOwnerUseCase findAddressesByOwnerUseCase;
    private final AuthenticationService authenticationService;

    private final AddressesMapper addressesMapper = AddressesMapper.INSTANCE;

    @Override
    public ResponseEntity<AddressesResponse> createAddressesForUser(UUID userId,
            CreateAddressesRequest createAddressesRequest) {

        var address = addressesMapper.toAddress(createAddressesRequest);
        var createdAddress = createAddressesUseCase.execute(address, userId,
                AddressOwnerTypeEnum.USER.getDescription());
        return ResponseEntity.status(HttpStatus.CREATED).body(addressesMapper.toAddressesResponse(createdAddress));
    }

    @Override
    public ResponseEntity<List<AddressesResponse>> listAddressesByUserId(UUID userId) {


        var address = findAddressesByOwnerUseCase.execute(userId, AddressOwnerTypeEnum.USER.getDescription());
        return ResponseEntity.ok(addressesMapper.toAddressesResponseList(address));
    }

    @Override
    public ResponseEntity<AddressesResponse> updateAddressesForUser(UUID userId, UUID addressesId,
            UpdateAddressesRequest updateAddressesRequest) {


        var addressUpdates = addressesMapper.toAddress(updateAddressesRequest);
        var updatedAddress = updateAddressesUseCase.execute(addressesId, addressUpdates, userId,
                AddressOwnerTypeEnum.USER.getDescription());
        return ResponseEntity.ok(addressesMapper.toAddressesResponse(updatedAddress));
    }

    @Override
    public ResponseEntity<Void> deleteAddressesForUser(UUID userId, UUID addressesId) {


        deleteAddressesUseCase.execute(userId, addressesId, AddressOwnerTypeEnum.USER.getDescription());
        return ResponseEntity.noContent().build();
    }
}