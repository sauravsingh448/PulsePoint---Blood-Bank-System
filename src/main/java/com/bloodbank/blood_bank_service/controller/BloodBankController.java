package com.bloodbank.blood_bank_service.controller;

import com.bloodbank.blood_bank_service.entity.BloodBank;
import com.bloodbank.blood_bank_service.service.BloodBankService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bloodBanks")
@RequiredArgsConstructor
public class BloodBankController {
    private  final BloodBankService bloodBankService;

    // search blood bank with Blood-Bank name
    @GetMapping("/search")
    public ResponseEntity<List<BloodBank>> searchBloodBank(
            @RequestParam String keyword
    ) throws Exception{
        List<BloodBank> bloodBanks = bloodBankService.searchBloodBank(keyword);
        return new ResponseEntity<>(bloodBanks, HttpStatus.OK);
    }

    // Get All blood banks
    // Any authenticated user
    @GetMapping()
    public ResponseEntity<List<BloodBank>> getAllBloodBanks() throws Exception{
        List<BloodBank> banks = bloodBankService.getAllBloodBanks();
        return new ResponseEntity<>(banks, HttpStatus.OK);
    }

    //SEARCH Blood Bank By CITY
    @GetMapping("/city/{city}")
    public ResponseEntity<List<BloodBank>> getBloodBanksByCity(
            @PathVariable String city
    ) throws Exception{
        List<BloodBank> banks = bloodBankService.getBloodBanksByCity(city);
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
}
