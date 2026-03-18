package com.bloodbank.donationService.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CampDashboardDTO {
    private Long campId;
    private Long totalRegistrations;
    private Long approved;
    private Long pending;
    private Long rejected;
    private Long attended;
}
