package com.inventory.service;

import java.util.Date;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inventory.entity.Inventory;
import com.inventory.entity.InventoryAuditTrial;
import com.inventory.repository.InventoryAuditTrialRepository;
import com.inventory.repository.InventoryRepository;
import com.inventory.request.InventoryReq;

@Service
public class InventoryService {

	@Autowired private InventoryRepository inventoryRepo;
	@Autowired private InventoryAuditTrialRepository inventoryATRepo;

	//Checking the dbInventory by Id/InventoryCode given by RequestBody
	public Inventory dbInv(InventoryReq req) {
		Inventory inv = null;
		if(req.getId()	!= null) {
			inv = inventoryRepo.findById(req.getId()).orElseThrow(() -> new RuntimeException("Enquiry not found with id: " + req.getId()));
		}
		return inv;
	}
	
	public Inventory createInv(InventoryReq req) {
		Inventory inventory = new Inventory();
		BeanUtils.copyProperties(req, inventory);
		inventory.setStatus("N");
		inventory.setCreatedAt(new Date());
		return inventoryRepo.save(inventory);
	}

	//Get Inventory by dbInventory by Id/InventoryCode
	public Inventory getInventory(InventoryReq req) {
		Inventory existing = dbInv(req);
		if("D".equals(existing.getStatus())) {
			throw new RuntimeException("Inventory not found with Id");
		}else {
		return existing;
		}
	}

	//updating Inventory
	public Inventory updateInventory(InventoryReq req) {
		Inventory existing = dbInv(req); 

		if("D".equals(existing.getStatus())) {
			throw new RuntimeException("Inventory not found with Id");
		}else {
		saveInvAT(existing);

		if (req.getId() != null) existing.setId(req.getId());
		if (req.getLocation() != null) existing.setLocation(req.getLocation());
//		if (req.getProduct() != null) existing.setProduct(req.getProduct());
		if (req.getProduct() != null) existing.setProduct(req.getProduct());
		if (req.getQuantityAvailable() != null) existing.setQuantityAvailable(req.getQuantityAvailable());
		if (req.getQuantityReserved() != null) existing.setQuantityReserved(req.getQuantityReserved());
		if (req.getReorderLevel() != null) existing.setReorderLevel(req.getReorderLevel());

		Inventory updatedInventory = inventoryRepo.save(existing);

		return updatedInventory;
		}
	}
	
	//Deleting Enquiry
		public Inventory deleteInventory(InventoryReq req) {
			Inventory existing = dbInv(req);
			if("D".equals(existing.getStatus())) {
				throw new RuntimeException("Inventory not found with Id");
			}else {
			saveInvAT(existing);
			existing.setStatus("D");
			Inventory deletedInv = inventoryRepo.save(existing);
			return deletedInv;
			}

		}

		//InventoryAuditTrail
		private void saveInvAT(Inventory inv) {
			InventoryAuditTrial invAT = new InventoryAuditTrial();
			BeanUtils.copyProperties(inv, invAT);
			inventoryATRepo.save(invAT);
		}

}
