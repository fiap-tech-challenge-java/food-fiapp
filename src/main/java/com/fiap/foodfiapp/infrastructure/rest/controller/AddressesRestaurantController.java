package com.fiap.foodfiapp.infrastructure.rest.controller;

import com.fiap.foodfiapp.core.application.usecases.address.CreateAddressUseCase;
import com.fiap.foodfiapp.core.application.usecases.address.DeleteAddressUseCase;
import com.fiap.foodfiapp.core.domain.entities.addressesrestaurant.CreateAddressesRestaurant;
import com.fiap.foodfiapp.core.domain.entities.addressesrestaurant.CreatedAddressesRestaurant;
import com.fiap.foodfiapp.infrastructure.rest.dto.addressesrestaurant.CreateAddressesRestaurantRequestDTO;
import com.fiap.foodfiapp.infrastructure.rest.dto.addressesrestaurant.CreateAddressesRestaurantResponseDTO;
import com.fiap.foodfiapp.infrastructure.rest.mapper.AddressesRestaurantMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/restaurants/addresses")
public class AddressesRestaurantController {
    private final CreateAddressUseCase createAddressUseCase;
    private final DeleteAddressUseCase deleteAddressUseCase;
    private static final AddressesRestaurantMapper ADDRESSES_RESTAURANT_MAPPER = AddressesRestaurantMapper.INSTANCE;

    public AddressesRestaurantController(CreateAddressUseCase createAddressUseCase,
                                         DeleteAddressUseCase deleteAddressUseCase) {
        this.createAddressUseCase = createAddressUseCase;
        this.deleteAddressUseCase = deleteAddressUseCase;
    }

    @PostMapping
    public ResponseEntity<CreateAddressesRestaurantResponseDTO> create(@RequestBody @Valid CreateAddressesRestaurantRequestDTO createAddressesRestaurantRequestDTO) {
        CreateAddressesRestaurant createAddressesRestaurant = ADDRESSES_RESTAURANT_MAPPER.mapToCreateAddressesRestaurant(createAddressesRestaurantRequestDTO);
        CreatedAddressesRestaurant createdAddressesRestaurant = this.createAddressUseCase.execute(createAddressesRestaurant);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ADDRESSES_RESTAURANT_MAPPER.mapToCreateAddressesRestaurantResponseDTO(createdAddressesRestaurant));
    }

    @DeleteMapping("/{restaurantId}")
    public ResponseEntity<?> delete(@PathVariable UUID restaurantId) {
        this.deleteAddressUseCase.deleteAddressesRestaurant(restaurantId);

        return ResponseEntity.noContent().build();
    }
}
