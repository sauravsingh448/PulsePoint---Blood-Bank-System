package com.bloodbank.blood_bank_service.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BloodBankDto {
    private String name;
    private String contact;
    private String address;
    private String city;
    private Double latitude;
    private Double longitude;
    private Long adminUserId;
}
