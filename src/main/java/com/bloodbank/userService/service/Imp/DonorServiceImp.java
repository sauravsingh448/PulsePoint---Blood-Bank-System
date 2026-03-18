package com.bloodbank.userService.service.Imp;

import com.bloodbank.userService.entity.User;
import com.bloodbank.userService.repository.UserRepository;
import com.bloodbank.userService.service.DonorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DonorServiceImp implements DonorService {

    @Autowired
    private UserRepository userRepository;

    //  view own profile (email comes from Authentication)
    @Override
    public User findUserByEmail(String email) {

        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new RuntimeException("User not found");
        }
        return user;
    }

    //  update donor profile
    @Override
    public User updateDonorProfile(String email, User updateUser) {

        User existingUser = userRepository.findByEmail(email);

        if (existingUser == null) {
            throw new RuntimeException("User not found");
        }

        // update allowed fields only (if value is provided.)
        if(updateUser.getName() != null){
            existingUser.setName(updateUser.getName());
        }
        if(updateUser.getContact() != null){
            existingUser.setContact(updateUser.getContact());
        }
        if(updateUser.getAddress() != null){
            existingUser.setAddress(updateUser.getAddress());
        }
        if(updateUser.getBloodGroup() != null){
            existingUser.setBloodGroup(updateUser.getBloodGroup());
        }
        return userRepository.save(existingUser);
    }

    @Override
    public User deleteDonorProfile(Long donorId) throws Exception {
          User existing = userRepository.findById(donorId)
                  .orElseThrow(() -> new RuntimeException("Donor not found "+ donorId));
          userRepository.delete(existing);
        return existing;
    }

    @Override
    public User getDonorById(Long donorId) throws Exception {
        User donor = userRepository.findById(donorId)
                .orElseThrow(() -> new Exception("Donor not found with id : " + donorId));

        return donor;
    }
}
