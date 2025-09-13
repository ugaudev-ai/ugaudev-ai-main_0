package com.admin.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.admin.entity.Admin;

public interface AdminRepository extends CrudRepository<Admin, Long>{

	Page<Admin> findAll(Pageable pageable);
	Page<Admin> findByStatusNot(String status, Pageable pageable);
	
	boolean existsByUsername(String username);
	
}
