package com.fiap.foodfiapp.core.application.usecases.menuitem;

import com.fiap.foodfiapp.core.domain.entity.MenuItem;
import com.fiap.foodfiapp.core.domain.port.FileStorageRepository;
import com.fiap.foodfiapp.core.domain.port.MenuItemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteMenuItemUseCaseTest {

    @Mock
    private MenuItemRepository menuItemRepository;

    @Mock
    private FileStorageRepository fileStorageRepository;

    @InjectMocks
    private DeleteMenuItemUseCase deleteMenuItemUseCase;

    @Test
    void shouldDeleteMenuItemById() throws IOException {
        UUID menuItemId = UUID.randomUUID();
        MenuItem menuItem = new MenuItem(
                menuItemId,
                "Test Item",
                "Description",
                BigDecimal.valueOf(10.0),
                false,
                null,
                UUID.randomUUID(),
                null,
                null
        );

        when(menuItemRepository.findById(menuItemId)).thenReturn(Optional.of(menuItem));

        deleteMenuItemUseCase.execute(menuItemId);

        verify(menuItemRepository).findById(menuItemId);
        verify(menuItemRepository).deleteById(menuItemId);
        verify(fileStorageRepository, never()).delete(anyString());
    }

    @Test
    void shouldThrowExceptionWhenMenuItemNotFound() {
        UUID menuItemId = UUID.randomUUID();

        when(menuItemRepository.findById(menuItemId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> deleteMenuItemUseCase.execute(menuItemId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Menu item not found");

        verify(menuItemRepository).findById(menuItemId);
        verify(menuItemRepository, never()).deleteById(menuItemId);
    }
}
