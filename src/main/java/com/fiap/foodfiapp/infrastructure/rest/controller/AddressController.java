package com.fiap.foodfiapp.infrastructure.rest.controller;

import com.fiap.foodfiapp.core.application.usecases.address.CreateAddressUseCase;
import com.fiap.foodfiapp.core.application.usecases.address.DeleteAddressUseCase;
import com.fiap.foodfiapp.core.application.usecases.address.FindAddressesByOwnerUseCase;
import com.fiap.foodfiapp.core.application.usecases.address.UpdateAddressUseCase;
import com.fiap.foodfiapp.infrastructure.rest.dto.address.AddressResponseDTO;
import com.fiap.foodfiapp.infrastructure.rest.dto.address.CreateAddressRequestDTO;
import com.fiap.foodfiapp.infrastructure.rest.dto.address.UpdateAddressRequestDTO;
import com.fiap.foodfiapp.infrastructure.rest.mapper.AddressMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final CreateAddressUseCase createAddressUseCase;
    private final UpdateAddressUseCase updateAddressUseCase;
    private final DeleteAddressUseCase deleteAddressUseCase;
    private final FindAddressesByOwnerUseCase findAddressesByOwnerUseCase;

    private static final AddressMapper ADDRESS_MAPPER = AddressMapper.INSTANCE;

    @PostMapping("/user/{userId}")
    public ResponseEntity<AddressResponseDTO> createForUser(@PathVariable UUID userId, @RequestBody @Valid CreateAddressRequestDTO createAddressRequestDTO) {
        var address = ADDRESS_MAPPER.toAddress(createAddressRequestDTO);
        var createdAddress = createAddressUseCase.execute(address, userId, "USER");
        return ResponseEntity.status(HttpStatus.CREATED).body(ADDRESS_MAPPER.toAddressResponseDTO(createdAddress));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AddressResponseDTO>> getByUser(@PathVariable UUID userId) {
        var addresses = findAddressesByOwnerUseCase.execute(userId, "USER");
        return ResponseEntity.ok(ADDRESS_MAPPER.toAddressResponseDTOList(addresses));
    }

    @PutMapping("/{addressId}")
    public ResponseEntity<AddressResponseDTO> update(@PathVariable UUID addressId, @RequestBody @Valid UpdateAddressRequestDTO updateAddressRequestDTO) {
        var address = ADDRESS_MAPPER.toAddress(updateAddressRequestDTO);
        var updatedAddress = updateAddressUseCase.execute(addressId, address);
        return ResponseEntity.ok(ADDRESS_MAPPER.toAddressResponseDTO(updatedAddress));
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<Void> delete(@PathVariable UUID addressId) {
        deleteAddressUseCase.execute(addressId);
        return ResponseEntity.noContent().build();
    }
}