package com.fiap.foodfiapp.core.application.usecases.menuitem;

import com.fiap.foodfiapp.core.domain.entity.MenuItem;
import com.fiap.foodfiapp.core.domain.port.FileStorageRepository;
import com.fiap.foodfiapp.core.domain.port.MenuItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateMenuItemUseCaseTest {

    @Mock
    private MenuItemRepository menuItemRepository;

    @Mock
    private FileStorageRepository fileStorageRepository;

    @Mock
    private MultipartFile photo;

    @InjectMocks
    private UpdateMenuItemUseCase updateMenuItemUseCase;

    private MenuItem existingMenuItem;
    private MenuItem updatedMenuItem;
    private UUID menuItemId;

    @BeforeEach
    void setUp() {
        menuItemId = UUID.randomUUID();
        existingMenuItem = new MenuItem(
                menuItemId,
                "Original Item",
                "Original Description",
                BigDecimal.valueOf(20.00),
                false,
                "original-photo.jpg",
                UUID.randomUUID(),
                null,
                null
        );
        updatedMenuItem = new MenuItem(
                menuItemId,
                "Updated Item",
                "Updated Description",
                BigDecimal.valueOf(25.99),
                true,
                null,
                existingMenuItem.restaurantId(),
                null,
                null
        );
    }

    @Test
    void shouldUpdateMenuItemWithPhoto() throws IOException {
        when(menuItemRepository.findById(menuItemId)).thenReturn(Optional.of(existingMenuItem));
        when(photo.isEmpty()).thenReturn(false);
        when(photo.getOriginalFilename()).thenReturn("new-photo.jpg");
        when(fileStorageRepository.store(any(MultipartFile.class), anyString())).thenReturn("new-photo-url.jpg");

        MenuItem updatedWithPhoto = updatedMenuItem.withPhotoUrl("new-photo-url.jpg");
        when(menuItemRepository.save(any(MenuItem.class))).thenReturn(updatedWithPhoto);

        MenuItem result = updateMenuItemUseCase.execute(menuItemId, updatedMenuItem, photo);

        assertThat(result.photoUrl()).isEqualTo("new-photo-url.jpg");
        verify(menuItemRepository).findById(menuItemId);
        verify(fileStorageRepository).store(eq(photo), anyString());
        verify(menuItemRepository).save(any(MenuItem.class));
    }

    @Test
    void shouldUpdateMenuItemWithoutPhoto() throws IOException {
        when(menuItemRepository.findById(menuItemId)).thenReturn(Optional.of(existingMenuItem));
        when(menuItemRepository.save(any(MenuItem.class))).thenReturn(updatedMenuItem);

        MenuItem result = updateMenuItemUseCase.execute(menuItemId, updatedMenuItem, null);

        assertThat(result).isEqualTo(updatedMenuItem);
        verify(menuItemRepository).findById(menuItemId);
        verify(fileStorageRepository, never()).store(any(), anyString());
        verify(menuItemRepository).save(any(MenuItem.class));
    }

    @Test
    void shouldUpdateMenuItemWithEmptyPhoto() throws IOException {
        when(menuItemRepository.findById(menuItemId)).thenReturn(Optional.of(existingMenuItem));
        when(photo.isEmpty()).thenReturn(true);
        when(menuItemRepository.save(any(MenuItem.class))).thenReturn(updatedMenuItem);

        MenuItem result = updateMenuItemUseCase.execute(menuItemId, updatedMenuItem, photo);

        assertThat(result).isEqualTo(updatedMenuItem);
        verify(menuItemRepository).findById(menuItemId);
        verify(fileStorageRepository, never()).store(any(), anyString());
        verify(menuItemRepository).save(any(MenuItem.class));
    }

    @Test
    void shouldThrowExceptionWhenMenuItemNotFound() throws IOException {
        when(menuItemRepository.findById(menuItemId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> updateMenuItemUseCase.execute(menuItemId, updatedMenuItem, photo))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Menu item not found");

        verify(menuItemRepository).findById(menuItemId);
        verify(fileStorageRepository, never()).store(any(), anyString());
        verify(menuItemRepository, never()).save(any());
    }

    @Test
    void shouldPropagateIOExceptionFromFileStorage() throws IOException {
        when(menuItemRepository.findById(menuItemId)).thenReturn(Optional.of(existingMenuItem));
        when(photo.isEmpty()).thenReturn(false);
        when(photo.getOriginalFilename()).thenReturn("new-photo.jpg");
        when(fileStorageRepository.store(any(MultipartFile.class), anyString()))
                .thenThrow(new IOException("File storage error"));

        assertThatThrownBy(() -> updateMenuItemUseCase.execute(menuItemId, updatedMenuItem, photo))
                .isInstanceOf(IOException.class)
                .hasMessage("File storage error");

        verify(menuItemRepository).findById(menuItemId);
        verify(fileStorageRepository).store(eq(photo), anyString());
        verify(menuItemRepository, never()).save(any());
    }
}
