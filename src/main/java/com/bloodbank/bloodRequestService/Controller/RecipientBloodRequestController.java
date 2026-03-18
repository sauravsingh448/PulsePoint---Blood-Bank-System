package com.bloodbank.bloodRequestService.Controller;

import com.bloodbank.bloodRequestService.dto.BloodRequestDTO;
import com.bloodbank.bloodRequestService.entity.BloodRequest;
import com.bloodbank.bloodRequestService.service.BloodRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recipient")
@RequiredArgsConstructor
public class RecipientBloodRequestController {
    private final BloodRequestService bloodRequestService;

    // Create blood request (Patient / Hospital, RECIPIENT)
    @PostMapping("/requests")
    public ResponseEntity<BloodRequest> createRequest(@RequestBody BloodRequestDTO request) {
        BloodRequest createdRequest = bloodRequestService.createRequest(request);
        return ResponseEntity.ok(createdRequest);
    }

    // Get request by ID (RECIPIENT)
    @GetMapping("/requests/{requestId}")
    public ResponseEntity<BloodRequest> getRequestById(@PathVariable Long requestId) {

        BloodRequest request = bloodRequestService.getRequestById(requestId);
        return ResponseEntity.ok(request);
    }
}
