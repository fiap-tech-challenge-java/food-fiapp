package com.fiap.foodfiapp.core.application.usecases.menuitem;

import com.fiap.foodfiapp.core.domain.entity.MenuItem;
import com.fiap.foodfiapp.core.domain.port.FileStorageRepository;
import com.fiap.foodfiapp.core.domain.port.MenuItemRepository;
import com.fiap.foodfiapp.core.domain.port.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateMenuItemUseCaseTest {

    @Mock
    private MenuItemRepository menuItemRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private FileStorageRepository fileStorageRepository;

    @Mock
    private MultipartFile photo;

    @InjectMocks
    private CreateMenuItemUseCase createMenuItemUseCase;

    private MenuItem menuItem;
    private UUID restaurantId;

    @BeforeEach
    void setUp() {
        restaurantId = UUID.randomUUID();
        menuItem = new MenuItem(
                null,
                "Test Menu Item",
                "Test Description",
                BigDecimal.valueOf(25.99),
                false,
                null,
                restaurantId,
                null,
                null
        );
    }

    @Test
    void shouldCreateMenuItemSuccessfully() throws IOException {
        when(restaurantRepository.existsById(restaurantId)).thenReturn(true);
        when(photo.isEmpty()).thenReturn(false);
        when(photo.getOriginalFilename()).thenReturn("test.jpg");
        when(fileStorageRepository.store(any(MultipartFile.class), anyString())).thenReturn("photo-url.jpg");
        when(menuItemRepository.save(any(MenuItem.class))).thenReturn(menuItem);

        MenuItem result = createMenuItemUseCase.execute(menuItem, photo);

        assertThat(result).isNotNull();
        verify(restaurantRepository).existsById(restaurantId);
        verify(fileStorageRepository).store(eq(photo), anyString());
        verify(menuItemRepository).save(any(MenuItem.class));
    }

    @Test
    void shouldCreateMenuItemWithoutPhoto() throws IOException {
        when(restaurantRepository.existsById(restaurantId)).thenReturn(true);
        when(menuItemRepository.save(any(MenuItem.class))).thenReturn(menuItem);

        MenuItem result = createMenuItemUseCase.execute(menuItem, null);

        assertThat(result).isNotNull();
        verify(restaurantRepository).existsById(restaurantId);
        verify(fileStorageRepository, never()).store(any(), anyString());
        verify(menuItemRepository).save(any(MenuItem.class));
    }

    @Test
    void shouldCreateMenuItemWithEmptyPhoto() throws IOException {
        when(restaurantRepository.existsById(restaurantId)).thenReturn(true);
        when(photo.isEmpty()).thenReturn(true);
        when(menuItemRepository.save(any(MenuItem.class))).thenReturn(menuItem);

        MenuItem result = createMenuItemUseCase.execute(menuItem, photo);

        assertThat(result).isNotNull();
        verify(restaurantRepository).existsById(restaurantId);
        verify(fileStorageRepository, never()).store(any(), anyString());
        verify(menuItemRepository).save(any(MenuItem.class));
    }

    @Test
    void shouldThrowExceptionWhenRestaurantNotFound() throws IOException {
        when(restaurantRepository.existsById(restaurantId)).thenReturn(false);

        assertThatThrownBy(() -> createMenuItemUseCase.execute(menuItem, photo))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Restaurant not found");

        verify(restaurantRepository).existsById(restaurantId);
        verify(fileStorageRepository, never()).store(any(), anyString());
        verify(menuItemRepository, never()).save(any());
    }

    @Test
    void shouldPropagateIOExceptionFromFileStorage() throws IOException {
        when(restaurantRepository.existsById(restaurantId)).thenReturn(true);
        when(photo.isEmpty()).thenReturn(false);
        when(photo.getOriginalFilename()).thenReturn("test.jpg");
        when(fileStorageRepository.store(any(MultipartFile.class), anyString()))
                .thenThrow(new IOException("File storage error"));

        assertThatThrownBy(() -> {
            try {
                createMenuItemUseCase.execute(menuItem, photo);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        })
                .isInstanceOf(RuntimeException.class)
                .hasCauseInstanceOf(IOException.class);

        verify(restaurantRepository).existsById(restaurantId);
        verify(fileStorageRepository).store(eq(photo), anyString());
        verify(menuItemRepository, never()).save(any());
    }
}
