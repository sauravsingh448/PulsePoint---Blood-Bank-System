package com.bloodbank.blood_bank_service.service;

import com.bloodbank.blood_bank_service.dto.BloodBankDto;
import com.bloodbank.blood_bank_service.entity.BloodBank;

import java.util.List;

public interface BloodBankService {
    // Add new blood bank
    public BloodBank createBloodBank(BloodBankDto bloodBank, Long adminID) throws Exception;
    // update bloodBank
    public BloodBank updateBloodBank(Long bloodBankId, BloodBank update) throws Exception;
    // delete Blood Bank
    public void deleteBloodBank(Long bloodBankId) throws Exception;

    // get All blood Banks
    public List<BloodBank> getAllBloodBanks() throws Exception ;

    // get blood bank by id
    public BloodBank getBloodBankById(Long bloodBankById) throws Exception;

    // Search blood banks by city
    public List<BloodBank> getBloodBanksByCity(String City) throws Exception;

    // search blood bank by adminId
    public List<BloodBank> getBloodBanksByAdminId(Long adminId) throws Exception;

    // search blood Bank with Name
    public List<BloodBank> searchBloodBank(String keyword) throws Exception;
}
