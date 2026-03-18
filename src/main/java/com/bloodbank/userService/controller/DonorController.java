package com.bloodbank.userService.controller;

import com.bloodbank.userService.entity.User;
import com.bloodbank.userService.service.DonorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/donor")
public class DonorController {

    @Autowired
    private DonorService donorService;

    // profile view
    @GetMapping("/profile")
    public ResponseEntity<User> getDonorProfile(Authentication authentication) throws Exception {

        if (authentication == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        String email = authentication.getName();
        User user = donorService.findUserByEmail(email);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    // update own profile
    @PutMapping("/profile/update")
    public ResponseEntity<User> updateDonorProfile(
            Authentication authentication,
            @RequestBody User updateUser) throws Exception {

        if (authentication == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        String email = authentication.getName();
        User user = donorService.updateDonorProfile(email, updateUser);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping("/{donorId}")
    public ResponseEntity<String> deleteDonorProfile(
            @PathVariable Long donorId
    ) throws Exception{
        donorService.deleteDonorProfile(donorId);
        return new ResponseEntity<>("Donor deleted successfully", HttpStatus.OK);
    }
}
