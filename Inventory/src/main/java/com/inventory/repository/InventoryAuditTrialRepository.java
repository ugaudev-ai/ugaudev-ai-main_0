package com.inventory.repository;

import org.springframework.data.repository.CrudRepository;

import com.inventory.entity.InventoryAuditTrial;

public interface InventoryAuditTrialRepository extends CrudRepository<InventoryAuditTrial, Long>{

}
