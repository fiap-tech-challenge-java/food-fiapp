package com.fiap.foodfiapp.infrastructure.rest.mapper;

import com.fiap.foodfiapp.core.domain.entity.Addresses;
import com.fiap.foodfiapp.core.domain.enums.AddressOwnerTypeEnum;
import com.fiap.foodfiapp.model.AddressesResponse;
import com.fiap.foodfiapp.model.CreateAddressesRequest;
import com.fiap.foodfiapp.model.UpdateAddressesRequest;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class AddressesMapperTest {

    private final AddressesMapper mapper = AddressesMapper.INSTANCE;

    @Test
    void shouldMapCreateAddressesRequestToAddress() {
        // Given
        CreateAddressesRequest request = new CreateAddressesRequest();
        request.setPublicPlace("Rua Teste");
        request.setNumber("123");
        request.setNeighborhood("Centro");
        request.setCity("São Paulo");
        request.setState("SP");
        request.setPostalCode("01001-000");
        request.setComplement("Apto 10");

        // When
        Addresses result = mapper.toAddress(request);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getPublicPlace()).isEqualTo("Rua Teste");
        assertThat(result.getNumber()).isEqualTo("123");
        assertThat(result.getNeighborhood()).isEqualTo("Centro");
        assertThat(result.getCity()).isEqualTo("São Paulo");
        assertThat(result.getState()).isEqualTo("SP");
        assertThat(result.getPostalCode()).isEqualTo("01001-000");
        assertThat(result.getComplement()).isEqualTo("Apto 10");
    }

    @Test
    void shouldMapUpdateAddressesRequestToAddress() {
        // Given
        UpdateAddressesRequest request = new UpdateAddressesRequest();
        request.setPublicPlace("Av Paulista");
        request.setNumber("1000");
        request.setNeighborhood("Bela Vista");
        request.setCity("São Paulo");
        request.setState("SP");
        request.setPostalCode("01310-100");

        // When
        Addresses result = mapper.toAddress(request);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getPublicPlace()).isEqualTo("Av Paulista");
        assertThat(result.getNumber()).isEqualTo("1000");
        assertThat(result.getNeighborhood()).isEqualTo("Bela Vista");
    }

    @Test
    void shouldMapAddressToAddressesResponse() {
        // Given
        Addresses address = new Addresses();
        address.setId(UUID.randomUUID());
        address.setPublicPlace("Rua Teste");
        address.setNumber("123");
        address.setNeighborhood("Centro");
        address.setCity("São Paulo");
        address.setState("SP");
        address.setPostalCode("01001-000");
        address.setComplement("Apto 10");
        address.setIsActive(true);
        address.setCreatedAt(OffsetDateTime.now());
        address.setUpdatedAt(OffsetDateTime.now());

        // When
        AddressesResponse result = mapper.toAddressesResponse(address);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(address.getId());
        assertThat(result.getPublicPlace()).isEqualTo("Rua Teste");
        assertThat(result.getNumber()).isEqualTo("123");
        assertThat(result.getNeighborhood()).isEqualTo("Centro");
        assertThat(result.getCity()).isEqualTo("São Paulo");
        assertThat(result.getState()).isEqualTo("SP");
        assertThat(result.getPostalCode()).isEqualTo("01001-000");
    }
}
