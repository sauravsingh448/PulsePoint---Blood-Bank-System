package com.bloodbank.donationService.controller;

import com.bloodbank.donationService.enum1.CampStatus;
import com.bloodbank.donationService.entity.DonationCamp;
import com.bloodbank.donationService.service.DonationCampService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/donation")
public class DonationCampController {

    @Autowired
    private DonationCampService donationCampService;

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

    // search Active camp by camp name
    @GetMapping("/search-camps")
    public ResponseEntity<List<DonationCamp>> searchCampByName(
            @RequestParam String campName
    ) throws Exception {
        List<DonationCamp> camps = donationCampService.searchCampByName(campName);
        return ResponseEntity.ok(camps);
    }
    // search upcoming camps by camp name
    @GetMapping("/search-camps-upcoming")
    public ResponseEntity<List<DonationCamp>> searchCampByNameOrUpcoming(
            @RequestParam String campName
    )throws Exception{
        List<DonationCamp> camps = donationCampService.searchCampByNameOrUpComing(campName);
        return ResponseEntity.ok(camps);
    }

    // find the camps by city name
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
