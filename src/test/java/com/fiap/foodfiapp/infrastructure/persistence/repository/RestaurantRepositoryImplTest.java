package com.fiap.foodfiapp.infrastructure.persistence.repository;

import com.fiap.foodfiapp.core.domain.entity.Addresses;
import com.fiap.foodfiapp.core.domain.entity.Restaurant;
import com.fiap.foodfiapp.infrastructure.persistence.entity.RestaurantEntity;
import com.fiap.foodfiapp.infrastructure.persistence.springdata.RestaurantSpringDataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestaurantRepositoryImplTest {

    @Mock
    private RestaurantSpringDataRepository restaurantSpringDataRepository;

    @InjectMocks
    private RestaurantRepositoryImpl restaurantRepositoryImpl;

    private UUID restaurantId;
    private UUID userOwnerId;
    private Restaurant restaurant;
    private RestaurantEntity restaurantEntity;
    private Addresses address;

    @BeforeEach
    void setUp() {
        restaurantId = UUID.randomUUID();
        userOwnerId = UUID.randomUUID();

        address = new Addresses(
            UUID.randomUUID(),
            "Rua dos Restaurantes",
            "100",
            "Sala 1",
            "Centro",
            "São Paulo",
            "SP",
            "01000-000"
        );

        restaurant = new Restaurant(
            restaurantId,
            "Restaurante Teste",
            "Italiana",
            "08:00-22:00",
            userOwnerId,
            "Um restaurante italiano tradicional",
            address
        );

        restaurantEntity = new RestaurantEntity();
        restaurantEntity.setId(restaurantId);
        restaurantEntity.setName("Restaurante Teste");
        restaurantEntity.setCuisineType("Italiana");
        restaurantEntity.setOpeningHours("08:00-22:00");
        restaurantEntity.setUserOwnerId(userOwnerId);
        restaurantEntity.setDescription("Um restaurante italiano tradicional");
        restaurantEntity.setIsActive(true);
    }

    // ========== SAVE TESTS ==========

    @Test
    void shouldSaveRestaurantSuccessfully() {
        // Arrange
        when(restaurantSpringDataRepository.save(any(RestaurantEntity.class))).thenReturn(restaurantEntity);

        // Act
        Restaurant result = restaurantRepositoryImpl.save(restaurant);

        // Assert
        assertNotNull(result);
        assertEquals(restaurantId, result.getId());
        assertEquals("Restaurante Teste", result.getName());
        assertEquals("Italiana", result.getCuisineType());
        assertEquals("08:00-22:00", result.getOpeningHours());
        assertEquals(userOwnerId, result.getUserOwnerId());
        assertEquals("Um restaurante italiano tradicional", result.getDescription());

        verify(restaurantSpringDataRepository).save(any(RestaurantEntity.class));
    }

    @Test
    void shouldPropagateExceptionFromSpringDataRepositoryOnSave() {
        // Arrange
        when(restaurantSpringDataRepository.save(any(RestaurantEntity.class)))
            .thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> restaurantRepositoryImpl.save(restaurant));
        verify(restaurantSpringDataRepository).save(any(RestaurantEntity.class));
    }

    // ========== FIND BY ID TESTS ==========

    @Test
    void shouldFindRestaurantByIdSuccessfully() {
        // Arrange
        when(restaurantSpringDataRepository.findByIdAndIsActiveTrue(restaurantId))
            .thenReturn(Optional.of(restaurantEntity));

        // Act
        Restaurant result = restaurantRepositoryImpl.findById(restaurantId);

        // Assert
        assertNotNull(result);
        assertEquals(restaurantId, result.getId());
        assertEquals("Restaurante Teste", result.getName());
        assertEquals("Italiana", result.getCuisineType());

        verify(restaurantSpringDataRepository).findByIdAndIsActiveTrue(restaurantId);
    }

    @Test
    void shouldReturnNullWhenRestaurantNotFoundById() {
        // Arrange
        when(restaurantSpringDataRepository.findByIdAndIsActiveTrue(restaurantId))
            .thenReturn(Optional.empty());

        // Act
        Restaurant result = restaurantRepositoryImpl.findById(restaurantId);

        // Assert
        assertNull(result);
        verify(restaurantSpringDataRepository).findByIdAndIsActiveTrue(restaurantId);
    }

    @Test
    void shouldReturnNullWhenRestaurantIsInactive() {
        // Arrange
        UUID inactiveRestaurantId = UUID.randomUUID();
        when(restaurantSpringDataRepository.findByIdAndIsActiveTrue(inactiveRestaurantId))
            .thenReturn(Optional.empty());

        // Act
        Restaurant result = restaurantRepositoryImpl.findById(inactiveRestaurantId);

        // Assert
        assertNull(result);
        verify(restaurantSpringDataRepository).findByIdAndIsActiveTrue(inactiveRestaurantId);
    }

    @Test
    void shouldHandleNullIdInFindById() {
        // Arrange
        when(restaurantSpringDataRepository.findByIdAndIsActiveTrue(null))
            .thenReturn(Optional.empty());

        // Act
        Restaurant result = restaurantRepositoryImpl.findById(null);

        // Assert
        assertNull(result);
        verify(restaurantSpringDataRepository).findByIdAndIsActiveTrue(null);
    }

    // ========== EXISTS BY ID TESTS ==========

    @Test
    void shouldReturnTrueWhenRestaurantExists() {
        // Arrange
        when(restaurantSpringDataRepository.existsById(restaurantId)).thenReturn(true);

        // Act
        boolean result = restaurantRepositoryImpl.existsById(restaurantId);

        // Assert
        assertTrue(result);
        verify(restaurantSpringDataRepository).existsById(restaurantId);
    }

    @Test
    void shouldReturnFalseWhenRestaurantDoesNotExist() {
        // Arrange
        when(restaurantSpringDataRepository.existsById(restaurantId)).thenReturn(false);

        // Act
        boolean result = restaurantRepositoryImpl.existsById(restaurantId);

        // Assert
        assertFalse(result);
        verify(restaurantSpringDataRepository).existsById(restaurantId);
    }

    @Test
    void shouldHandleNullIdInExistsById() {
        // Arrange
        when(restaurantSpringDataRepository.existsById(null)).thenReturn(false);

        // Act
        boolean result = restaurantRepositoryImpl.existsById(null);

        // Assert
        assertFalse(result);
        verify(restaurantSpringDataRepository).existsById(null);
    }

    // ========== FIND BY NAME AND USER TESTS ==========

    @Test
    void shouldFindRestaurantByNameAndUserSuccessfully() {
        // Arrange
        String restaurantName = "Restaurante Teste";
        when(restaurantSpringDataRepository.findByUserOwnerIdAndNameAndIsActiveTrue(userOwnerId, restaurantName))
            .thenReturn(Optional.of(restaurantEntity));

        // Act
        Restaurant result = restaurantRepositoryImpl.findByNameAndUser(restaurantName, userOwnerId);

        // Assert
        assertNotNull(result);
        assertEquals(restaurantId, result.getId());
        assertEquals(restaurantName, result.getName());
        assertEquals(userOwnerId, result.getUserOwnerId());

        verify(restaurantSpringDataRepository).findByUserOwnerIdAndNameAndIsActiveTrue(userOwnerId, restaurantName);
    }

    @Test
    void shouldReturnNullWhenRestaurantNotFoundByNameAndUser() {
        // Arrange
        String restaurantName = "Restaurante Inexistente";
        when(restaurantSpringDataRepository.findByUserOwnerIdAndNameAndIsActiveTrue(userOwnerId, restaurantName))
            .thenReturn(Optional.empty());

        // Act
        Restaurant result = restaurantRepositoryImpl.findByNameAndUser(restaurantName, userOwnerId);

        // Assert
        assertNull(result);
        verify(restaurantSpringDataRepository).findByUserOwnerIdAndNameAndIsActiveTrue(userOwnerId, restaurantName);
    }

    @Test
    void shouldHandleNullParametersInFindByNameAndUser() {
        // Arrange
        when(restaurantSpringDataRepository.findByUserOwnerIdAndNameAndIsActiveTrue(null, null))
            .thenReturn(Optional.empty());

        // Act
        Restaurant result = restaurantRepositoryImpl.findByNameAndUser(null, null);

        // Assert
        assertNull(result);
        verify(restaurantSpringDataRepository).findByUserOwnerIdAndNameAndIsActiveTrue(null, null);
    }

    // ========== CHANGE OWNER TESTS ==========

    @Test
    void shouldReturnNullForChangeOwnerMethod() {
        // Act
        Restaurant result = restaurantRepositoryImpl.changeOwner(restaurantId, UUID.randomUUID());

        // Assert
        assertNull(result);
        // This method doesn't interact with the repository as it's not implemented
        verifyNoInteractions(restaurantSpringDataRepository);
    }

    // ========== FIND ALL BY USER ID TESTS ==========

    @Test
    void shouldFindAllRestaurantsByUserIdSuccessfully() {
        // Arrange
        UUID secondRestaurantId = UUID.randomUUID();
        RestaurantEntity secondEntity = new RestaurantEntity();
        secondEntity.setId(secondRestaurantId);
        secondEntity.setName("Segundo Restaurante");
        secondEntity.setCuisineType("Brasileira");
        secondEntity.setUserOwnerId(userOwnerId);
        secondEntity.setIsActive(true);

        List<RestaurantEntity> entities = Arrays.asList(restaurantEntity, secondEntity);

        when(restaurantSpringDataRepository.findAllByUserOwnerIdAndIsActiveTrue(userOwnerId))
            .thenReturn(entities);

        // Act
        List<Restaurant> result = restaurantRepositoryImpl.findAllByUserId(userOwnerId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Restaurante Teste", result.get(0).getName());
        assertEquals("Segundo Restaurante", result.get(1).getName());
        assertEquals(userOwnerId, result.get(0).getUserOwnerId());
        assertEquals(userOwnerId, result.get(1).getUserOwnerId());

        verify(restaurantSpringDataRepository).findAllByUserOwnerIdAndIsActiveTrue(userOwnerId);
    }

    @Test
    void shouldReturnEmptyListWhenNoRestaurantsFoundByUserId() {
        // Arrange
        when(restaurantSpringDataRepository.findAllByUserOwnerIdAndIsActiveTrue(userOwnerId))
            .thenReturn(Collections.emptyList());

        // Act
        List<Restaurant> result = restaurantRepositoryImpl.findAllByUserId(userOwnerId);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(restaurantSpringDataRepository).findAllByUserOwnerIdAndIsActiveTrue(userOwnerId);
    }

    @Test
    void shouldHandleNullUserIdInFindAllByUserId() {
        // Arrange
        when(restaurantSpringDataRepository.findAllByUserOwnerIdAndIsActiveTrue(null))
            .thenReturn(Collections.emptyList());

        // Act
        List<Restaurant> result = restaurantRepositoryImpl.findAllByUserId(null);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(restaurantSpringDataRepository).findAllByUserOwnerIdAndIsActiveTrue(null);
    }

    // ========== DELETE TESTS (SOFT DELETE) ==========

    @Test
    void shouldSoftDeleteRestaurantSuccessfully() {
        // Arrange
        when(restaurantSpringDataRepository.findById(restaurantId)).thenReturn(Optional.of(restaurantEntity));
        when(restaurantSpringDataRepository.save(any(RestaurantEntity.class))).thenReturn(restaurantEntity);

        // Act
        restaurantRepositoryImpl.delete(restaurantId);

        // Assert
        verify(restaurantSpringDataRepository).findById(restaurantId);
        verify(restaurantSpringDataRepository).save(argThat(entity -> !entity.getIsActive()));
    }

    @Test
    void shouldHandleDeleteWhenRestaurantNotFound() {
        // Arrange
        when(restaurantSpringDataRepository.findById(restaurantId)).thenReturn(Optional.empty());

        // Act
        restaurantRepositoryImpl.delete(restaurantId);

        // Assert
        verify(restaurantSpringDataRepository).findById(restaurantId);
        verify(restaurantSpringDataRepository, never()).save(any());
    }

    @Test
    void shouldHandleNullIdInDelete() {
        // Arrange
        when(restaurantSpringDataRepository.findById(null)).thenReturn(Optional.empty());

        // Act
        restaurantRepositoryImpl.delete(null);

        // Assert
        verify(restaurantSpringDataRepository).findById(null);
        verify(restaurantSpringDataRepository, never()).save(any());
    }

    // ========== UPDATE TESTS ==========

    @Test
    void shouldUpdateRestaurantSuccessfully() {
        // Arrange
        Restaurant updatedRestaurant = new Restaurant(
            restaurantId,
            "Restaurante Atualizado",
            "Japonesa",
            "10:00-23:00",
            userOwnerId,
            "Restaurante japonês moderno",
            address
        );

        RestaurantEntity updatedEntity = new RestaurantEntity();
        updatedEntity.setId(restaurantId);
        updatedEntity.setName("Restaurante Atualizado");
        updatedEntity.setCuisineType("Japonesa");
        updatedEntity.setOpeningHours("10:00-23:00");
        updatedEntity.setDescription("Restaurante japonês moderno");

        when(restaurantSpringDataRepository.save(any(RestaurantEntity.class))).thenReturn(updatedEntity);

        // Act
        Restaurant result = restaurantRepositoryImpl.update(updatedRestaurant);

        // Assert
        assertNotNull(result);
        assertEquals(restaurantId, result.getId());
        assertEquals("Restaurante Atualizado", result.getName());
        assertEquals("Japonesa", result.getCuisineType());

        verify(restaurantSpringDataRepository).save(any(RestaurantEntity.class));
    }

    @Test
    void shouldPropagateExceptionFromSpringDataRepositoryOnUpdate() {
        // Arrange
        when(restaurantSpringDataRepository.save(any(RestaurantEntity.class)))
            .thenThrow(new RuntimeException("Update failed"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> restaurantRepositoryImpl.update(restaurant));
        verify(restaurantSpringDataRepository).save(any(RestaurantEntity.class));
    }

    // ========== FIND ALL ACTIVE TESTS ==========

    @Test
    void shouldFindAllActiveRestaurantsSuccessfully() {
        // Arrange
        RestaurantEntity inactiveEntity = new RestaurantEntity();
        inactiveEntity.setId(UUID.randomUUID());
        inactiveEntity.setName("Restaurante Inativo");
        inactiveEntity.setIsActive(false);

        List<RestaurantEntity> allEntities = Arrays.asList(restaurantEntity, inactiveEntity);

        when(restaurantSpringDataRepository.findAll()).thenReturn(allEntities);

        // Act
        List<Restaurant> result = restaurantRepositoryImpl.findAllActive();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size()); // Only active restaurants
        assertEquals("Restaurante Teste", result.get(0).getName());

        verify(restaurantSpringDataRepository).findAll();
    }

    @Test
    void shouldReturnEmptyListWhenNoActiveRestaurantsFound() {
        // Arrange
        RestaurantEntity inactiveEntity = new RestaurantEntity();
        inactiveEntity.setIsActive(false);

        when(restaurantSpringDataRepository.findAll()).thenReturn(Arrays.asList(inactiveEntity));

        // Act
        List<Restaurant> result = restaurantRepositoryImpl.findAllActive();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(restaurantSpringDataRepository).findAll();
    }

    @Test
    void shouldHandleNullIsActiveInFindAllActive() {
        // Arrange
        RestaurantEntity entityWithNullActive = new RestaurantEntity();
        entityWithNullActive.setIsActive(null);

        when(restaurantSpringDataRepository.findAll()).thenReturn(Arrays.asList(entityWithNullActive));

        // Act
        List<Restaurant> result = restaurantRepositoryImpl.findAllActive();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty()); // null is not TRUE, so should be filtered out

        verify(restaurantSpringDataRepository).findAll();
    }

    // ========== INTEGRATION AND EDGE CASE TESTS ==========

    @Test
    void shouldHandleMultipleOperationsInSequence() {
        // Arrange
        when(restaurantSpringDataRepository.save(any(RestaurantEntity.class))).thenReturn(restaurantEntity);
        when(restaurantSpringDataRepository.findByIdAndIsActiveTrue(restaurantId))
            .thenReturn(Optional.of(restaurantEntity));
        when(restaurantSpringDataRepository.existsById(restaurantId)).thenReturn(true);
        when(restaurantSpringDataRepository.findById(restaurantId)).thenReturn(Optional.of(restaurantEntity));

        // Act & Assert
        // Save
        Restaurant savedRestaurant = restaurantRepositoryImpl.save(restaurant);
        assertNotNull(savedRestaurant);

        // Find by ID
        Restaurant foundRestaurant = restaurantRepositoryImpl.findById(restaurantId);
        assertNotNull(foundRestaurant);

        // Exists
        boolean exists = restaurantRepositoryImpl.existsById(restaurantId);
        assertTrue(exists);

        // Update
        Restaurant updatedRestaurant = restaurantRepositoryImpl.update(restaurant);
        assertNotNull(updatedRestaurant);

        // Soft Delete
        assertDoesNotThrow(() -> restaurantRepositoryImpl.delete(restaurantId));

        // Verify all interactions
        // save() is called 3 times: once in save(), once in update(), and once in delete() (soft delete)
        verify(restaurantSpringDataRepository, times(3)).save(any(RestaurantEntity.class));
        verify(restaurantSpringDataRepository).findByIdAndIsActiveTrue(restaurantId);
        verify(restaurantSpringDataRepository).existsById(restaurantId);
        verify(restaurantSpringDataRepository).findById(restaurantId);
    }

    @Test
    void shouldHandleRestaurantWithDifferentCuisineTypes() {
        // Test with different cuisine types

        // Arrange
        Restaurant chineseRestaurant = new Restaurant(
            UUID.randomUUID(),
            "Restaurante Chinês",
            "Chinesa",
            "11:00-22:30",
            userOwnerId,
            "Autêntica comida chinesa",
            address
        );

        RestaurantEntity chineseEntity = new RestaurantEntity();
        chineseEntity.setId(chineseRestaurant.getId());
        chineseEntity.setName("Restaurante Chinês");
        chineseEntity.setCuisineType("Chinesa");
        chineseEntity.setIsActive(true);

        when(restaurantSpringDataRepository.save(any(RestaurantEntity.class))).thenReturn(chineseEntity);

        // Act
        Restaurant result = restaurantRepositoryImpl.save(chineseRestaurant);

        // Assert
        assertNotNull(result);
        assertEquals("Restaurante Chinês", result.getName());
        assertEquals("Chinesa", result.getCuisineType());

        verify(restaurantSpringDataRepository).save(any(RestaurantEntity.class));
    }

    @Test
    void shouldHandleEmptyCollectionsCorrectly() {
        // Test behavior with empty collections

        // Arrange
        when(restaurantSpringDataRepository.findAllByUserOwnerIdAndIsActiveTrue(any(UUID.class)))
            .thenReturn(Collections.emptyList());
        when(restaurantSpringDataRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Restaurant> resultByUser = restaurantRepositoryImpl.findAllByUserId(UUID.randomUUID());
        List<Restaurant> resultAllActive = restaurantRepositoryImpl.findAllActive();

        // Assert
        assertNotNull(resultByUser);
        assertTrue(resultByUser.isEmpty());
        assertNotNull(resultAllActive);
        assertTrue(resultAllActive.isEmpty());
    }
}
