package com.fiap.foodfiapp.integration;

import com.fiap.foodfiapp.core.application.usecases.user.CreateUserUseCase;
import com.fiap.foodfiapp.core.application.usecases.user.impl.CreateUserUseCaseImpl;
import com.fiap.foodfiapp.core.domain.entity.Address;
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
class CreateUserWithAddressIntegrationTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserTypeRepository userTypeRepository;

    @Mock
    private AddressRepository addressRepository;

    private CreateUserUseCase createUserUseCase;

    private User user;
    private UserType userType;
    private Address address;
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

        address = new Address();
        address.setId(addressId);
        address.setPublicPlace("Rua Teste");
        address.setNumber("123");
        address.setNeighborhood("Centro");
        address.setCity("São Paulo");
        address.setState("SP");
        address.setPostalCode("01001-000");
        address.setIsActive(true);

        user = new User();
        user.setId(userId);
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setCpf("12345678901");
        user.setLogin("testuser");
        user.setPassword("password123");
        user.setUserType(userType);
        user.setAddress(List.of(address));
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

        Address savedAddress = new Address();
        savedAddress.setId(addressId);
        savedAddress.setPublicPlace(address.getPublicPlace());
        savedAddress.setNumber(address.getNumber());
        savedAddress.setNeighborhood(address.getNeighborhood());
        savedAddress.setCity(address.getCity());
        savedAddress.setState(address.getState());
        savedAddress.setPostalCode(address.getPostalCode());
        savedAddress.setIsActive(true);
        savedAddress.setCreatedAt(OffsetDateTime.now());
        savedAddress.setUpdatedAt(OffsetDateTime.now());

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(userRepository.findByCpf(user.getCpf())).thenReturn(Optional.empty());
        when(userRepository.findByLogin(user.getLogin())).thenReturn(Optional.empty());
        when(userTypeRepository.findById(userTypeUuid)).thenReturn(Optional.of(userType));
        when(userRepository.save(user)).thenReturn(savedUser);
        when(addressRepository.save(any(Address.class), eq(userId), eq("USER")))
                .thenReturn(savedAddress);

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

        Address returnedAddress = result.getAddress().get(0);
        assertEquals(addressId, returnedAddress.getId());
        assertEquals("Rua Teste", returnedAddress.getPublicPlace());
        assertEquals("123", returnedAddress.getNumber());
        assertEquals("Centro", returnedAddress.getNeighborhood());
        assertEquals("São Paulo", returnedAddress.getCity());
        assertEquals("SP", returnedAddress.getState());
        assertEquals("01001-000", returnedAddress.getPostalCode());
        assertTrue(returnedAddress.getIsActive());
    }

    @Test
    void shouldCreateUserWithMultipleAddressesAndReturnAllInResponse() {
        // Given
        Address secondAddress = new Address();
        secondAddress.setId(UUID.randomUUID());
        secondAddress.setPublicPlace("Avenida Paulista");
        secondAddress.setNumber("1000");
        secondAddress.setNeighborhood("Bela Vista");
        secondAddress.setCity("São Paulo");
        secondAddress.setState("SP");
        secondAddress.setPostalCode("01310-100");
        secondAddress.setIsActive(true);

        user.setAddress(List.of(address, secondAddress));

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
        when(addressRepository.save(any(Address.class), eq(userId), eq("USER")))
                .thenReturn(address, secondAddress);

        // When
        User result = createUserUseCase.execute(user);

        // Then
        assertNotNull(result, "User should not be null");
        assertNotNull(result.getAddress(), "Addresses should not be null");
        assertEquals(2, result.getAddress().size(), "Should have exactly 2 addresses");

        // Verifica se ambos os endereços estão na resposta
        List<String> returnedStreets = result.getAddress().stream()
                .map(Address::getPublicPlace)
                .toList();

        assertTrue(returnedStreets.contains("Rua Teste"));
        assertTrue(returnedStreets.contains("Avenida Paulista"));
    }
}
