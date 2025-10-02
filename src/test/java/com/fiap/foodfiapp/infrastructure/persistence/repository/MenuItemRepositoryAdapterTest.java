package com.fiap.foodfiapp.infrastructure.persistence.repository;

import com.fiap.foodfiapp.core.domain.entity.MenuItem;
import com.fiap.foodfiapp.infrastructure.persistence.entity.MenuItemEntity;
import com.fiap.foodfiapp.infrastructure.persistence.entity.RestaurantEntity;
import com.fiap.foodfiapp.infrastructure.persistence.mapper.MenuItemEntityMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MenuItemRepositoryAdapterTest {

    @Mock
    private MenuItemJpaRepository menuItemJpaRepository;

    @Mock
    private MenuItemEntityMapper menuItemEntityMapper;

    @InjectMocks
    private MenuItemRepositoryAdapter menuItemRepositoryAdapter;

    private MenuItem menuItem;
    private MenuItemEntity menuItemEntity;
    private UUID menuItemId;
    private UUID restaurantId;

    @BeforeEach
    void setUp() {
        menuItemId = UUID.randomUUID();
        restaurantId = UUID.randomUUID();

        menuItem = new MenuItem(
                menuItemId,
                "Test Menu Item",
                "Test Description",
                BigDecimal.valueOf(29.99),
                false,
                "photo-url.jpg",
                restaurantId,
                null,
                null
        );

        RestaurantEntity restaurantEntity = new RestaurantEntity();
        restaurantEntity.setId(restaurantId);

        menuItemEntity = new MenuItemEntity();
        menuItemEntity.setId(menuItemId);
        menuItemEntity.setName("Test Menu Item");
        menuItemEntity.setDescription("Test Description");
        menuItemEntity.setPrice(BigDecimal.valueOf(29.99));
        menuItemEntity.setAvailableForInStoreOnly(false);
        menuItemEntity.setPhotoUrl("photo-url.jpg");
        menuItemEntity.setRestaurant(restaurantEntity);
    }

    @Test
    void shouldSaveMenuItemSuccessfully() {
        when(menuItemEntityMapper.toEntity(menuItem)).thenReturn(menuItemEntity);
        when(menuItemJpaRepository.save(menuItemEntity)).thenReturn(menuItemEntity);
        when(menuItemEntityMapper.toDomain(menuItemEntity)).thenReturn(menuItem);

        MenuItem result = menuItemRepositoryAdapter.save(menuItem);

        assertThat(result).isEqualTo(menuItem);
        verify(menuItemEntityMapper).toEntity(menuItem);
        verify(menuItemJpaRepository).save(menuItemEntity);
        verify(menuItemEntityMapper).toDomain(menuItemEntity);
    }

    @Test
    void shouldFindMenuItemByIdSuccessfully() {
        when(menuItemJpaRepository.findById(menuItemId)).thenReturn(Optional.of(menuItemEntity));
        when(menuItemEntityMapper.toDomain(menuItemEntity)).thenReturn(menuItem);

        Optional<MenuItem> result = menuItemRepositoryAdapter.findById(menuItemId);

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(menuItem);
        verify(menuItemJpaRepository).findById(menuItemId);
        verify(menuItemEntityMapper).toDomain(menuItemEntity);
    }

    @Test
    void shouldReturnEmptyWhenMenuItemNotFoundById() {
        when(menuItemJpaRepository.findById(menuItemId)).thenReturn(Optional.empty());

        Optional<MenuItem> result = menuItemRepositoryAdapter.findById(menuItemId);

        assertThat(result).isEmpty();
        verify(menuItemJpaRepository).findById(menuItemId);
        verify(menuItemEntityMapper, never()).toDomain(any());
    }

    @Test
    void shouldFindAllMenuItemsByRestaurantIdSuccessfully() {
        List<MenuItemEntity> entities = Arrays.asList(menuItemEntity);
        when(menuItemJpaRepository.findAllByRestaurantId(restaurantId)).thenReturn(entities);
        when(menuItemEntityMapper.toDomain(menuItemEntity)).thenReturn(menuItem);

        List<MenuItem> result = menuItemRepositoryAdapter.findAllByRestaurantId(restaurantId);

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(menuItem);
        verify(menuItemJpaRepository).findAllByRestaurantId(restaurantId);
        verify(menuItemEntityMapper).toDomain(menuItemEntity);
    }

    @Test
    void shouldReturnEmptyListWhenNoMenuItemsFoundByRestaurantId() {
        when(menuItemJpaRepository.findAllByRestaurantId(restaurantId)).thenReturn(Arrays.asList());

        List<MenuItem> result = menuItemRepositoryAdapter.findAllByRestaurantId(restaurantId);

        assertThat(result).isEmpty();
        verify(menuItemJpaRepository).findAllByRestaurantId(restaurantId);
        verify(menuItemEntityMapper, never()).toDomain(any());
    }

    @Test
    void shouldDeleteMenuItemByIdSuccessfully() {
        doNothing().when(menuItemJpaRepository).deleteById(menuItemId);

        menuItemRepositoryAdapter.deleteById(menuItemId);

        verify(menuItemJpaRepository).deleteById(menuItemId);
    }

    @Test
    void shouldCheckIfMenuItemExistsById() {
        when(menuItemJpaRepository.existsById(menuItemId)).thenReturn(true);

        boolean result = menuItemRepositoryAdapter.existsById(menuItemId);

        assertThat(result).isTrue();
        verify(menuItemJpaRepository).existsById(menuItemId);
    }

    @Test
    void shouldReturnFalseWhenMenuItemDoesNotExist() {
        when(menuItemJpaRepository.existsById(menuItemId)).thenReturn(false);

        boolean result = menuItemRepositoryAdapter.existsById(menuItemId);

        assertThat(result).isFalse();
        verify(menuItemJpaRepository).existsById(menuItemId);
    }
}
