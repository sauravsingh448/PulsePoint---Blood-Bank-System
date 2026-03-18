package com.bloodbank.donationService.service.Imp;

import com.bloodbank.donationService.entity.DonationRecord;
import com.bloodbank.donationService.repository.DonationRecordRepository;
import com.bloodbank.donationService.service.DonationRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DonationRecordServiceImpl implements DonationRecordService {

    private final DonationRecordRepository donationRecordRepository;

    @Override
    // create donation record
    public DonationRecord createDonationRecord(DonationRecord record) throws Exception{
        return donationRecordRepository.save(record);
    }

    // donor donation history
    @Override
    public List<DonationRecord> getDonorDonationHistory(Long donorId) throws Exception{
        if (donorId == null) {
            throw new IllegalArgumentException("Donor id cannot be null");
        }
        List<DonationRecord> donationRecords = donationRecordRepository.findByDonorId(donorId);
        if (donationRecords.isEmpty()) {
            throw new RuntimeException("Donation history not found for donor id: " + donorId);
        }
        return donationRecords;
    }

    // camp donations
    @Override
    public List<DonationRecord> getCampDonations(Long campId) throws Exception{
        if(campId == null){
            throw new IllegalArgumentException("Camp id cannot be null: ");
        }
        List<DonationRecord> donationRecords = donationRecordRepository.findByCampId(campId);
        if(donationRecords.isEmpty()){
            throw new RuntimeException("No donation records found for camp id: " + campId);
        }
        return donationRecords;
    }
}
