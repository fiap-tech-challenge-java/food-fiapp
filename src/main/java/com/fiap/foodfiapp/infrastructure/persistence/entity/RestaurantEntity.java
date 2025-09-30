package com.fiap.foodfiapp.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "restaurants")
@Getter
@Setter
@NoArgsConstructor
public class RestaurantEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    private String cuisineType;

    private String openingHours;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id", unique = true)
    private AddressesEntity address;

    private Boolean active;

    public RestaurantEntity(String name, String cuisineType, String openingHours, UUID userId, AddressesEntity address, Boolean active) {
        this.name = name;
        this.cuisineType = cuisineType;
        this.openingHours = openingHours;
        this.userId = userId;
        this.address = address;
        this.active = active;
    }
}
