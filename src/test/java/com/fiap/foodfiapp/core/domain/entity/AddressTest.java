package com.fiap.foodfiapp.core.domain.entity;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class AddressTest {

    @Test
    void shouldCreateAddressWithAllFields() {
        UUID id = UUID.randomUUID();

        Address address = new Address(
                id,
                "Rua das Flores, 123",
                "45",
                "Apt 202",
                "Centro",
                "São Paulo",
                "SP",
                "01234-567"
        );

        assertThat(address.getId()).isEqualTo(id);
        assertThat(address.getPublicPlace()).isEqualTo("Rua das Flores, 123");
        assertThat(address.getNumber()).isEqualTo("45");
        assertThat(address.getComplement()).isEqualTo("Apt 202");
        assertThat(address.getNeighborhood()).isEqualTo("Centro");
        assertThat(address.getCity()).isEqualTo("São Paulo");
        assertThat(address.getState()).isEqualTo("SP");
        assertThat(address.getPostalCode()).isEqualTo("01234-567");
    }

    @Test
    void shouldCreateAddressWithoutComplement() {
        UUID id = UUID.randomUUID();

        Address address = new Address(
                id,
                "Avenida Paulista",
                "1000",
                null,
                "Bela Vista",
                "São Paulo",
                "SP",
                "01310-100"
        );

        assertThat(address.getComplement()).isNull();
        assertThat(address.getPublicPlace()).isEqualTo("Avenida Paulista");
    }

    @Test
    void shouldSetAndGetAllFields() {
        Address address = new Address(null, null, null, null, null, null, null, null);
        UUID newId = UUID.randomUUID();

        address.setId(newId);
        address.setPublicPlace("Nova Rua");
        address.setNumber("999");
        address.setComplement("Casa");
        address.setNeighborhood("Novo Bairro");
        address.setCity("Rio de Janeiro");
        address.setState("RJ");
        address.setPostalCode("20000-000");

        assertThat(address.getId()).isEqualTo(newId);
        assertThat(address.getPublicPlace()).isEqualTo("Nova Rua");
        assertThat(address.getNumber()).isEqualTo("999");
        assertThat(address.getComplement()).isEqualTo("Casa");
        assertThat(address.getNeighborhood()).isEqualTo("Novo Bairro");
        assertThat(address.getCity()).isEqualTo("Rio de Janeiro");
        assertThat(address.getState()).isEqualTo("RJ");
        assertThat(address.getPostalCode()).isEqualTo("20000-000");
    }

    @Test
    void shouldHandleNullValues() {
        Address address = new Address(null, null, null, null, null, null, null, null);

        assertThat(address.getId()).isNull();
        assertThat(address.getPublicPlace()).isNull();
        assertThat(address.getNumber()).isNull();
        assertThat(address.getComplement()).isNull();
        assertThat(address.getNeighborhood()).isNull();
        assertThat(address.getCity()).isNull();
        assertThat(address.getState()).isNull();
        assertThat(address.getPostalCode()).isNull();
    }

    @Test
    void shouldCreateWithMinimalRequiredFields() {
        UUID id = UUID.randomUUID();

        Address address = new Address(
                id,
                "Rua Principal",
                "100",
                "",
                "Vila Nova",
                "Brasília",
                "DF",
                "70000-000"
        );

        assertThat(address.getId()).isEqualTo(id);
        assertThat(address.getPublicPlace()).isEqualTo("Rua Principal");
        assertThat(address.getNumber()).isEqualTo("100");
        assertThat(address.getComplement()).isEmpty();
        assertThat(address.getNeighborhood()).isEqualTo("Vila Nova");
        assertThat(address.getCity()).isEqualTo("Brasília");
        assertThat(address.getState()).isEqualTo("DF");
        assertThat(address.getPostalCode()).isEqualTo("70000-000");
    }
}
