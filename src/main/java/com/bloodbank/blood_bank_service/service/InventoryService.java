package com.bloodbank.blood_bank_service.service;

import com.bloodbank.blood_bank_service.entity.Inventory;

import java.util.List;

public interface InventoryService {

    public Inventory createInventory(Long bloodBankId, Inventory inventory) throws Exception;
    public Inventory updateInventory(Long inventoryId, Integer quantity) throws Exception;
    public void deleteInventory(Long inventoryId) throws Exception;
    public List<Inventory> getInventoryByBloodBank(Long bloodBankId) throws Exception;
    public List<Inventory> searchBlood(String bloodGroup, String city) throws Exception;

}
