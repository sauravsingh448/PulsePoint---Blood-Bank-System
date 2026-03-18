package com.bloodbank.blood_bank_service.repository;

import com.bloodbank.blood_bank_service.entity.BloodBank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

//BloodBankRepository provides CRUD operations for BloodBank

@Repository
public interface BloodBankRepository extends JpaRepository<BloodBank, Long> {

    // find blood banks by city/location
    List<BloodBank> findByCity(String city);

    // Search blood banks with name
    List<BloodBank> findByNameContainingIgnoreCase(String name);

    boolean existsByNameAndCityAndIdNot(String name, String city, Long id);
    // for search BloodBank by UserId
    List<BloodBank> findByAdminUserId(Long adminUserId);

}
