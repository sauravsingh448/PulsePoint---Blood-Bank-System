package com.bloodbank.donationService.repository;

import com.bloodbank.donationService.enum1.CampStatus;
import com.bloodbank.donationService.entity.DonationCamp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/*
 * Repository for DonationCamp
 * Handles all dataBase operations
 */
@Repository
public interface DonationCampRepository extends JpaRepository<DonationCamp, Long> {
    // find all ACTIVE camps
    List<DonationCamp> findByStatus(CampStatus status);

    // find upcoming ACTIVE camps
    List<DonationCamp> findByStatusAndCampDateGreaterThanEqual(
            CampStatus status,
            LocalDate date
    );
    // find by camps Name And Active (Because User should not see CLOSED/UPCOMING camps)
    List<DonationCamp> findByCampNameContainingIgnoreCaseAndStatus(String campName, CampStatus active);

    // find the camps by city name
    @Query("SELECT c FROM DonationCamp c WHERE LOWER(c.address) LIKE LOWER(CONCAT('%', :city, '%'))")
    List<DonationCamp> findByCityInAddress(@Param("city") String city);


    @Query(value = """
SELECT c.*
FROM donation_camps c
WHERE
(
    6371 * acos(
        cos(radians(:userLat)) * cos(radians(c.latitude)) *
        cos(radians(c.longitude) - radians(:userLon)) +
        sin(radians(:userLat)) * sin(radians(c.latitude))
    )
) <= :radius
AND c.status = :status
ORDER BY
(
    6371 * acos(
        cos(radians(:userLat)) * cos(radians(c.latitude)) *
        cos(radians(c.longitude) - radians(:userLon)) +
        sin(radians(:userLat)) * sin(radians(c.latitude))
    )
) ASC
""", nativeQuery = true)
    List<DonationCamp> searchNearbyCamps(
            @Param("userLat") double userLat,
            @Param("userLon") double userLon,
            @Param("radius") double radius,
            @Param("status") CampStatus status
    );

}