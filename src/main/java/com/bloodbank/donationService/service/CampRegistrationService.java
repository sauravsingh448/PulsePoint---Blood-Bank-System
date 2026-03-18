package com.bloodbank.donationService.service;

import com.bloodbank.donationService.dto.CampDashboardDTO;
import com.bloodbank.donationService.entity.CampRegistration;
import com.bloodbank.donationService.enum1.RegistrationStatus;

import java.util.List;

public interface CampRegistrationService {

    // Donor register for a camp
    CampRegistration registerForCamp(Long campId, Long donorId) throws Exception;

    // Admin: get all registrations of a camp
    List<CampRegistration> getCampRegistrations(Long campId) throws Exception;

    // Donor: get all registrations of a donor
    List<CampRegistration> getDonorRegistrations(Long donorId) throws Exception;

    // Admin approve / reject donor
    CampRegistration updateRegistrationStatus(Long registrationId, RegistrationStatus status) throws Exception;

    // Donor cancel registration
    CampRegistration cancelRegistration(Long registrationId, Long donorId);

    // Admin get registrations by status
    List<CampRegistration> getRegistrationsByStatus(Long campId, RegistrationStatus status) throws Exception;

    // Admin mark donor attended
    CampRegistration markDonorAttended(Long registrationId) throws Exception;

    //Camp Registration Count
    long getRegistrationCount(Long campId) throws Exception;

    // give the camp data
    CampDashboardDTO getCampDashboard(Long campId) throws Exception;
}