package com.fiap.foodfiapp.integration;

import com.fiap.foodfiapp.core.application.usecases.user.CreateUserUseCase;
import com.fiap.foodfiapp.core.application.usecases.user.impl.CreateUserUseCaseImpl;
import com.fiap.foodfiapp.core.domain.entity.Addresses;
import com.fiap.foodfiapp.core.domain.entity.User;
import com.fiap.foodfiapp.core.domain.entity.UserType;
import com.fiap.foodfiapp.core.domain.port.AddressRepository;
import com.fiap.foodfiapp.core.domain.port.UserRepository;
import com.fiap.foodfiapp.core.domain.port.UserTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateUserWithAddressesIntegrationTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserTypeRepository userTypeRepository;

    @Mock
    private AddressRepository addressRepository;

    private CreateUserUseCase createUserUseCase;

    private User user;
    private UserType userType;
    private Addresses addresses;
    private UUID userTypeUuid;
    private UUID userId;
    private UUID addressId;

    @BeforeEach
    void setUp() {
        createUserUseCase = new CreateUserUseCaseImpl(userRepository, userTypeRepository, addressRepository);

        userTypeUuid = UUID.randomUUID();
        userId = UUID.randomUUID();
        addressId = UUID.randomUUID();

        userType = new UserType();
        userType.setUuid(userTypeUuid);
        userType.setName("CLIENT");

        addresses = new Addresses();
        addresses.setId(addressId);
        addresses.setPublicPlace("Rua Teste");
        addresses.setNumber("123");
        addresses.setNeighborhood("Centro");
        addresses.setCity("São Paulo");
        addresses.setState("SP");
        addresses.setPostalCode("01001-000");
        addresses.setIsActive(true);

        user = new User();
        user.setId(userId);
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setCpf("52998224725"); // Valid CPF
        user.setLogin("testuser");
        user.setPassword("password123");
        user.setUserType(userType);
        user.setAddress(List.of(addresses));
        user.setIsActive(true);
        user.setCreatedAt(OffsetDateTime.now());
        user.setUpdatedAt(OffsetDateTime.now());
    }

    @Test
    void shouldCreateUserAndReturnAddressesInResponse() {
        // Given
        User savedUser = new User();
        savedUser.setId(userId);
        savedUser.setName(user.getName());
        savedUser.setEmail(user.getEmail());
        savedUser.setCpf(user.getCpf());
        savedUser.setLogin(user.getLogin());
        savedUser.setPassword(user.getPassword());
        savedUser.setUserType(userType);
        savedUser.setIsActive(true);
        savedUser.setCreatedAt(OffsetDateTime.now());
        savedUser.setUpdatedAt(OffsetDateTime.now());

        Addresses savedAddresses = new Addresses();
        savedAddresses.setId(addressId);
        savedAddresses.setPublicPlace(addresses.getPublicPlace());
        savedAddresses.setNumber(addresses.getNumber());
        savedAddresses.setNeighborhood(addresses.getNeighborhood());
        savedAddresses.setCity(addresses.getCity());
        savedAddresses.setState(addresses.getState());
        savedAddresses.setPostalCode(addresses.getPostalCode());
        savedAddresses.setIsActive(true);
        savedAddresses.setCreatedAt(OffsetDateTime.now());
        savedAddresses.setUpdatedAt(OffsetDateTime.now());

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(userRepository.findByCpf(user.getCpf())).thenReturn(Optional.empty());
        when(userRepository.findByLogin(user.getLogin())).thenReturn(Optional.empty());
        when(userTypeRepository.findById(userTypeUuid)).thenReturn(Optional.of(userType));
        when(userRepository.save(user)).thenReturn(savedUser);
        when(addressRepository.save(any(Addresses.class), eq(userId), eq("USER")))
                .thenReturn(savedAddresses);

        // When
        User result = createUserUseCase.execute(user);

        // Then
        assertNotNull(result, "User should not be null");
        assertEquals(user.getName(), result.getName());
        assertEquals(user.getEmail(), result.getEmail());

        // Verifica se os endereços estão presentes na resposta
        assertNotNull(result.getAddress(), "Addresses should not be null");
        assertFalse(result.getAddress().isEmpty(), "Addresses should not be empty");
        assertEquals(1, result.getAddress().size(), "Should have exactly 1 address");

        Addresses returnedAddresses = result.getAddress().getFirst();
        assertEquals(addressId, returnedAddresses.getId());
        assertEquals("Rua Teste", returnedAddresses.getPublicPlace());
        assertEquals("123", returnedAddresses.getNumber());
        assertEquals("Centro", returnedAddresses.getNeighborhood());
        assertEquals("São Paulo", returnedAddresses.getCity());
        assertEquals("SP", returnedAddresses.getState());
        assertEquals("01001-000", returnedAddresses.getPostalCode());
        assertTrue(returnedAddresses.getIsActive());
    }

    @Test
    void shouldCreateUserWithMultipleAddressesAndReturnAllInResponse() {
        // Given
        Addresses secondAddresses = new Addresses();
        secondAddresses.setId(UUID.randomUUID());
        secondAddresses.setPublicPlace("Avenida Paulista");
        secondAddresses.setNumber("1000");
        secondAddresses.setNeighborhood("Bela Vista");
        secondAddresses.setCity("São Paulo");
        secondAddresses.setState("SP");
        secondAddresses.setPostalCode("01310-100");
        secondAddresses.setIsActive(true);

        user.setAddress(List.of(addresses, secondAddresses));

        User savedUser = new User();
        savedUser.setId(userId);
        savedUser.setName(user.getName());
        savedUser.setEmail(user.getEmail());
        savedUser.setCpf(user.getCpf());
        savedUser.setLogin(user.getLogin());
        savedUser.setPassword(user.getPassword());
        savedUser.setUserType(userType);
        savedUser.setIsActive(true);
        savedUser.setCreatedAt(OffsetDateTime.now());
        savedUser.setUpdatedAt(OffsetDateTime.now());

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(userRepository.findByCpf(user.getCpf())).thenReturn(Optional.empty());
        when(userRepository.findByLogin(user.getLogin())).thenReturn(Optional.empty());
        when(userTypeRepository.findById(userTypeUuid)).thenReturn(Optional.of(userType));
        when(userRepository.save(user)).thenReturn(savedUser);
        when(addressRepository.save(any(Addresses.class), eq(userId), eq("USER")))
                .thenReturn(addresses, secondAddresses);

        // When
        User result = createUserUseCase.execute(user);

        // Then
        assertNotNull(result, "User should not be null");
        assertNotNull(result.getAddress(), "Addresses should not be null");
        assertEquals(2, result.getAddress().size(), "Should have exactly 2 addresses");

        // Verifica se ambos os endereços estão na resposta
        List<String> returnedStreets = result.getAddress().stream()
                .map(Addresses::getPublicPlace)
                .toList();

        assertTrue(returnedStreets.contains("Rua Teste"));
        assertTrue(returnedStreets.contains("Avenida Paulista"));
    }
}
