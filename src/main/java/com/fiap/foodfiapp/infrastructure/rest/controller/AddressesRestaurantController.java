package com.fiap.foodfiapp.infrastructure.rest.controller;

import com.fiap.foodfiapp.core.application.usecases.addressesrestaurant.CreateAddressesRestaurantUseCase;
import com.fiap.foodfiapp.core.application.usecases.addressesrestaurant.DeleteAddressesRestaurantUseCase;
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
    private final CreateAddressesRestaurantUseCase createAddressesRestaurantUseCase;
    private final DeleteAddressesRestaurantUseCase deleteAddressesRestaurantUseCase;
    private static final AddressesRestaurantMapper ADDRESSES_RESTAURANT_MAPPER = AddressesRestaurantMapper.INSTANCE;

    public AddressesRestaurantController(CreateAddressesRestaurantUseCase createAddressesRestaurantUseCase,
                                         DeleteAddressesRestaurantUseCase deleteAddressesRestaurantUseCase) {
        this.createAddressesRestaurantUseCase = createAddressesRestaurantUseCase;
        this.deleteAddressesRestaurantUseCase = deleteAddressesRestaurantUseCase;
    }

    @PostMapping
    public ResponseEntity<CreateAddressesRestaurantResponseDTO> create(@RequestBody @Valid CreateAddressesRestaurantRequestDTO createAddressesRestaurantRequestDTO) {
        CreateAddressesRestaurant createAddressesRestaurant = ADDRESSES_RESTAURANT_MAPPER.mapToCreateAddressesRestaurant(createAddressesRestaurantRequestDTO);
        CreatedAddressesRestaurant createdAddressesRestaurant = this.createAddressesRestaurantUseCase.execute(createAddressesRestaurant);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ADDRESSES_RESTAURANT_MAPPER.mapToCreateAddressesRestaurantResponseDTO(createdAddressesRestaurant));
    }

    @DeleteMapping("/{restaurantId}")
    public ResponseEntity<?> delete(@PathVariable UUID restaurantId) {
        this.deleteAddressesRestaurantUseCase.deleteAddressesRestaurant(restaurantId);

        return ResponseEntity.noContent().build();
    }
}
