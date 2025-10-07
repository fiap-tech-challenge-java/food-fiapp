package com.fiap.foodfiapp.infrastructure.rest.controller;

import com.fiap.foodfiapp.api.RestaurantsApi;
import com.fiap.foodfiapp.core.application.usecases.restaurant.*;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class RestaurantController implements RestaurantsApi {

    private final CreateRestaurantUseCase createRestaurantUseCase;
    private final FindRestaurantByIdUseCase findRestaurantByIdUseCase;
    private final UpdateRestaurantUseCase updateRestaurantUseCase;
    private final DeleteRestaurantUseCase deleteRestaurantUseCase;
    private final FindAllRestaurantsByUserIdUseCase findAllRestaurantsByUserIdUseCase;

    private final UserRepository userRepository; // Para buscar os dados do dono

    private final RestaurantMapper restaurantMapper = RestaurantMapper.INSTANCE;
    private final UserMapper userMapper = UserMapper.INSTANCE;

    @Override
    public ResponseEntity<RestaurantResponse> createRestaurant(CreateRestaurantRequest createRestaurantRequest) {
        Restaurant restaurant = restaurantMapper.toRestaurant(createRestaurantRequest);
        // O ownerId vem no DTO, precisamos passá-lo para a entidade de domínio
        restaurant.setUserOwnerId(createRestaurantRequest.getOwnerId());

        Restaurant createdRestaurant = createRestaurantUseCase.execute(restaurant);
        RestaurantResponse response = enrichResponseWithOwner(createdRestaurant);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public ResponseEntity<Void> deleteRestaurant(UUID id) {
        deleteRestaurantUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<RestaurantResponse> getRestaurant(UUID id) {
        return Optional.ofNullable(findRestaurantByIdUseCase.execute(id))
                .map(this::enrichResponseWithOwner)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<List<RestaurantResponse>> listRestaurants() {
        // A API define um endpoint para listar TODOS os restaurantes.
        // Se a regra de negócio for listar apenas por usuário, o openapi.yaml precisaria ser ajustado.
        // Por enquanto, vamos retornar uma lista vazia para cumprir o contrato.
        // Se houver um use case para buscar todos, ele seria usado aqui.
        List<RestaurantResponse> response = Collections.emptyList();
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<RestaurantResponse> updateRestaurant(UUID id, UpdateRestaurantRequest updateRestaurantRequest) {
        Restaurant restaurantUpdates = restaurantMapper.toRestaurant(updateRestaurantRequest);
        restaurantUpdates.setId(id); // O ID vem do path da URL

        Restaurant updatedRestaurant = updateRestaurantUseCase.execute(restaurantUpdates);
        return ResponseEntity.ok(enrichResponseWithOwner(updatedRestaurant));
    }

    /**
     * Método auxiliar para popular os dados do proprietário (owner) na resposta.
     */
    private RestaurantResponse enrichResponseWithOwner(Restaurant restaurant) {
        RestaurantResponse response = restaurantMapper.toRestaurantResponse(restaurant);
        if (restaurant.getUserOwnerId() != null) {
            userRepository.findById(restaurant.getUserOwnerId()).ifPresent(owner -> {
                response.setOwner(userMapper.toUserResponse(owner));
            });
        }
        return response;
    }
}