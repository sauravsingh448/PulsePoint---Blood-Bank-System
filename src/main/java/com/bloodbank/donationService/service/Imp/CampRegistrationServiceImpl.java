package com.bloodbank.donationService.service.Imp;

import com.bloodbank.donationService.dto.CampDashboardDTO;
import com.bloodbank.donationService.entity.CampRegistration;
import com.bloodbank.donationService.entity.DonationRecord;
import com.bloodbank.donationService.enum1.CampStatus;
import com.bloodbank.donationService.entity.DonationCamp;
import com.bloodbank.donationService.enum1.RegistrationStatus;
import com.bloodbank.donationService.repository.CampRegistrationRepository;
import com.bloodbank.donationService.repository.DonationCampRepository;
import com.bloodbank.donationService.repository.DonationRecordRepository;
import com.bloodbank.donationService.service.CampRegistrationService;
import com.bloodbank.userService.entity.User;
import com.bloodbank.userService.service.DonorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CampRegistrationServiceImpl implements CampRegistrationService {
    //for DonationCamp table
    private final DonationCampRepository campRepository;
    private final DonationRecordRepository donationRecordRepository;
    // for CampRegistration table
    private final CampRegistrationRepository campRegistrationRepository;
    private final DonorService donorService;

    @Override
    public CampRegistration registerForCamp(Long campId, Long donorId) throws Exception {
        // check camp fount or not
        DonationCamp donationCamp = campRepository.findById(campId)
                .orElseThrow(() ->
                        new RuntimeException("Donation camp not found"));

        //  Allow registration only for UPCOMING or ACTIVE camp
        if(donationCamp.getStatus() == CampStatus.CLOSED){
            throw new RuntimeException("Camp is already closed: ");
        }

        // check duplicate registration
        boolean alreadyRegistered = campRegistrationRepository.existsByCamp_IdAndDonorId(campId,donorId);
        if(alreadyRegistered){
            throw new RuntimeException("you are already registered for this Camp: ");
        }

        CampRegistration req = new CampRegistration();
        req.setDonorId(donorId);
        req.setCamp(donationCamp);
        req.setStatus(RegistrationStatus.PENDING);  // default status
        req.setRegisteredAt(LocalDateTime.now());  // donor register time

        return campRegistrationRepository.save(req);
    }

    // Admin views registrations of a camp
    @Override
    public List<CampRegistration> getCampRegistrations(Long campId) throws Exception {
        // check camp exists
        DonationCamp camp = campRepository.findById(campId)
                .orElseThrow(() -> new RuntimeException("Camp not found"));

        return campRegistrationRepository.findByCamp_Id(campId);
    }

    // Donor views his registrations
    @Override
    public List<CampRegistration> getDonorRegistrations(Long donorId) throws Exception {
        List<CampRegistration> registrations = campRegistrationRepository.findByDonorId(donorId);
        if(registrations.isEmpty()){
            throw new RuntimeException("No registrations found for this donor");
        }
        return registrations;
    }

    //Admin approves or rejects registration
    @Override
    public CampRegistration updateRegistrationStatus(
            Long registerId,
            RegistrationStatus status
    ) throws Exception {
        // check registration exists
        CampRegistration registration = campRegistrationRepository.findById(registerId)
                .orElseThrow(() -> new RuntimeException("Registration not found"));

        // prevent updating if already decided
        if (registration.getStatus() != RegistrationStatus.PENDING) {
            throw new RuntimeException("Registration already processed");
        }
        // allow only APPROVED or REJECTED
        if (status != RegistrationStatus.APPROVED &&
                status != RegistrationStatus.REJECTED) {

            throw new RuntimeException("Invalid status update");
        }
        // update status
        registration.setStatus(status);
        registration.setDecisionAt(LocalDateTime.now());

        return campRegistrationRepository.save(registration);
    }

    @Override
    public CampRegistration cancelRegistration(Long registrationId, Long donorId){
        // find registration
        CampRegistration registration = campRegistrationRepository.findById(registrationId)
                .orElseThrow(() -> new RuntimeException("Registration not found"));

        // check donor ownership
        if(!registration.getDonorId().equals(donorId)){
            throw new RuntimeException("You are not allowed to cancel this registration");
        }

        // check if all ready cancelled
        if(registration.getStatus() == RegistrationStatus.CANCELLED){
            throw new RuntimeException("Registration already cancelled");
        }

        // donor cannot cancel after attending
        if(registration.getStatus() == RegistrationStatus.ATTENDED){
            throw new RuntimeException("Cannot cancel after attending the camp");
        }
        // update status
        registration.setStatus(RegistrationStatus.CANCELLED);
        registration.setDecisionAt(LocalDateTime.now());

        return campRegistrationRepository.save(registration);
    }

    @Override
    public List<CampRegistration> getRegistrationsByStatus(Long campId, RegistrationStatus status) throws Exception {
        // check camp exits
        DonationCamp camp = campRepository.findById(campId)
                .orElseThrow(() -> new RuntimeException("Camp not found"));

        // fetch registrations by status
        List<CampRegistration> registrations =
                campRegistrationRepository.findByCamp_IdAndStatus(campId, status);

        if (registrations.isEmpty()) {
            throw new RuntimeException("No registrations found with status: " + status);
        }

        return registrations;
    }

    @Override
    public CampRegistration markDonorAttended(Long registrationId) throws Exception {
        // check registration exists
        CampRegistration registration = campRegistrationRepository.findById(registrationId)
                .orElseThrow(() -> new RuntimeException("Registration not found"));

        // donor must be approved first
        if(registration.getStatus() != RegistrationStatus.APPROVED){
            throw new RuntimeException("Only approved donors can be marked as attended");
        }
        // update status
        registration.setStatus(RegistrationStatus.ATTENDED);
        registration.setDecisionAt(LocalDateTime.now());

        campRegistrationRepository.save(registration);

        // fetch donor from user service
        User donor = donorService.getDonorById(registration.getDonorId());

        if (donor == null) {
            throw new RuntimeException("Donor not found with id: " + registration.getDonorId());
        }

        // get blood group
        String bloodGroup = donor.getBloodGroup();

        // CREATE DONATION RECORD
        DonationRecord record = new DonationRecord();
        record.setDonorId(registration.getDonorId());
        record.setCampId(registration.getCamp().getId());
        record.setBloodBankId(registration.getCamp().getBloodBankId());
        record.setBloodGroup(bloodGroup);
        record.setUnitsDonated(1);
        record.setDonationDate(LocalDateTime.now());

        donationRecordRepository.save(record);

        return registration;
    }

    @Override
    public long getRegistrationCount(Long campId) throws Exception {
        return campRegistrationRepository.countByCamp_Id(campId);
    }

    @Override
    public CampDashboardDTO getCampDashboard(Long campId) throws Exception {
        CampDashboardDTO dashboard = new CampDashboardDTO();
        dashboard.setCampId(campId);
        dashboard.setTotalRegistrations(campRegistrationRepository.countByCamp_Id(campId));
        dashboard.setApproved(
                campRegistrationRepository.countByCamp_IdAndStatus(
                        campId,
                        RegistrationStatus.APPROVED
                )
        );
        dashboard.setPending(
                campRegistrationRepository.countByCamp_IdAndStatus(
                        campId,
                        RegistrationStatus.PENDING
                )
        );
        dashboard.setRejected(
                campRegistrationRepository.countByCamp_IdAndStatus(
                        campId,
                        RegistrationStatus.REJECTED
                )
        );
        dashboard.setAttended(
                campRegistrationRepository.countByCamp_IdAndStatus(
                        campId,
                        RegistrationStatus.ATTENDED
                )
        );
        return dashboard;
    }
}