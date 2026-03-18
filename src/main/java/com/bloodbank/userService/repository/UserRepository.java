package com.bloodbank.userService.repository;

import com.bloodbank.userService.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    public User findByEmail(String email);

    Optional<User> findByResetToken(String token);

    // for login with phone number
    User findByContact(String contact);
}
