package com.bloodbank.donationService.entity;

import com.bloodbank.donationService.enum1.RegistrationStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "camp_registrations",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"camp_id", "donor_id"})
        }
)
@Getter
@Setter
@NoArgsConstructor // create no-argument constructor
@AllArgsConstructor // create with all fields (like name,age,rollNo)
@Builder
public class CampRegistration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Donor ID from USER-SERVICE
    @Column(name = "donor_id", nullable = false)
    private Long donorId;

    // Many registration can belong to ONE camp
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "camp_id", nullable = false)
    @JsonIgnore
    private DonationCamp camp;

    //Registration status
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RegistrationStatus status;

    // Registration time
    @Column(nullable = false)
    private LocalDateTime registeredAt;

    // when admin approved / rejected
    private LocalDateTime decisionAt;

    // Automatically set registration time and default status
    @PrePersist
    public void prePersist(){
        this.registeredAt = LocalDateTime.now();

        //If status not set then make it PENDING
        //Agar status frontend se nahi aaya → default PENDING set ho jayega
        if(this.status == null){
            this.status = RegistrationStatus.PENDING;
        }
    }
}
