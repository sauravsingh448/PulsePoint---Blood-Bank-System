package com.bloodbank.donationService.entity;
import com.bloodbank.donationService.enum1.CampStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "donation_camps")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DonationCamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // name of the donation camp
    @Column(nullable = false)
    private String campName;

    // Date of the camp
    @Column(nullable = false, length = 150)
    private LocalDate campDate;

    // camp start time
    @Column(nullable = false)
    private LocalTime startTime;
    // camp end time
    @Column(nullable = false)
    private LocalTime endTime;

    // full address of camp location
    @Column(nullable = false)
    private String address;

    // Latitude for map and nearby search
    @Column(nullable = false)
    private Double latitude;

    // Longitude for map and nearby search
    @Column(nullable = false)
    private Double longitude;

    // Blood bank ID from BloodBank
    @Column(nullable = false)
    private Long bloodBankId;

    // status of the camp
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CampStatus status;

    // One DonationCamp can have Many registrations
    @OneToMany(
            mappedBy = "camp", // field name in campRegistration
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY // load only when need
    )
    @JsonIgnore
    private List<CampRegistration> registrations;
}
