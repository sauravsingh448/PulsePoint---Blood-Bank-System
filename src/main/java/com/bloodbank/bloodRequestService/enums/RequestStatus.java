package com.bloodbank.bloodRequestService.enums;

public enum RequestStatus {
    PENDING,     // Request created but not reviewed by admin

    APPROVED,    // Admin approved request

    REJECTED,    // Admin rejected request

    COMPLETED    // Blood issued to patient
}
