package com.fiap.foodfiapp.infrastructure.rest.controller;

import com.fiap.foodfiapp.core.application.usecases.restaurant.CreateRestaurantUseCase;
import com.fiap.foodfiapp.core.application.usecases.restaurant.FindRestaurantUseCase;
import com.fiap.foodfiapp.core.application.usecases.restaurant.UpdateRestaurantUseCase;
import com.fiap.foodfiapp.core.domain.entities.*;
import com.fiap.foodfiapp.infrastructure.rest.mapper.RestaurantMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {
    private final CreateRestaurantUseCase createRestaurantUseCase;
    private final FindRestaurantUseCase findRestaurantUseCase;
    private final UpdateRestaurantUseCase updateRestaurantUseCase;
    private static final RestaurantMapper RESTAURANT_MAPPER = RestaurantMapper.INSTANCE;

    public RestaurantController(CreateRestaurantUseCase createRestaurantUseCase,
                                FindRestaurantUseCase findRestaurantUseCase,
                                UpdateRestaurantUseCase updateRestaurantUseCase) {
        this.createRestaurantUseCase = createRestaurantUseCase;
        this.findRestaurantUseCase = findRestaurantUseCase;
        this.updateRestaurantUseCase = updateRestaurantUseCase;
    }

    @PostMapping
    public ResponseEntity<CreateRestaurantResponseDTO> create(@RequestBody @Valid CreateRestaurantRequestDTO createRestaurantRequestDTO) {
        CreateRestaurant createRestaurant = RESTAURANT_MAPPER.mapToCreateRestaurant(createRestaurantRequestDTO);
        CreatedRestaurant createdRestaurant = this.createRestaurantUseCase.execute(createRestaurant);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(RESTAURANT_MAPPER.mapToCreateRestaurantResponseDTO(createdRestaurant));
    }

    @GetMapping
    @RequestMapping("/{id}")
    public Restaurant findById(@PathVariable UUID id) {
        return this.findRestaurantUseCase.findById(id);
    }

    @GetMapping
    @RequestMapping("/{name}/{userId}")
    public Restaurant findByName(@PathVariable String name, @PathVariable UUID userId) {
        return this.findRestaurantUseCase.findByName(name, userId);
    }

    @GetMapping
    @RequestMapping("/{userId}")
    public Restaurant findAllByUserId(@PathVariable UUID userId) {
        return this.findRestaurantUseCase.findAllByUserId(userId);
    }

    @PutMapping
    public Restaurant update(@RequestBody UpdateRestaurantRequestDTO updateRestaurantRequestDTO) {
        return this.updateRestaurantUseCase.update(
                RESTAURANT_MAPPER.mapToUpdateRestaurant(updateRestaurantRequestDTO)
        );
    }

    @DeleteMapping
    public void delete(@RequestParam UUID id) {
    }
}
