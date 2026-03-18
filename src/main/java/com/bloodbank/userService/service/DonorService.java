package com.bloodbank.userService.service;

import com.bloodbank.userService.entity.User;

public interface DonorService {

    //  Get logged-in donor profile using email from Authentication
    public User findUserByEmail(String email) throws Exception;

    //  Update logged-in donor profile
    public User updateDonorProfile(String email, User updateUser) throws Exception;

    // delete donor profile
    public User deleteDonorProfile(Long donorId) throws Exception;

    public User getDonorById(Long donorId) throws Exception;
}
