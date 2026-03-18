package com.bloodbank.donationService.service.Imp;


import com.bloodbank.blood_bank_service.entity.BloodBank;
import com.bloodbank.blood_bank_service.repository.BloodBankRepository;
import com.bloodbank.donationService.dto.DonationCampRequest;
import com.bloodbank.donationService.enum1.CampStatus;
import com.bloodbank.donationService.entity.DonationCamp;
import com.bloodbank.donationService.repository.DonationCampRepository;
import com.bloodbank.donationService.service.DonationCampService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
@Service
@RequiredArgsConstructor
public class DonationCampServiceImp implements DonationCampService {

    private final DonationCampRepository donationCampRepository;
    private final BloodBankRepository bloodBankRepository;

    @Override
    public DonationCamp createCamp(DonationCampRequest request, Long adminId) {

        // Find BloodBank using bloodBankId
        BloodBank bloodBank = bloodBankRepository.findById(request.getBloodBankId())
                .orElseThrow(() -> new RuntimeException("Blood Bank not found"));

        // Check this blood bank belongs to admin
        if(!bloodBank.getAdminUserId().equals(adminId)){
            throw new RuntimeException("This blood bank does not belong to this admin");
        }

        // Validate camp date
        if (request.getCampDate().isBefore(LocalDate.now())) {
            throw new RuntimeException("Camp date cannot be in the past");
        }

        // Validate time
        if (request.getEndTime().isBefore(request.getStartTime())) {
            throw new RuntimeException("End time must be after start time");
        }
        // Create Donation Camp object
        DonationCamp camp = new DonationCamp();
        camp.setCampName(request.getCampName());
        camp.setCampDate(request.getCampDate());
        camp.setStartTime(request.getStartTime());
        camp.setEndTime(request.getEndTime());
        camp.setAddress(request.getAddress());
        camp.setLatitude(request.getLatitude());
        camp.setLongitude(request.getLongitude());

        // Set BloodBank Id
        camp.setBloodBankId(bloodBank.getId());

        // Default status
        camp.setStatus(CampStatus.UPCOMING);

        // Save camp
        return donationCampRepository.save(camp);
    }

    @Override
    public DonationCamp updateCamp(Long campId, DonationCampRequest request, Long bloodBankId) {

        DonationCamp existingCamp = donationCampRepository.findById(campId)
                .orElseThrow(() -> new RuntimeException("Camp not found"));

        if (!existingCamp.getBloodBankId().equals(bloodBankId)) {
            throw new RuntimeException("You cannot update this existingCamp");
        }

        // only update ACTIVE camp
        if (existingCamp.getStatus() != CampStatus.ACTIVE
                && existingCamp.getStatus() != CampStatus.UPCOMING) {
            throw new RuntimeException("Only ACTIVE or UPCOMING camps can be updated");
        }

        // validate time
        if (request.getEndTime().isBefore(request.getStartTime())) {
            throw new RuntimeException("End time must be after start time");
        }

        existingCamp.setCampName(request.getCampName());
        existingCamp.setCampDate(request.getCampDate());
        existingCamp.setStartTime(request.getStartTime());
        existingCamp.setEndTime(request.getEndTime());
        existingCamp.setAddress(request.getAddress());
        existingCamp.setLatitude(request.getLatitude());
        existingCamp.setLongitude(request.getLongitude());

        return donationCampRepository.save(existingCamp);
    }

    @Override
    public void deleteCamp(Long campId) {

        if (!donationCampRepository.existsById(campId)) {
            throw new RuntimeException("Donation Camp not found");
        }
        donationCampRepository.deleteById(campId);
    }

    @Override
    public List<DonationCamp> getActiveCamps() {

        // auto active today camp
        List<DonationCamp> upcomingToday = donationCampRepository
                .findByStatusAndCampDateGreaterThanEqual(
                        CampStatus.UPCOMING,
                        LocalDate.now()
                );

        for(DonationCamp camp : upcomingToday){
            if (camp.getCampDate().equals(LocalDate.now())) {
                camp.setStatus(CampStatus.ACTIVE);
                donationCampRepository.save(camp);
            }
        }

        List<DonationCamp> activeCamps =
                donationCampRepository.findByStatus(CampStatus.ACTIVE);

        if (activeCamps.isEmpty()) {
            throw new RuntimeException("No active camps found");
        }

        return activeCamps;
    }

    @Override
    public List<DonationCamp> getUpcomingCamp() {

        List<DonationCamp> upcomingCamps =
                donationCampRepository.findByStatusAndCampDateGreaterThanEqual(
                        CampStatus.UPCOMING,
                        LocalDate.now());

        if (upcomingCamps.isEmpty()) {
            throw new RuntimeException("No upcoming donation camps found");
        }

        return upcomingCamps;
    }

    @Override
    public void closeCamp(Long campId) {

        DonationCamp camp = donationCampRepository.findById(campId)
                .orElseThrow(() -> new RuntimeException("Donation Camp not found"));

        camp.setStatus(CampStatus.CLOSED);
        donationCampRepository.save(camp);
    }


    @Override
    public List<DonationCamp> searchCampByName(String campName) throws Exception {
        List<DonationCamp> camp = donationCampRepository.findByCampNameContainingIgnoreCaseAndStatus(
                campName,
                CampStatus.ACTIVE
        );
        if(camp.isEmpty()){
            throw new RuntimeException("No donation camps found with name: " + campName);
        }
        return camp;
    }

    @Override
    public List<DonationCamp> searchCampByNameOrUpComing(String campName) throws Exception {
        List<DonationCamp> camps = donationCampRepository.findByCampNameContainingIgnoreCaseAndStatus(campName,
                CampStatus.UPCOMING
        );
        if(camps.isEmpty()){
            throw new RuntimeException("No donation camps found with name: " + campName);
        }
        return camps;
    }

    @Override
    public List<DonationCamp> SearchCampByCity(String city) throws Exception {
        List<DonationCamp> camps = donationCampRepository.findByCityInAddress(city);

        if (camps.isEmpty()) {
            throw new RuntimeException("No camps found in this city: " + city);
        }
        return camps;
    }

    @Override
    public List<DonationCamp> searchNearbyCampsByName(
            Double userLatitude,
            Double userLongitude,
            Double radius,
            CampStatus status
    ) throws Exception {
        List<DonationCamp> camps = donationCampRepository.searchNearbyCamps(userLatitude,userLongitude,radius,status);
        if(camps.isEmpty()){
            return Collections.emptyList();
        }
        return camps;
    }
}