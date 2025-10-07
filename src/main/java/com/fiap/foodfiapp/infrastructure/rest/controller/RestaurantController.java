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
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class RestaurantController implements RestaurantsApi {

    private final CreateRestaurantUseCase createRestaurantUseCase;
    private final FindRestaurantByIdUseCase findRestaurantByIdUseCase;
    private final UpdateRestaurantUseCase updateRestaurantUseCase;
    private final DeleteRestaurantUseCase deleteRestaurantUseCase;
    private final FindAllRestaurantsByUserIdUseCase findAllRestaurantsByUserIdUseCase; // Para a busca customizada

    private final UserRepository userRepository; // Para enriquecer a resposta com dados do dono

    private final RestaurantMapper restaurantMapper = RestaurantMapper.INSTANCE;
    private final UserMapper userMapper = UserMapper.INSTANCE;

    @Override
    public ResponseEntity<RestaurantResponse> createRestaurant(CreateRestaurantRequest createRestaurantRequest) {
        Restaurant restaurant = restaurantMapper.toRestaurant(createRestaurantRequest);
        Restaurant createdRestaurant = createRestaurantUseCase.execute(restaurant);
        RestaurantResponse response = toRestaurantResponse(createdRestaurant);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public ResponseEntity<Void> deleteRestaurant(UUID id) {
        deleteRestaurantUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<RestaurantResponse> getRestaurant(UUID id) {
        Restaurant restaurant = findRestaurantByIdUseCase.execute(id);
        if (restaurant == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(toRestaurantResponse(restaurant));
    }

    @Override
    public ResponseEntity<List<RestaurantResponse>> listRestaurants() {
        // Nota: A listagem de todos os restaurantes não foi definida nos seus UseCases.
        // Adicionando uma busca por todos os restaurantes de um usuário como exemplo.
        // Se precisar de uma busca global, teríamos que adicionar um novo UseCase.
        // Por enquanto, retornaremos uma lista vazia para cumprir o contrato da API.
        List<RestaurantResponse> response = Collections.emptyList();
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<RestaurantResponse> updateRestaurant(UUID id, UpdateRestaurantRequest updateRestaurantRequest) {
        Restaurant restaurantUpdates = restaurantMapper.toRestaurant(updateRestaurantRequest);
        restaurantUpdates.setId(id); // Define o ID a partir do path

        Restaurant updatedRestaurant = updateRestaurantUseCase.execute(restaurantUpdates);
        return ResponseEntity.ok(toRestaurantResponse(updatedRestaurant));
    }

    /**
     * Método auxiliar para enriquecer a resposta do restaurante com os dados do proprietário.
     */
    private RestaurantResponse toRestaurantResponse(Restaurant restaurant) {
        RestaurantResponse response = restaurantMapper.toRestaurantResponse(restaurant);
        userRepository.findById(restaurant.getUserOwnerId()).ifPresent(owner -> {
            response.setOwner(userMapper.toUserResponse(owner));
        });
        return response;
    }
}