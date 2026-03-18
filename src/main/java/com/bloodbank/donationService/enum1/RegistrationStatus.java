package com.bloodbank.donationService.enum1;

public enum RegistrationStatus {
    PENDING,     // Waiting for admin approval
    APPROVED,    // Admin approved donor
    REJECTED,    // Admin rejected donor
    CANCELLED,   // Donor cancelled registration
    ATTENDED     // Donor came and donated blood
}
