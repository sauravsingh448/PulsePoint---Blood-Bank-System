package com.bloodbank.donationService.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DonationCampRequest {

    private String CampName;
    private LocalDate campDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String address;
    private Double latitude;
    private Double longitude;
    private Long bloodBankId;
}
