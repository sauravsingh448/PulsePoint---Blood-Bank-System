package com.bloodbank.donationService.repository;

import com.bloodbank.donationService.entity.DonationRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DonationRecordRepository extends JpaRepository<DonationRecord, Long> {
    // donor donation history
    List<DonationRecord> findByDonorId(Long donorId);

    // donations in a camp(Admin, Camp me kitne donors ne blood donate kiya)
    List<DonationRecord> findByCampId(Long campId);
}
