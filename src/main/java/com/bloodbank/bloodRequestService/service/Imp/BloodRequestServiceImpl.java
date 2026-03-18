package com.bloodbank.bloodRequestService.service.Imp;

import com.bloodbank.bloodRequestService.dto.BloodRequestDTO;
import com.bloodbank.bloodRequestService.entity.BloodRequest;
import com.bloodbank.bloodRequestService.enums.RequestStatus;
import com.bloodbank.bloodRequestService.repository.BloodRequestRepository;
import com.bloodbank.bloodRequestService.service.BloodRequestService;
import com.bloodbank.blood_bank_service.entity.BloodBank;
import com.bloodbank.blood_bank_service.entity.Inventory;
import com.bloodbank.blood_bank_service.enums.BloodGroup;
import com.bloodbank.blood_bank_service.repository.BloodBankRepository;
import com.bloodbank.blood_bank_service.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor // Lombok constructor
public class BloodRequestServiceImpl implements BloodRequestService {
    private final BloodRequestRepository bloodRequestRepository;
    private final BloodBankRepository bloodBankRepository;
    private final InventoryRepository inventoryRepository;

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {

        final int EARTH_RADIUS = 6371; // km

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2)
                * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c;
    }

    @Override
    public BloodRequest createRequest(BloodRequestDTO request) {

        if (request == null) {
            throw new RuntimeException("Request cannot be null");
        }

        List<BloodBank> banks = bloodBankRepository.findByCity(request.getCity());

        if (banks.isEmpty()) {
            throw new RuntimeException("No blood banks in this city");
        }

        BloodBank nearestBank = null;
        double minDistance = Double.MAX_VALUE;

        for (BloodBank bank : banks) {
            // inventory check
            Optional<Inventory> inventoryOpt =
                    inventoryRepository.findByBloodBankIdAndBloodGroup(
                            bank.getId(),
                            request.getBloodGroup()
                    );

            if (inventoryOpt.isEmpty()) {
                continue;
            }

            Inventory inventory = inventoryOpt.get();

            // check units
            if (inventory.getQuantity() < request.getUnitsRequired()) {
                continue;
            }

            // calculate distance
            double distance = calculateDistance(
                    request.getLatitude(),
                    request.getLongitude(),
                    bank.getLatitude(),
                    bank.getLongitude()
            );

            if (distance < minDistance) {
                minDistance = distance;
                nearestBank = bank;
            }
        }

        if (nearestBank == null) {
            throw new RuntimeException("No blood bank available with required blood");
        }

        BloodRequest bloodRequest = new BloodRequest();

        bloodRequest.setPatientName(request.getPatientName());
        bloodRequest.setBloodGroup(request.getBloodGroup());
        bloodRequest.setUnitsRequired(request.getUnitsRequired());
        bloodRequest.setHospitalName(request.getHospitalName());
        bloodRequest.setCity(request.getCity());
        bloodRequest.setLatitude(request.getLatitude());
        bloodRequest.setLongitude(request.getLongitude());
        bloodRequest.setBloodBank(nearestBank);

        bloodRequest.setStatus(RequestStatus.PENDING);
        bloodRequest.setRequestDate(LocalDateTime.now());

        return bloodRequestRepository.save(bloodRequest);
    }

    // access by Admin
    @Override
    public List<BloodRequest> getRequestsByBloodBank(Long bloodBankId) {
        if (bloodBankId == null) {
            throw new IllegalArgumentException("BloodBankId cannot be null");
        }
        return bloodRequestRepository.findByBloodBank_Id(bloodBankId);
    }

    @Override
    public List<BloodRequest> getRequestsByStatus(Long bloodBankId, RequestStatus status) {
        if(bloodBankId == null){
            throw new RuntimeException("BloodBankId cannot be null");
        }
        if(status == null){
            throw new RuntimeException("Status cannot be null");
        }
        // fetch requests by status
        return bloodRequestRepository.findByBloodBank_IdAndStatus(bloodBankId, status);
    }

    @Override
    public BloodRequest approveRequest(Long requestId){
        if (requestId == null) {
            throw new RuntimeException("RequestId cannot be null");
        }
        // fetch request from database
        BloodRequest request = bloodRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Blood request not found"));
        // check if request already processed
        if (request.getStatus() != RequestStatus.PENDING) {
            throw new RuntimeException("Request already processed");
        }
        // approve request
        request.setStatus(RequestStatus.APPROVED);
        // save updated request
        return bloodRequestRepository.save(request);
    }

    @Override
    public BloodRequest getRequestById(Long requestId) {

        if (requestId == null) {
            throw new RuntimeException("RequestId cannot be null");
        }

        return bloodRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Blood request not found with id: " + requestId));
    }

    @Override
    public BloodRequest rejectRequest(Long requestId){
        if (requestId == null) {
            throw new RuntimeException("RequestId cannot be null");
        }
        // fetch request
        BloodRequest request = bloodRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Blood request not found"));
        // check request status
        if (request.getStatus() != RequestStatus.PENDING) {
            throw new RuntimeException("Request already processed");
        }
        // update status
        request.setStatus(RequestStatus.REJECTED);
        return bloodRequestRepository.save(request);
    }

    @Override
    public BloodRequest completeRequest(Long requestId){
        if (requestId == null) {
            throw new RuntimeException("RequestId cannot be null");
        }

        // fetch request
        BloodRequest request = bloodRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Blood request not found"));

        // request must be approved first
        if (request.getStatus() != RequestStatus.APPROVED) {
            throw new RuntimeException("Request must be approved before completion");
        }
        Long bloodBankId = request.getBloodBank().getId();
        // fetch inventory
        Inventory inventory = inventoryRepository
                .findByBloodBankIdAndBloodGroup(bloodBankId, request.getBloodGroup())
                .orElseThrow(() -> new RuntimeException("Blood inventory not found"));

        // check available units
        if (inventory.getQuantity() < request.getUnitsRequired()) {
            throw new RuntimeException("Not enough blood units available");
        }

        // reduce inventory units
        inventory.setQuantity(inventory.getQuantity() - request.getUnitsRequired());

        inventoryRepository.save(inventory);

        // update request status
        request.setStatus(RequestStatus.COMPLETED);

        return bloodRequestRepository.save(request);
    }

    @Override
    public List<BloodRequest> getRequestsByStatus(RequestStatus status) {
        if (status == null) {
            throw new RuntimeException("Status cannot be null");
        }
        return bloodRequestRepository.findByStatus(status);
    }

    @Override
    public List<BloodRequest> getRequestsByBloodGroup(String bloodGroup) {
        if (bloodGroup == null || bloodGroup.isEmpty()) {
            throw new RuntimeException("Blood group cannot be null or empty");
        }

        BloodGroup group = BloodGroup.from(bloodGroup);

        return bloodRequestRepository.findByBloodGroup(group);
    }
}
