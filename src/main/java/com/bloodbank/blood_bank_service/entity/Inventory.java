package com.bloodbank.blood_bank_service.entity;

import com.bloodbank.blood_bank_service.enums.BloodGroup;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "inventory")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // primary key

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BloodGroup bloodGroup; // A+, o-

    @Column(nullable = false)
    private Integer quantity; // for number of units

    @Column(nullable = false)
    private LocalDate expiryDate; // Expiry date of blood
    @Column(nullable = false)
    private String bloodComponent;
    /*
       Many inventory -> one blood bank
       FetchType.LAZY = Inventory list can be large, Load only when needed
    */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blood_bank_id") // FK reference to BloodBank
    @JsonIgnore  // Prevent infinite recursion in JSON response
    private BloodBank bloodBank;
}
