package com.inventory.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.inventory.entity.Inventory;

public interface InventoryRepository extends CrudRepository<Inventory, Long>{

	List<Inventory> findAll(Pageable pageable);

	Page<Inventory> findByStatusNot(String string, Pageable pageable);

}
