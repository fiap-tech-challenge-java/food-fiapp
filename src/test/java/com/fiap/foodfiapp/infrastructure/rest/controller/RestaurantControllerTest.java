package com.fiap.foodfiapp.infrastructure.rest.controller;

import com.fiap.foodfiapp.core.application.usecases.restaurant.*;
import com.fiap.foodfiapp.core.domain.entity.Restaurant;
import com.fiap.foodfiapp.core.domain.exception.UnauthorizedAccessException;
import com.fiap.foodfiapp.core.domain.exception.UnauthorizedException;
import com.fiap.foodfiapp.infrastructure.rest.mapper.RestaurantMapper;
import com.fiap.foodfiapp.infrastructure.security.AuthenticationService;
import com.fiap.foodfiapp.model.CreateRestaurantRequest;
import com.fiap.foodfiapp.model.RestaurantResponse;
import com.fiap.foodfiapp.model.RestaurantResponseOwner;
import com.fiap.foodfiapp.model.UpdateRestaurantRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestaurantControllerTest {

    @Mock
    private CreateRestaurantUseCase createRestaurantUseCase;

    @Mock
    private FindRestaurantByIdUseCase findRestaurantByIdUseCase;

    @Mock
    private UpdateRestaurantUseCase updateRestaurantUseCase;

    @Mock
    private DeleteRestaurantUseCase deleteRestaurantUseCase;

    @Mock
    private FindAllPublicRestaurantsUseCase findAllPublicRestaurantsUseCase;

    @Mock
    private FindMyRestaurantsUseCase findMyRestaurantsUseCase;

    @Mock
    private FindAllRestaurantsByUserIdUseCase findAllRestaurantsByUserIdUseCase;

    @Mock
    private ValidateRestaurantOwnershipUseCase validateRestaurantOwnershipUseCase;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private RestaurantMapper restaurantMapper;

    @InjectMocks
    private RestaurantController restaurantController;

    private UUID userId;
    private UUID restaurantId;
    private Restaurant restaurant;
    private CreateRestaurantRequest createRequest;
    private UpdateRestaurantRequest updateRequest;
    private RestaurantResponse restaurantResponse;
    private RestaurantResponseOwner owner;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        restaurantId = UUID.randomUUID();

        restaurant = new Restaurant();
        restaurant.setId(restaurantId);
        restaurant.setName("Test Restaurant");
        restaurant.setCuisineType("Italian");
        restaurant.setOpeningHours("09:00-22:00");
        restaurant.setUserOwnerId(userId);
        restaurant.setCreatedAt(OffsetDateTime.now());
        restaurant.setUpdatedAt(OffsetDateTime.now());

        createRequest = new CreateRestaurantRequest();
        createRequest.setName("New Restaurant");
        createRequest.setCuisineType("Brazilian");
        createRequest.setOpeningHours("10:00-23:00");

        updateRequest = new UpdateRestaurantRequest();
        updateRequest.setName("Updated Restaurant");
        updateRequest.setCuisineType("Mexican");
        updateRequest.setOpeningHours("11:00-24:00");

        owner = new RestaurantResponseOwner();
        owner.setId(userId);
        owner.setName("Owner Name");

        restaurantResponse = new RestaurantResponse();
        restaurantResponse.setId(restaurantId);
        restaurantResponse.setName("Test Restaurant");
        restaurantResponse.setCuisineType("Italian");
        restaurantResponse.setOpeningHours("09:00-22:00");
        restaurantResponse.setOwner(owner);
    }

    @Test
    void shouldGetUserRestaurantsSuccessfully() {
        // Arrange
        List<Restaurant> restaurants = Arrays.asList(restaurant);
        List<RestaurantResponse> responses = Arrays.asList(restaurantResponse);

        when(findMyRestaurantsUseCase.execute(userId)).thenReturn(restaurants);
        when(restaurantMapper.toRestaurantResponseList(restaurants)).thenReturn(responses);

        // Act
        ResponseEntity<List<RestaurantResponse>> response = restaurantController.usersUserIdRestaurantsGet(userId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(restaurantId, response.getBody().get(0).getId());
        assertEquals("Test Restaurant", response.getBody().get(0).getName());

        verify(findMyRestaurantsUseCase).execute(userId);
        verify(restaurantMapper).toRestaurantResponseList(restaurants);
    }

    @Test
    void shouldGetMyRestaurantsSuccessfully() {
        // Arrange
        List<Restaurant> restaurants = Arrays.asList(restaurant);
        List<RestaurantResponse> responses = Arrays.asList(restaurantResponse);

        when(findMyRestaurantsUseCase.execute(userId)).thenReturn(restaurants);
        when(restaurantMapper.toRestaurantResponseList(restaurants)).thenReturn(responses);

        // Act
        ResponseEntity<List<RestaurantResponse>> response = restaurantController.usersUserIdRestaurantsMyRestaurantsGet(userId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());

        verify(findMyRestaurantsUseCase).execute(userId);
        verify(restaurantMapper).toRestaurantResponseList(restaurants);
    }

    @Test
    void shouldGetPublicRestaurantsListSuccessfully() {
        // Arrange
        List<Restaurant> restaurants = Arrays.asList(restaurant);
        List<RestaurantResponse> responses = Arrays.asList(restaurantResponse);

        when(findAllPublicRestaurantsUseCase.execute(userId)).thenReturn(restaurants);
        when(restaurantMapper.toRestaurantResponseList(restaurants)).thenReturn(responses);

        // Act
        ResponseEntity<List<RestaurantResponse>> response = restaurantController.usersUserIdRestaurantsPublicListGet(userId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());

        verify(findAllPublicRestaurantsUseCase).execute(userId);
        verify(restaurantMapper).toRestaurantResponseList(restaurants);
    }

    @Test
    void shouldReturnEmptyListWhenNoRestaurantsFound() {
        // Arrange
        when(findMyRestaurantsUseCase.execute(userId)).thenReturn(Collections.emptyList());
        when(restaurantMapper.toRestaurantResponseList(Collections.emptyList())).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<List<RestaurantResponse>> response = restaurantController.usersUserIdRestaurantsGet(userId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());

        verify(findMyRestaurantsUseCase).execute(userId);
    }

    @Test
    void shouldCreateRestaurantSuccessfully() {
        // Arrange
        Restaurant newRestaurant = new Restaurant();
        newRestaurant.setName("New Restaurant");
        newRestaurant.setCuisineType("Brazilian");
        newRestaurant.setOpeningHours("10:00-23:00");

        Restaurant createdRestaurant = new Restaurant();
        createdRestaurant.setId(restaurantId);
        createdRestaurant.setName("New Restaurant");
        createdRestaurant.setCuisineType("Brazilian");
        createdRestaurant.setOpeningHours("10:00-23:00");
        createdRestaurant.setUserOwnerId(userId);

        RestaurantResponse createdResponse = new RestaurantResponse();
        createdResponse.setId(restaurantId);
        createdResponse.setName("New Restaurant");
        createdResponse.setCuisineType("Brazilian");

        // Use specific method to avoid ambiguity
        when(restaurantMapper.toRestaurant(any(CreateRestaurantRequest.class))).thenReturn(newRestaurant);
        when(createRestaurantUseCase.execute(userId, newRestaurant)).thenReturn(createdRestaurant);
        when(restaurantMapper.toRestaurantResponse(createdRestaurant)).thenReturn(createdResponse);

        // Act
        ResponseEntity<RestaurantResponse> response = restaurantController.usersUserIdRestaurantsPost(userId, createRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(restaurantId, response.getBody().getId());
        assertEquals("New Restaurant", response.getBody().getName());
        assertEquals("Brazilian", response.getBody().getCuisineType());

        verify(restaurantMapper).toRestaurant(any(CreateRestaurantRequest.class));
        verify(createRestaurantUseCase).execute(userId, newRestaurant);
        verify(restaurantMapper).toRestaurantResponse(createdRestaurant);
    }

    @Test
    void shouldGetRestaurantByIdSuccessfully() {
        // Arrange
        when(validateRestaurantOwnershipUseCase.execute(userId, restaurantId)).thenReturn(true);
        when(findRestaurantByIdUseCase.execute(restaurantId)).thenReturn(restaurant);
        when(restaurantMapper.toRestaurantResponse(restaurant)).thenReturn(restaurantResponse);

        // Act
        ResponseEntity<RestaurantResponse> response = restaurantController.usersUserIdRestaurantsRestaurantIdGet(userId, restaurantId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(restaurantId, response.getBody().getId());

        verify(validateRestaurantOwnershipUseCase).execute(userId, restaurantId);
        verify(findRestaurantByIdUseCase).execute(restaurantId);
        verify(restaurantMapper).toRestaurantResponse(restaurant);
    }

    @Test
    void shouldThrowUnauthorizedExceptionWhenUserDoesNotOwnRestaurant() {
        // Arrange
        when(validateRestaurantOwnershipUseCase.execute(userId, restaurantId)).thenReturn(false);

        // Act & Assert
        UnauthorizedException exception = assertThrows(
            UnauthorizedException.class,
            () -> restaurantController.usersUserIdRestaurantsRestaurantIdGet(userId, restaurantId)
        );

        assertEquals("Permission denied. Unauthorized operation for this user.", exception.getMessage());

        verify(validateRestaurantOwnershipUseCase).execute(userId, restaurantId);
        verify(findRestaurantByIdUseCase, never()).execute(any());
        verify(restaurantMapper, never()).toRestaurantResponse(any());
    }

    @Test
    void shouldUpdateRestaurantSuccessfully() {
        // Arrange
        Restaurant updatedRestaurant = new Restaurant();
        updatedRestaurant.setId(restaurantId);
        updatedRestaurant.setName("Updated Restaurant");
        updatedRestaurant.setCuisineType("Mexican");

        RestaurantResponse updatedResponse = new RestaurantResponse();
        updatedResponse.setId(restaurantId);
        updatedResponse.setName("Updated Restaurant");
        updatedResponse.setCuisineType("Mexican");

        when(validateRestaurantOwnershipUseCase.execute(userId, restaurantId)).thenReturn(true);
        // Use specific method to avoid ambiguity
        when(restaurantMapper.toRestaurant(any(UpdateRestaurantRequest.class))).thenReturn(updatedRestaurant);
        when(updateRestaurantUseCase.execute(userId, restaurantId, updatedRestaurant)).thenReturn(updatedRestaurant);
        when(restaurantMapper.toRestaurantResponse(updatedRestaurant)).thenReturn(updatedResponse);

        // Act
        ResponseEntity<RestaurantResponse> response = restaurantController.usersUserIdRestaurantsRestaurantIdPut(userId, restaurantId, updateRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(restaurantId, response.getBody().getId());
        assertEquals("Updated Restaurant", response.getBody().getName());

        verify(validateRestaurantOwnershipUseCase).execute(userId, restaurantId);
        verify(restaurantMapper).toRestaurant(any(UpdateRestaurantRequest.class));
        verify(updateRestaurantUseCase).execute(userId, restaurantId, updatedRestaurant);
        verify(restaurantMapper).toRestaurantResponse(updatedRestaurant);
    }

    @Test
    void shouldThrowUnauthorizedAccessExceptionWhenUpdatingRestaurantWithoutPermission() {
        // Arrange
        when(validateRestaurantOwnershipUseCase.execute(userId, restaurantId)).thenReturn(false);

        // Act & Assert
        UnauthorizedAccessException exception = assertThrows(
            UnauthorizedAccessException.class,
            () -> restaurantController.usersUserIdRestaurantsRestaurantIdPut(userId, restaurantId, updateRequest)
        );

        assertEquals("You do not have permission to update this restaurant.", exception.getMessage());

        verify(validateRestaurantOwnershipUseCase).execute(userId, restaurantId);
        verify(restaurantMapper, never()).toRestaurant(any(UpdateRestaurantRequest.class));
        verify(updateRestaurantUseCase, never()).execute(any(), any(), any());
    }

    @Test
    void shouldDeleteRestaurantSuccessfully() {
        // Arrange
        when(validateRestaurantOwnershipUseCase.execute(userId, restaurantId)).thenReturn(true);
        doNothing().when(deleteRestaurantUseCase).execute(userId, restaurantId);

        // Act
        ResponseEntity<Void> response = restaurantController.usersUserIdRestaurantsRestaurantIdDelete(userId, restaurantId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());

        verify(validateRestaurantOwnershipUseCase).execute(userId, restaurantId);
        verify(deleteRestaurantUseCase).execute(userId, restaurantId);
    }

    @Test
    void shouldThrowUnauthorizedAccessExceptionWhenDeletingRestaurantWithoutPermission() {
        // Arrange
        when(validateRestaurantOwnershipUseCase.execute(userId, restaurantId)).thenReturn(false);

        // Act & Assert
        UnauthorizedAccessException exception = assertThrows(
            UnauthorizedAccessException.class,
            () -> restaurantController.usersUserIdRestaurantsRestaurantIdDelete(userId, restaurantId)
        );

        assertEquals("You do not have permission to delete this restaurant.", exception.getMessage());

        verify(validateRestaurantOwnershipUseCase).execute(userId, restaurantId);
        verify(deleteRestaurantUseCase, never()).execute(any(), any());
    }

    @Test
    void shouldGetPublicRestaurantsListViaPublicMethod() {
        // Arrange
        List<Restaurant> restaurants = Arrays.asList(restaurant);
        List<RestaurantResponse> responses = Arrays.asList(restaurantResponse);

        when(findAllPublicRestaurantsUseCase.execute(userId)).thenReturn(restaurants);
        when(restaurantMapper.toRestaurantResponseList(restaurants)).thenReturn(responses);

        // Act
        ResponseEntity<List<RestaurantResponse>> response = restaurantController.getPublicRestaurantsList(userId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());

        verify(findAllPublicRestaurantsUseCase).execute(userId);
        verify(restaurantMapper).toRestaurantResponseList(restaurants);
    }

    @Test
    void shouldGetMyRestaurantsViaPublicMethod() {
        // Arrange
        List<Restaurant> restaurants = Arrays.asList(restaurant);
        List<RestaurantResponse> responses = Arrays.asList(restaurantResponse);

        when(findAllRestaurantsByUserIdUseCase.execute(userId)).thenReturn(restaurants);
        when(restaurantMapper.toRestaurantResponseList(restaurants)).thenReturn(responses);

        // Act
        ResponseEntity<List<RestaurantResponse>> response = restaurantController.getMyRestaurants(userId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());

        verify(findAllRestaurantsByUserIdUseCase).execute(userId);
        verify(restaurantMapper).toRestaurantResponseList(restaurants);
    }

    @Test
    void shouldHandleMultipleRestaurantsInList() {
        // Arrange
        UUID secondRestaurantId = UUID.randomUUID();
        Restaurant secondRestaurant = new Restaurant();
        secondRestaurant.setId(secondRestaurantId);
        secondRestaurant.setName("Second Restaurant");
        secondRestaurant.setCuisineType("Japanese");

        RestaurantResponse secondResponse = new RestaurantResponse();
        secondResponse.setId(secondRestaurantId);
        secondResponse.setName("Second Restaurant");
        secondResponse.setCuisineType("Japanese");

        List<Restaurant> restaurants = Arrays.asList(restaurant, secondRestaurant);
        List<RestaurantResponse> responses = Arrays.asList(restaurantResponse, secondResponse);

        when(findMyRestaurantsUseCase.execute(userId)).thenReturn(restaurants);
        when(restaurantMapper.toRestaurantResponseList(restaurants)).thenReturn(responses);

        // Act
        ResponseEntity<List<RestaurantResponse>> response = restaurantController.usersUserIdRestaurantsGet(userId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());

        assertEquals(restaurantId, response.getBody().get(0).getId());
        assertEquals("Test Restaurant", response.getBody().get(0).getName());

        assertEquals(secondRestaurantId, response.getBody().get(1).getId());
        assertEquals("Second Restaurant", response.getBody().get(1).getName());

        verify(findMyRestaurantsUseCase).execute(userId);
        verify(restaurantMapper).toRestaurantResponseList(restaurants);
    }

    @Test
    void shouldHandleCreateRestaurantWithMinimalData() {
        // Arrange
        CreateRestaurantRequest minimalRequest = new CreateRestaurantRequest();
        minimalRequest.setName("Minimal Restaurant");
        // cuisine type and opening hours are null

        Restaurant minimalRestaurant = new Restaurant();
        minimalRestaurant.setName("Minimal Restaurant");

        Restaurant createdMinimalRestaurant = new Restaurant();
        createdMinimalRestaurant.setId(restaurantId);
        createdMinimalRestaurant.setName("Minimal Restaurant");
        createdMinimalRestaurant.setUserOwnerId(userId);

        RestaurantResponse minimalResponse = new RestaurantResponse();
        minimalResponse.setId(restaurantId);
        minimalResponse.setName("Minimal Restaurant");

        when(restaurantMapper.toRestaurant(any(CreateRestaurantRequest.class))).thenReturn(minimalRestaurant);
        when(createRestaurantUseCase.execute(userId, minimalRestaurant)).thenReturn(createdMinimalRestaurant);
        when(restaurantMapper.toRestaurantResponse(createdMinimalRestaurant)).thenReturn(minimalResponse);

        // Act
        ResponseEntity<RestaurantResponse> response = restaurantController.usersUserIdRestaurantsPost(userId, minimalRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Minimal Restaurant", response.getBody().getName());

        verify(restaurantMapper).toRestaurant(any(CreateRestaurantRequest.class));
        verify(createRestaurantUseCase).execute(userId, minimalRestaurant);
        verify(restaurantMapper).toRestaurantResponse(createdMinimalRestaurant);
    }

    @Test
    void shouldHandleUpdateRestaurantWithPartialData() {
        // Arrange
        UpdateRestaurantRequest partialUpdate = new UpdateRestaurantRequest();
        partialUpdate.setName("Partially Updated Restaurant");
        // other fields are null

        Restaurant partialRestaurant = new Restaurant();
        partialRestaurant.setName("Partially Updated Restaurant");

        Restaurant updatedRestaurant = new Restaurant();
        updatedRestaurant.setId(restaurantId);
        updatedRestaurant.setName("Partially Updated Restaurant");
        updatedRestaurant.setUserOwnerId(userId);

        RestaurantResponse partialResponse = new RestaurantResponse();
        partialResponse.setId(restaurantId);
        partialResponse.setName("Partially Updated Restaurant");

        when(validateRestaurantOwnershipUseCase.execute(userId, restaurantId)).thenReturn(true);
        when(restaurantMapper.toRestaurant(any(UpdateRestaurantRequest.class))).thenReturn(partialRestaurant);
        when(updateRestaurantUseCase.execute(userId, restaurantId, partialRestaurant)).thenReturn(updatedRestaurant);
        when(restaurantMapper.toRestaurantResponse(updatedRestaurant)).thenReturn(partialResponse);

        // Act
        ResponseEntity<RestaurantResponse> response = restaurantController.usersUserIdRestaurantsRestaurantIdPut(userId, restaurantId, partialUpdate);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Partially Updated Restaurant", response.getBody().getName());

        verify(validateRestaurantOwnershipUseCase).execute(userId, restaurantId);
        verify(restaurantMapper).toRestaurant(any(UpdateRestaurantRequest.class));
        verify(updateRestaurantUseCase).execute(userId, restaurantId, partialRestaurant);
        verify(restaurantMapper).toRestaurantResponse(updatedRestaurant);
    }

    @Test
    void shouldVerifyAllUseCasesAreCalledWithCorrectParameters() {
        // Test for create
        Restaurant newRestaurant = new Restaurant();
        when(restaurantMapper.toRestaurant(any(CreateRestaurantRequest.class))).thenReturn(newRestaurant);
        when(createRestaurantUseCase.execute(userId, newRestaurant)).thenReturn(restaurant);
        when(restaurantMapper.toRestaurantResponse(restaurant)).thenReturn(restaurantResponse);

        restaurantController.usersUserIdRestaurantsPost(userId, createRequest);

        verify(createRestaurantUseCase).execute(eq(userId), eq(newRestaurant));

        // Test for validation
        when(validateRestaurantOwnershipUseCase.execute(userId, restaurantId)).thenReturn(true);
        when(findRestaurantByIdUseCase.execute(restaurantId)).thenReturn(restaurant);

        restaurantController.usersUserIdRestaurantsRestaurantIdGet(userId, restaurantId);

        verify(validateRestaurantOwnershipUseCase).execute(eq(userId), eq(restaurantId));
        verify(findRestaurantByIdUseCase).execute(eq(restaurantId));
    }
}
