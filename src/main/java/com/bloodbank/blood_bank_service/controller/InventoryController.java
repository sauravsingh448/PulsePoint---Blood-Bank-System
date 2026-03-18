package com.bloodbank.blood_bank_service.controller;

import com.bloodbank.blood_bank_service.entity.Inventory;
import com.bloodbank.blood_bank_service.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    // Add Inventory by(Admin)
    @PostMapping("/{bloodBankId}")
    public ResponseEntity<Inventory> addInventory(
            @PathVariable Long bloodBankId,
            @RequestBody Inventory inventory
    ) throws Exception{
        Inventory inventory1 = inventoryService.createInventory(bloodBankId, inventory);
        return new ResponseEntity<>(inventory1, HttpStatus.OK);
    }

    // Update Inventory by Admin
    @PutMapping("/{id}")
    public ResponseEntity<Inventory> updateInventory(
            @PathVariable Long id,
            @RequestBody Inventory inventory
    ) throws Exception{
       Inventory inventory1 = inventoryService.updateInventory(id, inventory.getQuantity());
       return new ResponseEntity<>(inventory1, HttpStatus.OK);
    }

    // delete inventory by Admin
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<String> deleteInventory(
            @PathVariable Long id
    ) throws Exception{
        inventoryService.deleteInventory(id);
        return new ResponseEntity<>("Inventory deleted successfully", HttpStatus.OK);
    }

    // view Inventory by Admin
    @GetMapping("{bloodBankId}")
    public ResponseEntity<List<Inventory>> getInventoryByBloodBank(
            @PathVariable Long bloodBankId
    ) throws Exception{
        List<Inventory> inventories = inventoryService.getInventoryByBloodBank(bloodBankId);
        return new ResponseEntity<>(inventories, HttpStatus.OK);
    }

    // RECIPIENT + ADMIN — Search blood
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN','RECIPIENT')")
    public ResponseEntity<List<Inventory>> searchBlood(
            @RequestParam String bloodGroup,
            @RequestParam String city
    ) throws Exception{
       return ResponseEntity.ok(inventoryService.searchBlood(bloodGroup, city));
    }
}
