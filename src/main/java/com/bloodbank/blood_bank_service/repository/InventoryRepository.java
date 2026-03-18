package com.bloodbank.blood_bank_service.repository;

import com.bloodbank.blood_bank_service.entity.Inventory;
import com.bloodbank.blood_bank_service.enums.BloodGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    // Check existing inventory for same blood bank & group
    Inventory findByBloodBankIdAndBloodGroupAndExpiryDate(
            Long bloodBankId,
            BloodGroup bloodGroup,
            LocalDate expiryDate
    );
    // find inventory by blood group + city
    List<Inventory> findByBloodGroupAndBloodBankCityIgnoreCase(
            BloodGroup bloodGroup, String city);

    // Get all Inventory from a Blood Bank
    List<Inventory> findByBloodBankId(Long bloodBankId);
    Optional<Inventory> findByBloodBankIdAndBloodGroup(Long bloodBankId, BloodGroup bloodGroup);
}
