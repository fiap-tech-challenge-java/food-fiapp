package com.fiap.foodfiapp.model;

import org.junit.jupiter.api.Test;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class AddressResponseTest {

    @Test
    void shouldCreateAddressResponseWithAllFields() {
        UUID id = UUID.randomUUID();
        AddressResponse response = new AddressResponse()
                .id(id)
                .publicPlace("Main Street 123")
                .complement("Apt 45")
                .neighborhood("Downtown")
                .city("New York")
                .state("NY")
                .postalCode("10001");

        assertThat(response.getId()).isEqualTo(id);
        assertThat(response.getPublicPlace()).isEqualTo("Main Street 123");
        assertThat(response.getComplement()).isEqualTo("Apt 45");
        assertThat(response.getNeighborhood()).isEqualTo("Downtown");
        assertThat(response.getCity()).isEqualTo("New York");
        assertThat(response.getState()).isEqualTo("NY");
        assertThat(response.getPostalCode()).isEqualTo("10001");
    }

    @Test
    void shouldHandleNullValues() {
        AddressResponse response = new AddressResponse();

        assertThat(response.getId()).isNull();
        assertThat(response.getPublicPlace()).isNull();
        assertThat(response.getComplement()).isNull();
        assertThat(response.getNeighborhood()).isNull();
        assertThat(response.getCity()).isNull();
        assertThat(response.getState()).isNull();
        assertThat(response.getPostalCode()).isNull();
    }

    @Test
    void shouldSupportFluentInterface() {
        AddressResponse response = new AddressResponse()
                .publicPlace("Broadway 456")
                .city("Los Angeles")
                .state("CA");

        assertThat(response.getPublicPlace()).isEqualTo("Broadway 456");
        assertThat(response.getCity()).isEqualTo("Los Angeles");
        assertThat(response.getState()).isEqualTo("CA");
    }

    @Test
    void shouldImplementEqualsAndHashCode() {
        UUID id = UUID.randomUUID();
        AddressResponse response1 = new AddressResponse().id(id).publicPlace("Street 1");
        AddressResponse response2 = new AddressResponse().id(id).publicPlace("Street 1");

        assertThat(response1).isEqualTo(response2);
        assertThat(response1.hashCode()).isEqualTo(response2.hashCode());
    }

    @Test
    void shouldImplementToString() {
        AddressResponse response = new AddressResponse().publicPlace("Test Street");

        assertThat(response.toString()).contains("Test Street");
    }
}
