package com.fiap.foodfiapp.infrastructure.rest.controller;

import com.fiap.foodfiapp.api.RestaurantsApi;
import com.fiap.foodfiapp.core.application.usecases.restaurant.*;
import com.fiap.foodfiapp.infrastructure.rest.mapper.RestaurantMapper;
import com.fiap.foodfiapp.model.CreateRestaurantRequest;
import com.fiap.foodfiapp.model.RestaurantResponse;
import com.fiap.foodfiapp.model.UpdateRestaurantRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class RestaurantController implements RestaurantsApi {

    private final CreateRestaurantUseCase createRestaurantUseCase;
    private final FindRestaurantByIdUseCase findRestaurantByIdUseCase;
    private final UpdateRestaurantUseCase updateRestaurantUseCase;
    private final DeleteRestaurantUseCase deleteRestaurantUseCase;
    private final FindAllPublicRestaurantsUseCase findAllPublicRestaurantsUseCase;

    private final RestaurantMapper restaurantMapper = RestaurantMapper.INSTANCE;

    @Override
    public ResponseEntity<List<RestaurantResponse>> restaurantsGet() {
        // TODO: Criar e usar um FindAllRestaurantsUseCase
        return ResponseEntity.ok(Collections.emptyList());
    }

    @Override
    public ResponseEntity<Void> restaurantsIdDelete(UUID id) {
        deleteRestaurantUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<RestaurantResponse> restaurantsIdGet(UUID id) {
        // O controller não enriquece mais a resposta. Apenas mapeia o resultado do caso de uso.
        return Optional.ofNullable(findRestaurantByIdUseCase.execute(id))
                .map(restaurantMapper::toRestaurantResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<RestaurantResponse> restaurantsIdPut(UUID id, UpdateRestaurantRequest updateRestaurantRequest) {
        // Mapeia o DTO de requisição para a entidade de domínio
        var restaurantUpdates = restaurantMapper.toRestaurant(updateRestaurantRequest);

        // Chama o use case com a entidade de domínio
        var updatedRestaurant = updateRestaurantUseCase.execute(id, restaurantUpdates);
        return ResponseEntity.ok(restaurantMapper.toRestaurantResponse(updatedRestaurant));
    }

    @Override
    public ResponseEntity<RestaurantResponse> restaurantsPost(CreateRestaurantRequest createRestaurantRequest) {
        // Mapeia o DTO de requisição para a entidade de domínio
        // O mapper agora faz o mapeamento completo, incluindo o ownerId -> userOwnerId
        var restaurant = restaurantMapper.toRestaurant(createRestaurantRequest);

        // Chama o use case com a entidade de domínio
        var createdRestaurant = createRestaurantUseCase.execute(restaurant);
        return ResponseEntity.status(HttpStatus.CREATED).body(restaurantMapper.toRestaurantResponse(createdRestaurant));
    }

    @GetMapping("/restaurants/public")
    public ResponseEntity<List<RestaurantResponse>> getPublicRestaurants() {
        var restaurants = findAllPublicRestaurantsUseCase.execute();
        return ResponseEntity.ok(restaurantMapper.toRestaurantResponseListWithoutOwner(restaurants));
    }
}