package com.fiap.foodfiapp.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "addresses",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"owner_type", "owner_id"})
        }
)
public class AddressesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "public_place")
    private String publicPlace;

    private String number;
    private String complement;
    private String neighborhood;
    private String city;
    private String state;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "owner_type", nullable = false)
    private String ownerType; // "USER" or "RESTAURANT"

    @Column(name = "owner_id", nullable = false)
    private UUID ownerId;
}
