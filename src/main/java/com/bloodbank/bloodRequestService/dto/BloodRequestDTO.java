package com.bloodbank.bloodRequestService.dto;

import com.bloodbank.blood_bank_service.enums.BloodGroup;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BloodRequestDTO {
    private String patientName;

    private BloodGroup bloodGroup;

    private Integer unitsRequired;

    private String hospitalName;

    private String city;

    // patient location
    private Double latitude;
    private Double longitude;
}
