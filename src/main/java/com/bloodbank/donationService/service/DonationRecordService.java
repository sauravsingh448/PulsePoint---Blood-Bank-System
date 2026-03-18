package com.bloodbank.donationService.service;

import com.bloodbank.donationService.entity.DonationRecord;

import java.util.List;

public interface DonationRecordService {
    // create donation record
    DonationRecord createDonationRecord(DonationRecord record) throws Exception;

    // donor donation history
    List<DonationRecord> getDonorDonationHistory(Long donorId) throws Exception;

    // donations of a camp
    List<DonationRecord> getCampDonations(Long campId) throws Exception;
}
