package com.bloodbank.bloodRequestService.service;

import com.bloodbank.bloodRequestService.dto.BloodRequestDTO;
import com.bloodbank.bloodRequestService.entity.BloodRequest;
import com.bloodbank.bloodRequestService.enums.RequestStatus;

import java.util.List;

public interface BloodRequestService {
    // Create new blood request (patient / hospital request)
    BloodRequest createRequest(BloodRequestDTO request);

    // Get all blood requests of a blood bank
    List<BloodRequest> getRequestsByBloodBank(Long bloodBankId);

    // Get requests by status (PENDING / COMPLETED etc.)
    List<BloodRequest> getRequestsByStatus(Long bloodBankId, RequestStatus status);

    // Admin approve blood request
    BloodRequest approveRequest(Long requestId);

    BloodRequest getRequestById(Long requestId);

    // Admin reject request
    BloodRequest rejectRequest(Long requestId);

    // Mark request completed after issuing blood
    BloodRequest completeRequest(Long requestId);

    // Get requests by status (PENDING / APPROVED / REJECTED / COMPLETED)
    List<BloodRequest> getRequestsByStatus(RequestStatus status);

    // Get requests by blood group
    List<BloodRequest> getRequestsByBloodGroup(String bloodGroup);
}
