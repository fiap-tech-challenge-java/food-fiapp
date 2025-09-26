package com.fiap.foodfiapp.infrastructure.rest.controller;

import com.fiap.foodfiapp.core.application.usecases.restaurant.CreateRestaurantUseCase;
import com.fiap.foodfiapp.core.application.usecases.restaurant.DeleteRestaurantUseCase;
import com.fiap.foodfiapp.core.application.usecases.restaurant.FindRestaurantUseCase;
import com.fiap.foodfiapp.core.application.usecases.restaurant.UpdateRestaurantUseCase;
import com.fiap.foodfiapp.core.domain.entities.restaurant.*;
import com.fiap.foodfiapp.infrastructure.rest.dto.CreateRestaurantRequestDTO;
import com.fiap.foodfiapp.infrastructure.rest.dto.CreateRestaurantResponseDTO;
import com.fiap.foodfiapp.infrastructure.rest.dto.UpdateRestaurantRequestDTO;
import com.fiap.foodfiapp.infrastructure.rest.mapper.RestaurantMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {
    private final CreateRestaurantUseCase createRestaurantUseCase;
    private final FindRestaurantUseCase findRestaurantUseCase;
    private final UpdateRestaurantUseCase updateRestaurantUseCase;
    private final DeleteRestaurantUseCase deleteRestaurantUseCase;
    private static final RestaurantMapper RESTAURANT_MAPPER = RestaurantMapper.INSTANCE;

    public RestaurantController(CreateRestaurantUseCase createRestaurantUseCase,
                                FindRestaurantUseCase findRestaurantUseCase,
                                UpdateRestaurantUseCase updateRestaurantUseCase,
                                DeleteRestaurantUseCase deleteRestaurantUseCase) {
        this.createRestaurantUseCase = createRestaurantUseCase;
        this.findRestaurantUseCase = findRestaurantUseCase;
        this.updateRestaurantUseCase = updateRestaurantUseCase;
        this.deleteRestaurantUseCase = deleteRestaurantUseCase;
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
    public ResponseEntity<Restaurant> findById(@PathVariable UUID id) {
        Restaurant restaurant = this.findRestaurantUseCase.findById(id);

        return ResponseEntity.ok(restaurant);
    }

    @GetMapping
    @RequestMapping("/{name}/{userId}")
    public ResponseEntity<Restaurant> findByName(@PathVariable String name, @PathVariable UUID userId) {
        Restaurant restaurant = this.findRestaurantUseCase.findByName(name, userId);

        return ResponseEntity.ok(restaurant);
    }

    @GetMapping
    @RequestMapping("/user/{id}")
    public ResponseEntity<List<Restaurant>> findAllByUserId(@PathVariable UUID id) {
        List<Restaurant> restaurant = this.findRestaurantUseCase.findAllByUserId(id);

        return ResponseEntity.ok(restaurant);
    }

    @PutMapping
    public ResponseEntity<Restaurant> update(@RequestBody UpdateRestaurantRequestDTO updateRestaurantRequestDTO) {
        Restaurant restaurant = this.updateRestaurantUseCase.update(
                RESTAURANT_MAPPER.mapToUpdateRestaurant(updateRestaurantRequestDTO)
        );

        return ResponseEntity.ok(restaurant);
    }

    @DeleteMapping
    public ResponseEntity<?> delete(@RequestParam UUID id) {
        this.deleteRestaurantUseCase.deleteRestaurant(id);

        return ResponseEntity.noContent().build();
    }
}
