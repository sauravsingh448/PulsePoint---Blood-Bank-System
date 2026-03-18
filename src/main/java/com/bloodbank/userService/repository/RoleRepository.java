package com.bloodbank.userService.repository;

import com.bloodbank.userService.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    // Find role by name, e.g., ADMIN, DONOR, RECIPIENT
    Optional<Role> findByName(String name);

}
