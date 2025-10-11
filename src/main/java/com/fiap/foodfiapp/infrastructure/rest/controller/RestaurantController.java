package com.fiap.foodfiapp.infrastructure.rest.controller;

import com.fiap.foodfiapp.api.RestaurantsApi;
import com.fiap.foodfiapp.core.application.usecases.restaurant.*;
import com.fiap.foodfiapp.core.domain.entity.Restaurant;
import com.fiap.foodfiapp.core.domain.exception.UnauthorizedAccessException;
import com.fiap.foodfiapp.core.domain.exception.UnauthorizedException;
import com.fiap.foodfiapp.infrastructure.rest.mapper.RestaurantMapper;
import com.fiap.foodfiapp.infrastructure.security.AuthenticationService;
import com.fiap.foodfiapp.model.CreateRestaurantRequest;
import com.fiap.foodfiapp.model.RestaurantResponse;
import com.fiap.foodfiapp.model.UpdateRestaurantRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class RestaurantController implements RestaurantsApi {

    private final CreateRestaurantUseCase createRestaurantUseCase;
    private final FindRestaurantByIdUseCase findRestaurantByIdUseCase;
    private final UpdateRestaurantUseCase updateRestaurantUseCase;
    private final DeleteRestaurantUseCase deleteRestaurantUseCase;
    private final FindAllPublicRestaurantsUseCase findAllPublicRestaurantsUseCase;
    private final FindMyRestaurantsUseCase findMyRestaurantsUseCase;
    private final FindAllRestaurantsByUserIdUseCase findAllRestaurantsByUserIdUseCase;
    private final ValidateRestaurantOwnershipUseCase validateRestaurantOwnershipUseCase;
    private final AuthenticationService authenticationService;
    private final RestaurantMapper restaurantMapper; // Now using dependency injection

    @Override
    public ResponseEntity<List<RestaurantResponse>> usersUserIdRestaurantsGet(UUID userId) {

        var restaurants = findMyRestaurantsUseCase.execute(userId);
        var response = restaurantMapper.toRestaurantResponseList(restaurants);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<List<RestaurantResponse>> usersUserIdRestaurantsMyRestaurantsGet(UUID userId) {


        var restaurants = findMyRestaurantsUseCase.execute(userId);
        var response = restaurantMapper.toRestaurantResponseList(restaurants);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<List<RestaurantResponse>> usersUserIdRestaurantsPublicListGet(UUID userId) {

        var restaurants = findAllPublicRestaurantsUseCase.execute(userId);
        var response = restaurantMapper.toRestaurantResponseList(restaurants);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<RestaurantResponse> usersUserIdRestaurantsPost(UUID userId, CreateRestaurantRequest createRestaurantRequest) {

        Restaurant restaurant = restaurantMapper.toRestaurant(createRestaurantRequest);

        var createdRestaurant = createRestaurantUseCase.execute(userId, restaurant);
        var response = restaurantMapper.toRestaurantResponse(createdRestaurant);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public ResponseEntity<RestaurantResponse> usersUserIdRestaurantsRestaurantIdGet(UUID userId, UUID restaurantId) {
        if (!validateRestaurantOwnershipUseCase.execute(userId, restaurantId)) {
            throw new UnauthorizedException("Permission denied. Unauthorized operation for this user.");
        }

        Restaurant restaurant = findRestaurantByIdUseCase.execute(restaurantId);

        var response = restaurantMapper.toRestaurantResponse(restaurant);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<RestaurantResponse> usersUserIdRestaurantsRestaurantIdPut(UUID userId, UUID restaurantId, UpdateRestaurantRequest updateRestaurantRequest) {
        if (!validateRestaurantOwnershipUseCase.execute(userId, restaurantId)) {
            throw new UnauthorizedAccessException("You do not have permission to update this restaurant.");
        }

        Restaurant restaurant = restaurantMapper.toRestaurant(updateRestaurantRequest);

        var updatedRestaurant = updateRestaurantUseCase.execute(userId, restaurantId, restaurant);
        var response = restaurantMapper.toRestaurantResponse(updatedRestaurant);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Void> usersUserIdRestaurantsRestaurantIdDelete(UUID userId, UUID restaurantId) {
        if (!validateRestaurantOwnershipUseCase.execute(userId, restaurantId)) {
            throw new UnauthorizedAccessException("You do not have permission to delete this restaurant.");
        }

        deleteRestaurantUseCase.execute(userId, restaurantId);
        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<List<RestaurantResponse>> getPublicRestaurantsList(UUID userId) {

        var restaurants = findAllPublicRestaurantsUseCase.execute(userId);
        var response = restaurantMapper.toRestaurantResponseList(restaurants);
        return ResponseEntity.ok(response);
    }


    public ResponseEntity<List<RestaurantResponse>> getMyRestaurants(UUID userId) {

        var restaurants = findAllRestaurantsByUserIdUseCase.execute(userId);
        var response = restaurantMapper.toRestaurantResponseList(restaurants);
        return ResponseEntity.ok(response);
    }
}
