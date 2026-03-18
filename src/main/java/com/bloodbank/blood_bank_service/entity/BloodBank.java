package com.bloodbank.blood_bank_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "blood_banks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BloodBank {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String contact;
    private String address;
    private String city;

    // Location (for search)
    private Double latitude;
    private Double longitude;

    // ADMIN userId from User Service
    private Long adminUserId;

    // One blood bank -> many inventory records
    @OneToMany(
            mappedBy = "bloodBank",
            cascade = CascadeType.ALL,  // auto saved delete update
            orphanRemoval = true
    )
    private List<Inventory> inventories;
}
