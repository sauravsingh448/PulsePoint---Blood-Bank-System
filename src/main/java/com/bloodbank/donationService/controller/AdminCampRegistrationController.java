package com.bloodbank.donationService.controller;

import com.bloodbank.donationService.dto.CampDashboardDTO;
import com.bloodbank.donationService.entity.CampRegistration;
import com.bloodbank.donationService.entity.DonationRecord;
import com.bloodbank.donationService.enum1.RegistrationStatus;
import com.bloodbank.donationService.service.CampRegistrationService;
import com.bloodbank.donationService.service.DonationRecordService;
import com.bloodbank.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/camps")
@RequiredArgsConstructor
public class AdminCampRegistrationController {
    private final CampRegistrationService campRegistrationService;
    private final DonationRecordService donationRecordService;

    // Get all registrations of a camp
    @GetMapping("/{campId}/registration")
    public ResponseEntity<List<CampRegistration>> getCampRegistrations(
            @PathVariable Long campId
    )
        throws Exception{
        Long adminId = SecurityUtils.getCurrentUserId();
        List<CampRegistration> registrations = campRegistrationService.getCampRegistrations(campId);
        return ResponseEntity.ok(registrations);
    }

    // get registration by status
    @GetMapping("/{campId}/registration/status")
    public ResponseEntity<List<CampRegistration>> getRegistrationsByStatus(
            @PathVariable Long campId,
            @RequestParam RegistrationStatus status
            )throws Exception{
        List<CampRegistration> registrations = campRegistrationService.getRegistrationsByStatus(campId, status);
        return ResponseEntity.ok(registrations);
    }

    // Approved or Reject donor
    @PutMapping("/registrations/{registrationId}/status")
    public ResponseEntity<CampRegistration> updateRegistrationStatus(
            @PathVariable Long registrationId,
            @RequestParam RegistrationStatus status
    ) throws Exception{
        CampRegistration registration = campRegistrationService.updateRegistrationStatus(registrationId, status);
        return ResponseEntity.ok(registration);
    }

    //Mark donor attend
    @PutMapping("/registrations/{registrationId}/attended")
    public ResponseEntity<CampRegistration> markDonorAttended(
            @PathVariable Long registrationId
    ) throws Exception{
        CampRegistration registration = campRegistrationService.markDonorAttended(registrationId);
        return ResponseEntity.ok(registration);
    }

    // Camp registration count
    @GetMapping("/{campId}/registration-count")
    public ResponseEntity<Long> getRegistrationCount(
            @PathVariable Long campId
    ) throws Exception{
     Long count = campRegistrationService.getRegistrationCount(campId);
     return ResponseEntity.ok(count);
    }

    @GetMapping("/{campId}/dashboard")
    public ResponseEntity<CampDashboardDTO> getCampDashboard(
            @PathVariable Long campId
    )
        throws Exception{
        CampDashboardDTO dashboardDTO =
                campRegistrationService.getCampDashboard(campId);
        return ResponseEntity.ok(dashboardDTO);
    }

    // camp donation list from donation Record API
    @GetMapping("/{campId}/camp")
    public ResponseEntity<List<DonationRecord>> getCampDonations(
            @PathVariable Long campId
    )throws Exception{
        List<DonationRecord> donationRecords = donationRecordService.getCampDonations(campId);
        return ResponseEntity.ok(donationRecords);
    }
}
