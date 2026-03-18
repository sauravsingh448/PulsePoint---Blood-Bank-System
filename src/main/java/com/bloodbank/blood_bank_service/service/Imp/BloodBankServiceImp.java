package com.bloodbank.blood_bank_service.service.Imp;

import com.bloodbank.blood_bank_service.dto.BloodBankDto;
import com.bloodbank.blood_bank_service.entity.BloodBank;
import com.bloodbank.blood_bank_service.repository.BloodBankRepository;
import com.bloodbank.blood_bank_service.service.BloodBankService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor // Lombok constructor
public class BloodBankServiceImp implements BloodBankService {


    private final BloodBankRepository bloodBankRepository;

    @Override
    public BloodBank createBloodBank(BloodBankDto req, Long adminId) throws Exception {
        if(req == null){
            throw new RuntimeException("BloodBank object cannot be null");
        }

        // Admin userId
        if(adminId == null){
            throw new IllegalArgumentException("Admin user ID is required");
        }
        // create new BloodBank object
        BloodBank bloodBank = new BloodBank();
        // set value one by one
        bloodBank.setName(req.getName());
        bloodBank.setCity(req.getCity());
        bloodBank.setAddress(req.getAddress());
        bloodBank.setContact(req.getContact());
        bloodBank.setLatitude(req.getLatitude());
        bloodBank.setLongitude(req.getLongitude());
        // Link blood bank with admin
        bloodBank.setAdminUserId(adminId);
        //save data into Database
        return bloodBankRepository.save(bloodBank);
    }

    @Override
    public BloodBank updateBloodBank(Long bloodBankId, BloodBank update) throws Exception {
        // fetch Existing Blood Bank
        BloodBank existingBank = bloodBankRepository.findById(bloodBankId)
                .orElseThrow(() -> new RuntimeException("Blood Bank not fount"));

        // Check duplicate name, city (ignore same record)
        boolean alreadyExits = bloodBankRepository
                .existsByNameAndCityAndIdNot(update.getName(), update.getCity(), bloodBankId);
        if(alreadyExits){
            throw new RuntimeException("Blood bank already exits in this city" + update.getName());
        }
        // update allowed fields
        existingBank.setName(update.getName());
        existingBank.setCity(update.getCity());
        existingBank.setAddress(update.getAddress());
        existingBank.setContact(update.getContact());
        existingBank.setLatitude(update.getLatitude());
        existingBank.setLongitude(update.getLongitude());
        // save update blood bank
        return bloodBankRepository.save(existingBank);
    }

    @Override
    public void deleteBloodBank(Long bloodBankId) throws Exception {
       // check if blood bank exits or not
        BloodBank existingBank = bloodBankRepository.findById(bloodBankId)
                .orElseThrow(() -> new RuntimeException("Blood Bank not found" + bloodBankId));
        // when delete bloodBank
        // This will also delete inventory records because of:
        // cascade = CascadeType.ALL + orphanRemoval = true
        bloodBankRepository.delete(existingBank);
    }

    @Override
    public List<BloodBank> getAllBloodBanks() throws Exception{
        // fetch all bloodBanks
        return bloodBankRepository.findAll();
    }

    @Override
    public BloodBank getBloodBankById(Long bloodBankById) throws Exception {
        // fetch Blood bank by id
        Optional<BloodBank> existingBloodBank = bloodBankRepository.findById(bloodBankById);

        if(existingBloodBank.isEmpty()){
          throw new RuntimeException("Blood bank not found with id: "+ bloodBankById);
        }
       return existingBloodBank.get();
    }

    @Override
    public List<BloodBank> getBloodBanksByCity(String city) throws Exception{
        // fetch blood banks by city name
        List<BloodBank> bloodBanks = bloodBankRepository.findByCity(city);

        //if no blood banks found return empty list
        if(bloodBanks.isEmpty()){
            throw new RuntimeException("No blood Banks found in city: "+ city);
        }
        return bloodBanks;
    }

    @Override
    public List<BloodBank> getBloodBanksByAdminId(Long adminId) throws Exception {
        List<BloodBank> bloodBanks = bloodBankRepository.findByAdminUserId(adminId);
        if (bloodBanks.isEmpty()) {
            throw new RuntimeException("No BloodBanks found with AdminId: " + adminId);
        }
        return bloodBanks;
    }
    @Override
    public List<BloodBank> searchBloodBank(String keyword) throws Exception {
        return bloodBankRepository.findByNameContainingIgnoreCase(keyword);
    }
}
