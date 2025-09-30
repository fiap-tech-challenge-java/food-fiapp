package com.fiap.foodfiapp.core.application.usecases.menuitem;

import com.fiap.foodfiapp.core.domain.entity.MenuItem;
import com.fiap.foodfiapp.core.domain.port.MenuItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetMenuItemsByRestaurantUseCaseTest {

    @Mock
    private MenuItemRepository menuItemRepository;

    @InjectMocks
    private GetMenuItemsByRestaurantUseCase getMenuItemsByRestaurantUseCase;

    private MenuItem menuItem1;
    private MenuItem menuItem2;
    private UUID restaurantId;

    @BeforeEach
    void setUp() {
        restaurantId = UUID.randomUUID();
        menuItem1 = new MenuItem(
                UUID.randomUUID(),
                "Menu Item 1",
                "Description 1",
                BigDecimal.valueOf(25.99),
                false,
                "photo1.jpg",
                restaurantId,
                null,
                null
        );
        menuItem2 = new MenuItem(
                UUID.randomUUID(),
                "Menu Item 2",
                "Description 2",
                BigDecimal.valueOf(35.99),
                true,
                "photo2.jpg",
                restaurantId,
                null,
                null
        );
    }

    @Test
    void shouldReturnMenuItemsForRestaurant() {
        List<MenuItem> menuItems = Arrays.asList(menuItem1, menuItem2);
        when(menuItemRepository.findAllByRestaurantId(restaurantId)).thenReturn(menuItems);

        List<MenuItem> result = getMenuItemsByRestaurantUseCase.execute(restaurantId);

        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(menuItem1, menuItem2);
        verify(menuItemRepository).findAllByRestaurantId(restaurantId);
    }

    @Test
    void shouldReturnEmptyListWhenNoMenuItems() {
        when(menuItemRepository.findAllByRestaurantId(restaurantId)).thenReturn(Collections.emptyList());

        List<MenuItem> result = getMenuItemsByRestaurantUseCase.execute(restaurantId);

        assertThat(result).isEmpty();
        verify(menuItemRepository).findAllByRestaurantId(restaurantId);
    }

    @Test
    void shouldHandleNullRestaurantId() {
        when(menuItemRepository.findAllByRestaurantId(null)).thenReturn(Collections.emptyList());

        List<MenuItem> result = getMenuItemsByRestaurantUseCase.execute(null);

        assertThat(result).isEmpty();
        verify(menuItemRepository).findAllByRestaurantId(null);
    }
}
