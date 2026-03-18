package com.bloodbank.bloodRequestService.repository;

import com.bloodbank.bloodRequestService.entity.BloodRequest;
import com.bloodbank.bloodRequestService.enums.RequestStatus;
import com.bloodbank.blood_bank_service.enums.BloodGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BloodRequestRepository extends JpaRepository<BloodRequest, Long> {
    // Get all requests of a specific blood bank
    // admin of blood bank id = 1 can see all requests
    List<BloodRequest> findByBloodBank_Id(Long bloodBankId);

    // Get requests by status (PENDING / APPROVED / COMPLETED / REJECTED)
    // admin wants to see only pending requests
    List<BloodRequest> findByStatus(RequestStatus status);

    // Get requests by blood group
    // search all requests for A+ blood
    List<BloodRequest> findByBloodGroup(BloodGroup bloodGroup);

    // Get requests of a blood bank with specific status
    // pending requests for blood bank id = 1
    List<BloodRequest> findByBloodBank_IdAndStatus(Long bloodBankId, RequestStatus status);
}
