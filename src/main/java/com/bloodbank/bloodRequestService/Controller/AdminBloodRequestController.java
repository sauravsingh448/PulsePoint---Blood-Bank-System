package com.bloodbank.bloodRequestService.Controller;

import com.bloodbank.bloodRequestService.entity.BloodRequest;
import com.bloodbank.bloodRequestService.enums.RequestStatus;
import com.bloodbank.bloodRequestService.service.BloodRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/blood-requests")
@RequiredArgsConstructor
public class AdminBloodRequestController {

    private final BloodRequestService bloodRequestService;

    // Get all requests of a blood bank(Admin)
    @GetMapping("/bloodBank/{bloodBankId}")
    public ResponseEntity<List<BloodRequest>> getRequestsByBloodBank(
            @PathVariable Long bloodBankId) {

        List<BloodRequest> requests =
                bloodRequestService.getRequestsByBloodBank(bloodBankId);
        return ResponseEntity.ok(requests);
    }

    // Get request by ID (Admin)
    @GetMapping("/requests/{requestId}")
    public ResponseEntity<BloodRequest> getRequestById(@PathVariable Long requestId) {

        BloodRequest request = bloodRequestService.getRequestById(requestId);
        return ResponseEntity.ok(request);
    }

    // Get Requests By Status (Admin)
    @GetMapping("/requests/{bloodBankId}/status")
    public ResponseEntity<List<BloodRequest>> getRequestsByStatus(
            @PathVariable Long bloodBankId,
            @RequestParam RequestStatus status) {
        List<BloodRequest> requests =
                bloodRequestService.getRequestsByStatus(bloodBankId, status);
        return ResponseEntity.ok(requests);
    }

    // Get Requests By Blood Group (Admin)
    @GetMapping("/requests/bloodGroup")
    public ResponseEntity<List<BloodRequest>> getRequestsByBloodGroup(
            @RequestParam String bloodGroup) {
        List<BloodRequest> requests = bloodRequestService.getRequestsByBloodGroup(bloodGroup);
        return ResponseEntity.ok(requests);
    }

    // Admin approve request
    @PutMapping("/{requestId}/approve")
    public ResponseEntity<BloodRequest> approveRequest(@PathVariable Long requestId) {
        BloodRequest request = bloodRequestService.approveRequest(requestId);
        return ResponseEntity.ok(request);
    }
    // Admin reject request
    @PutMapping("/{requestId}/reject")
    public ResponseEntity<BloodRequest> rejectRequest(@PathVariable Long requestId) {
        BloodRequest request = bloodRequestService.rejectRequest(requestId);
        return ResponseEntity.ok(request);
    }

    // Mark request as completed (blood issued) by Admin
    @PutMapping("/{requestId}/complete")
    public ResponseEntity<BloodRequest> completeRequest(@PathVariable Long requestId) {
        BloodRequest request = bloodRequestService.completeRequest(requestId);
        return ResponseEntity.ok(request);
    }

}
