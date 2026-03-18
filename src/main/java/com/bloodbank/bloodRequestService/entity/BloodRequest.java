package com.bloodbank.bloodRequestService.entity;

import com.bloodbank.bloodRequestService.enums.RequestStatus;
import com.bloodbank.blood_bank_service.entity.BloodBank;
import com.bloodbank.blood_bank_service.enums.BloodGroup;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "blood_requests")
public class BloodRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Patient name who needs blood
    private String patientName;

    // Number of units required
    private Integer unitsRequired;

    // Hospital name where patient admitted
    private String hospitalName;

    // City of hospital
    private String city;

    // patient location
    @Column(nullable = false)
    private Double latitude;
    @Column(nullable = false)
    private Double longitude;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blood_bank_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
    private BloodBank bloodBank;

    @Enumerated(EnumType.STRING)
    private BloodGroup bloodGroup;

    // Request status (PENDING / APPROVED / REJECTED / COMPLETED)
    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    // When request was created
    private LocalDateTime requestDate;
}
