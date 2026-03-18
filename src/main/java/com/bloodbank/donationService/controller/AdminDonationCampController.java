package com.bloodbank.donationService.controller;

import com.bloodbank.donationService.dto.DonationCampRequest;
import com.bloodbank.donationService.enum1.CampStatus;
import com.bloodbank.donationService.entity.DonationCamp;
import com.bloodbank.donationService.service.DonationCampService;
import com.bloodbank.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Only admin Access this controller
@RestController
@RequestMapping("/api/admin/donation")
public class AdminDonationCampController {

    @Autowired
    private DonationCampService donationCampService;

    // create Camp by Admin
    @PostMapping("/create-camp")
    public ResponseEntity<DonationCamp> createCamp(
            @RequestBody DonationCampRequest request
    ) throws Exception {
        Long adminId = SecurityUtils.getCurrentUserId();
        DonationCamp camp = donationCampService.createCamp(request, adminId);
        return ResponseEntity.ok(camp);
    }

    // update Camp by Admin
    @PutMapping("/{campId}/update-camp")
    public ResponseEntity<DonationCamp> updateCamp(
            @PathVariable Long campId,
            @RequestBody DonationCampRequest request,
            @RequestParam Long BloodBankId
    ) throws Exception {
        DonationCamp camp = donationCampService.updateCamp(campId, request, BloodBankId);
        return ResponseEntity.ok(camp);
    }
    // delete Camp by Admin
    @DeleteMapping("/{campId}/delete-camp")
    public ResponseEntity<String> deleteCamp(@PathVariable Long campId) throws Exception {
        donationCampService.deleteCamp(campId);
        return ResponseEntity.ok("Donation camp deleted successfully");
    }

    // Get CLOSE Camps for (Admin)
    @PatchMapping("/{campId}/close") // PatchMapping used for update only one field
    public ResponseEntity<String> closeCamp(@PathVariable Long campId) throws Exception{
        donationCampService.closeCamp(campId);
        return ResponseEntity.ok("Donation camp closed successfully");
    }

    // Get ACTIVE Camps
    @GetMapping("/active-camp")
    public ResponseEntity<List<DonationCamp>> getActiveCamps() throws Exception{
        return ResponseEntity.ok(donationCampService.getActiveCamps());
    }

    // Get UPCOMING Camps
    @GetMapping("/upcoming")
    public ResponseEntity<List<DonationCamp>> getUpcomingCamps() throws Exception{
        return ResponseEntity.ok(donationCampService.getUpcomingCamp());
    }

    // search camp
    @GetMapping("/search-camps")
    public ResponseEntity<List<DonationCamp>> searchCampByName(
            @RequestParam String campName
    ) throws Exception {
        List<DonationCamp> camps = donationCampService.searchCampByName(campName);
        return ResponseEntity.ok(camps);
    }

    @GetMapping("/search-camps-upcoming")
    public ResponseEntity<List<DonationCamp>> searchCampByNameOrUpcoming(
            @RequestParam String campName
    )throws Exception{
        List<DonationCamp> camps = donationCampService.searchCampByNameOrUpComing(campName);
        return ResponseEntity.ok(camps);
    }

    @GetMapping("/search-camps-by-city")
    public ResponseEntity<List<DonationCamp>> searchCampByCity(
            @RequestParam String city
    ) throws Exception {
        List<DonationCamp> camps = donationCampService.SearchCampByCity(city);
        return ResponseEntity.ok(camps);
    }

    // camps search by using the Lat and long
    @GetMapping("/search/nearbyCamps")
    public ResponseEntity<List<DonationCamp>> searchNearbyCampsByName(
            @RequestParam Double userLatitude,
            @RequestParam Double userLongitude,
            @RequestParam(defaultValue = "200") Double radius,
            @RequestParam CampStatus status
    )throws Exception{
        List<DonationCamp> camps = donationCampService.searchNearbyCampsByName(
                userLatitude,
                userLongitude,
                radius,
                status
        );
        return ResponseEntity.ok(camps);
    }
}
