package com.bloodbank.donationService.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "donation_records")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DonationRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // donor id from user service
    @Column(nullable = false)
    private Long donorId;

    // donation camp Id
    @Column(nullable = false)
    private Long campId;

    //Blood bank id
    @Column(nullable = false)
    private Long bloodBankId;

    // blood group donated
    @Column(nullable = false)
    private String bloodGroup;

    // how many units donated
    @Column(nullable = false)
    private Integer unitsDonated;

    // donation date
    @Column(nullable = false)
    private LocalDateTime donationDate;
}
