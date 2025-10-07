package com.fiap.foodfiapp.infrastructure.rest.controller;

import com.fiap.foodfiapp.api.RestaurantsApi;
import com.fiap.foodfiapp.core.application.usecases.restaurant.CreateRestaurantUseCase;
import com.fiap.foodfiapp.core.application.usecases.restaurant.DeleteRestaurantUseCase;
import com.fiap.foodfiapp.core.application.usecases.restaurant.FindRestaurantByIdUseCase;
import com.fiap.foodfiapp.core.application.usecases.restaurant.UpdateRestaurantUseCase;
import com.fiap.foodfiapp.core.application.usecases.restaurant.FindAllPublicRestaurantsUseCase;
import com.fiap.foodfiapp.core.domain.entity.Restaurant;
import com.fiap.foodfiapp.core.domain.port.UserRepository;
import com.fiap.foodfiapp.infrastructure.rest.mapper.RestaurantMapper;
import com.fiap.foodfiapp.infrastructure.rest.mapper.UserMapper;
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
    private final UserRepository userRepository;

    private final RestaurantMapper restaurantMapper = RestaurantMapper.INSTANCE;
    private final UserMapper userMapper = UserMapper.INSTANCE;

    // ===== Implementações geradas pelo OpenAPI =====

    @Override
    public ResponseEntity<List<RestaurantResponse>> restaurantsGet() {
        // Sem UC para "listar todos", retornamos lista vazia para cumprir contrato.
        // TODO: criar FindAllRestaurantsUseCase e mapear aqui.
        return ResponseEntity.ok(Collections.emptyList());
    }

    @Override
    public ResponseEntity<Void> restaurantsIdDelete(UUID id) {
        deleteRestaurantUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<RestaurantResponse> restaurantsIdGet(UUID id) {
        return Optional.ofNullable(findRestaurantByIdUseCase.execute(id))
                .map(this::enrichResponseWithOwner)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<RestaurantResponse> restaurantsIdPut(UUID id, UpdateRestaurantRequest updateRestaurantRequest) {
        Restaurant updates = restaurantMapper.toRestaurant(updateRestaurantRequest);
        updates.setId(id);
        Restaurant updated = updateRestaurantUseCase.execute(updates);
        return ResponseEntity.ok(enrichResponseWithOwner(updated));
    }

    @Override
    public ResponseEntity<RestaurantResponse> restaurantsPost(CreateRestaurantRequest createRestaurantRequest) {
        Restaurant restaurant = restaurantMapper.toRestaurant(createRestaurantRequest);
        // Pegamos o ID do usuário "autenticado" a partir do request (ownerId) até termos integração com Security
        UUID authenticatedUserId = createRestaurantRequest.getOwnerId();
        Restaurant created = createRestaurantUseCase.execute(authenticatedUserId, restaurant);
        return ResponseEntity.status(HttpStatus.CREATED).body(enrichResponseWithOwner(created));
    }

    @GetMapping("/restaurants/public")
    public ResponseEntity<List<RestaurantResponse>> getPublicRestaurants() {
        List<Restaurant> restaurants = findAllPublicRestaurantsUseCase.execute();
        return ResponseEntity.ok(restaurantMapper.toRestaurantResponseListWithoutOwner(restaurants));
    }

    // ===== Auxiliar =====

    private RestaurantResponse enrichResponseWithOwner(Restaurant restaurant) {
        RestaurantResponse resp = restaurantMapper.toRestaurantResponse(restaurant);
        if (restaurant.getUserOwnerId() != null) {
            userRepository.findById(restaurant.getUserOwnerId())
                    .ifPresent(owner -> resp.setOwner(userMapper.toUserResponse(owner)));
        }
        return resp;
    }
}
