package com.bloodbank.donationService.repository;

import com.bloodbank.donationService.entity.CampRegistration;
import com.bloodbank.donationService.enum1.RegistrationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CampRegistrationRepository extends JpaRepository<CampRegistration, Long> {

    boolean existsByCamp_IdAndDonorId(Long campId, Long donorId);

    List<CampRegistration> findByCamp_Id(Long campId);

    List<CampRegistration> findByDonorId(Long donorId);

    List<CampRegistration> findByCamp_IdAndStatus(Long campId, RegistrationStatus status);

    long countByCamp_Id(Long campId);
    long countByCamp_IdAndStatus(Long campId, RegistrationStatus status);
}
