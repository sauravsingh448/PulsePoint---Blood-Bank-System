package com.bloodbank.donationService.controller;

import com.bloodbank.donationService.entity.DonationRecord;
import com.bloodbank.donationService.service.DonationRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/donor")
@RequiredArgsConstructor
public class DonationRecordController {
    private final DonationRecordService donationRecordService;

    // Donor donation history
    @GetMapping("/my-history/{donorId}")
    public ResponseEntity<List<DonationRecord>> getDonationHistory(
            @PathVariable Long donorId
    )throws Exception{
        List<DonationRecord> donationRecords = donationRecordService.getDonorDonationHistory(donorId);
        return ResponseEntity.ok(donationRecords);
    }
}
