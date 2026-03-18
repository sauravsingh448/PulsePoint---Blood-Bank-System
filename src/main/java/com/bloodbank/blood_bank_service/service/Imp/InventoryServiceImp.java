package com.bloodbank.blood_bank_service.service.Imp;

import com.bloodbank.blood_bank_service.entity.BloodBank;
import com.bloodbank.blood_bank_service.entity.Inventory;
import com.bloodbank.blood_bank_service.enums.BloodGroup;
import com.bloodbank.blood_bank_service.repository.BloodBankRepository;
import com.bloodbank.blood_bank_service.repository.InventoryRepository;
import com.bloodbank.blood_bank_service.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryServiceImp implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final BloodBankRepository bloodBankRepository;

    // Add Inventory
    @Override
    public Inventory createInventory(Long bloodBankId, Inventory inventory) throws Exception {

        //check blood bank exists
        BloodBank bloodBank = bloodBankRepository.findById(bloodBankId)
                .orElseThrow(() ->
                        new RuntimeException("Blood bank not found with id: " + bloodBankId));

        // blood group null check (ENUM now)
        if (inventory.getBloodGroup() == null) {
            throw new RuntimeException("Blood group is required");
        }

        // quantity validation
        if (inventory.getQuantity() == null || inventory.getQuantity() <= 0) {
            throw new RuntimeException("Blood quantity must be greater than 0");
        }

        // expiry date validation
        if (inventory.getExpiryDate() == null ||
                inventory.getExpiryDate().isBefore(LocalDate.now())) {
            throw new RuntimeException("Invalid or expired blood");
        }
        // check existing inventory
        Inventory existingInventory =
                inventoryRepository.findByBloodBankIdAndBloodGroupAndExpiryDate(
                        bloodBankId,
                        inventory.getBloodGroup(),
                        inventory.getExpiryDate()
                );

        // If exists → update quantity
        if (existingInventory != null) {
            existingInventory.setQuantity(
                    existingInventory.getQuantity() + inventory.getQuantity()
            );
            return inventoryRepository.save(existingInventory);
        }

        // create new inventory
        Inventory newInventory = new Inventory();
        newInventory.setBloodGroup(inventory.getBloodGroup()); // ENUM direct
        newInventory.setQuantity(inventory.getQuantity());
        newInventory.setExpiryDate(inventory.getExpiryDate());
        newInventory.setBloodComponent(inventory.getBloodComponent());
        newInventory.setBloodBank(bloodBank);

        return inventoryRepository.save(newInventory);
    }

    // update Inventory
    @Override
    public Inventory updateInventory(Long inventoryId, Integer quantity) throws Exception {
        // Fetch inventory from database using inventory ID
        Inventory inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(()-> new RuntimeException("Inventory not found with id: "+ inventoryId));
        //validate quantity1
        // Quantity must not be null and cannot be negative
        if(quantity == null || quantity <= 0){
            throw new RuntimeException("Quantity must be 0 or greater");
        }
        // update quantity
        // Ex = old quantity = 10, new quantity = 7
        inventory.setQuantity(quantity);
        // save update inventory back to database
        return inventoryRepository.save(inventory);
    }

    @Override
    public void deleteInventory(Long inventoryId) throws Exception {
      // find Inventory existing or not
        Inventory inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new RuntimeException("Inventory not found with inventoryId"+ inventoryId));
        inventoryRepository.delete(inventory);
    }

    @Override
    public List<Inventory> getInventoryByBloodBank(Long bloodBankId) throws Exception {
       List<Inventory> inventories =  inventoryRepository.findByBloodBankId(bloodBankId);
       if(inventories.isEmpty()){
           throw new RuntimeException("No inventory found for bloodBankId: " + bloodBankId);
       }
       return inventories;
    }

    // SEARCH BLOOD
    // (FINDS AVAILABLE BLOOD BASED ON BLOOD GROUP AND CITY)
    @Override
    public List<Inventory> searchBlood(String bloodGroup, String city) throws Exception {

        // validate blood group
        if (bloodGroup == null || bloodGroup.trim().isEmpty()) {
            throw new RuntimeException("Blood group is required");
        }

        // validate city
        if (city == null || city.trim().isEmpty()) {
            throw new RuntimeException("City is required");
        }

        // normalize input (VERY IMPORTANT)
        String normalizedGroup = bloodGroup.trim().toUpperCase().replace(" ", "");

        // Convert String → ENUM safely
        BloodGroup groupEnum;
        try {
            normalizedGroup = normalizedGroup
                    .replace("+", "_POS")
                    .replace("-", "_NEG");

            groupEnum = BloodGroup.valueOf(normalizedGroup);

        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid blood group: " + bloodGroup);
        }

        // fetch from DB
        List<Inventory> inventories =
                inventoryRepository.findByBloodGroupAndBloodBankCityIgnoreCase(
                        groupEnum,
                        city.trim()
                );
        // no data found
        if (inventories.isEmpty()) {
            throw new RuntimeException(
                    "No blood available for " + bloodGroup + " in " + city
            );
        }
        return inventories;
    }
}