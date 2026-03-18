package com.bloodbank.donationService.controller;

import com.bloodbank.donationService.entity.CampRegistration;
import com.bloodbank.donationService.service.CampRegistrationService;
import com.bloodbank.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/donor/camp-registrations")
@RequiredArgsConstructor
public class DonorCampRegistrationController {

    private final CampRegistrationService campRegistrationService;

    // Register for camp
    @PostMapping("/{campId}/register")
    public ResponseEntity<CampRegistration> registerForCamp(
            @PathVariable Long campId
    ) throws Exception{
        Long donorId = SecurityUtils.getCurrentUserId();
        CampRegistration registration = campRegistrationService.registerForCamp(campId, donorId);
        return ResponseEntity.ok(registration);
    }

    // View my registrations
    @GetMapping("/my-registrations")
    public ResponseEntity<List<CampRegistration>> getMyRegistrations() throws Exception{
        Long donorId = SecurityUtils.getCurrentUserId();
        List<CampRegistration> registrations =
                campRegistrationService.getDonorRegistrations(donorId);

        return ResponseEntity.ok(registrations);
    }

    //Cancel registration
    @DeleteMapping("/{registrationId}/cancel")
    public ResponseEntity<CampRegistration> cancelRegistration(
            @PathVariable Long registrationId
    ) throws Exception{
        Long donorId = SecurityUtils.getCurrentUserId();
        CampRegistration registration = campRegistrationService.cancelRegistration(registrationId, donorId);
        return ResponseEntity.ok(registration);
    }
}
