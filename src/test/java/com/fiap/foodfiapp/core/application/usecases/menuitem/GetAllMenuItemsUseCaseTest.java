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
class GetAllMenuItemsUseCaseTest {

    @Mock
    private MenuItemRepository menuItemRepository;

    @InjectMocks
    private GetAllMenuItemsUseCase getAllMenuItemsUseCase;

    private MenuItem menuItem1;
    private MenuItem menuItem2;

    @BeforeEach
    void setUp() {
        menuItem1 = new MenuItem(
                UUID.randomUUID(),
                "Item 1",
                "Description 1",
                BigDecimal.valueOf(10.99),
                false,
                "photo1.jpg",
                UUID.randomUUID(),
                null,
                null
        );
        menuItem2 = new MenuItem(
                UUID.randomUUID(),
                "Item 2",
                "Description 2",
                BigDecimal.valueOf(15.99),
                true,
                "photo2.jpg",
                UUID.randomUUID(),
                null,
                null
        );
    }

    @Test
    void shouldReturnAllMenuItems() {
        UUID restaurantId = UUID.randomUUID();
        List<MenuItem> menuItems = Arrays.asList(menuItem1, menuItem2);
        when(menuItemRepository.findAllByRestaurantId(restaurantId)).thenReturn(menuItems);

        List<MenuItem> result = getAllMenuItemsUseCase.execute(restaurantId);

        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(menuItem1, menuItem2);
        verify(menuItemRepository).findAllByRestaurantId(restaurantId);
    }

    @Test
    void shouldReturnEmptyListWhenNoMenuItems() {
        UUID restaurantId = UUID.randomUUID();
        when(menuItemRepository.findAllByRestaurantId(restaurantId)).thenReturn(Collections.emptyList());

        List<MenuItem> result = getAllMenuItemsUseCase.execute(restaurantId);

        assertThat(result).isEmpty();
        verify(menuItemRepository).findAllByRestaurantId(restaurantId);
    }
}
