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
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetMenuItemByIdUseCaseTest {

    @Mock
    private MenuItemRepository menuItemRepository;

    @InjectMocks
    private GetMenuItemByIdUseCase getMenuItemByIdUseCase;

    private MenuItem menuItem;
    private UUID menuItemId;

    @BeforeEach
    void setUp() {
        menuItemId = UUID.randomUUID();
        menuItem = new MenuItem(
                menuItemId,
                "Test Menu Item",
                "Test Description",
                BigDecimal.valueOf(25.99),
                false,
                "photo-url.jpg",
                UUID.randomUUID(),
                null,
                null
        );
    }

    @Test
    void shouldReturnMenuItemWhenFound() {
        when(menuItemRepository.findById(menuItemId)).thenReturn(Optional.of(menuItem));

        Optional<MenuItem> result = getMenuItemByIdUseCase.execute(menuItemId);

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(menuItem);
        verify(menuItemRepository).findById(menuItemId);
    }

    @Test
    void shouldReturnEmptyWhenMenuItemNotFound() {
        when(menuItemRepository.findById(menuItemId)).thenReturn(Optional.empty());

        Optional<MenuItem> result = getMenuItemByIdUseCase.execute(menuItemId);

        assertThat(result).isEmpty();
        verify(menuItemRepository).findById(menuItemId);
    }

    @Test
    void shouldHandleNullId() {
        when(menuItemRepository.findById(null)).thenReturn(Optional.empty());

        Optional<MenuItem> result = getMenuItemByIdUseCase.execute(null);

        assertThat(result).isEmpty();
        verify(menuItemRepository).findById(null);
    }
}
