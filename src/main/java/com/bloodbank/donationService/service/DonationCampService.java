package com.bloodbank.donationService.service;

import com.bloodbank.donationService.dto.DonationCampRequest;
import com.bloodbank.donationService.enum1.CampStatus;
import com.bloodbank.donationService.entity.DonationCamp;

import java.util.List;

public interface DonationCampService {
    // Create a new donation camp (ADMIN)
    DonationCamp createCamp(DonationCampRequest request, Long bloodBankId) throws Exception;

    //Update an existing camp (ADMIN)
    DonationCamp updateCamp(Long campId, DonationCampRequest request, Long bloodBankId) throws Exception;

    // Delete a donation camp by its ID (ADMIN)
    void deleteCamp(Long campId) throws Exception;

    // Get all Currently active donation camps
    List<DonationCamp> getActiveCamps() throws Exception;

    // Get all upcoming donation camp
    List<DonationCamp> getUpcomingCamp() throws Exception;

    // close a donation camp (ADMIN)
    void closeCamp(Long campId) throws Exception;

    // find camps by camp name public
    List<DonationCamp> searchCampByName(String campName) throws Exception;
    // find camps by camp name with upcoming camps
    List<DonationCamp> searchCampByNameOrUpComing(String campName) throws Exception;

    // find the camp by city
    List<DonationCamp> SearchCampByCity(String city) throws Exception;

    // search nearby camps by using (Lat + Long)
    List<DonationCamp> searchNearbyCampsByName(
            Double userLatitude,
            Double userLongitude,
            Double radius,
            CampStatus status
    ) throws Exception;
}