package com.bloodbank.blood_bank_service.controller;

import com.bloodbank.blood_bank_service.dto.BloodBankDto;
import com.bloodbank.blood_bank_service.entity.BloodBank;
import com.bloodbank.blood_bank_service.service.BloodBankService;
import com.bloodbank.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/admin/bloodBanks")
@RequiredArgsConstructor
public class AdminBloodBankController {

    private final BloodBankService bloodBankService;

    // create Blood Bank (ADMIN ONLY)
    @PostMapping("/create")
    public ResponseEntity<BloodBank> createBloodBank(@RequestBody BloodBankDto bloodBank
    ) throws Exception{
        // Get the currently logged-in user's ID from SecurityContext
        Long userId = SecurityUtils.getCurrentUserId();
        BloodBank bloodBank1 = bloodBankService.createBloodBank(bloodBank, userId);
        return new ResponseEntity<>(bloodBank1, HttpStatus.CREATED);
    }

    // update Blood Bank
    @PutMapping("/{id}/update")
    public ResponseEntity<BloodBank> updateBloodBank(@PathVariable Long id,
                                           @RequestBody BloodBank bloodBank)throws Exception{
        BloodBank bloodBank1 = bloodBankService.updateBloodBank(id, bloodBank);
        return new ResponseEntity<>(bloodBank1, HttpStatus.OK);
    }

    //delete bloodBank by id
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<String> deleteBloodBank(
            @PathVariable Long id
    ) throws Exception{
        bloodBankService.deleteBloodBank(id);
        return new ResponseEntity<>("Blood bank deleted successfully", HttpStatus.OK);
    }

    // Get All blood banks
    // Any authenticated user
    @GetMapping()
    public ResponseEntity<List<BloodBank>> getAllBloodBanks() throws Exception{
        List<BloodBank> banks = bloodBankService.getAllBloodBanks();
        return new ResponseEntity<>(banks, HttpStatus.OK);
    }

    // Get Blood Bank By ID
    @GetMapping("/{id}")
    public ResponseEntity<BloodBank> getBloodBankById(
            @PathVariable Long id
    ) throws Exception{
        BloodBank bloodBank = bloodBankService.getBloodBankById(id);
        return new ResponseEntity<>(bloodBank, HttpStatus.OK);
    }

    //SEARCH Blood Bank By CITY
    @GetMapping("/city/{city}")
    public ResponseEntity<List<BloodBank>> getBloodBanksByCity(
            @PathVariable String city
    ) throws Exception{
        List<BloodBank> banks = bloodBankService.getBloodBanksByCity(city);
        return new ResponseEntity<>(banks, HttpStatus.OK);
    }

    @GetMapping("/admin/{admin-Id}")
    public ResponseEntity<List<BloodBank>> getBloodBankByUserId(
    ) throws Exception{
        Long adminId = SecurityUtils.getCurrentUserId();
        List<BloodBank> bloodBanks = bloodBankService.getBloodBanksByAdminId(adminId);
        return new ResponseEntity<>(bloodBanks, HttpStatus.OK);
    }
}
