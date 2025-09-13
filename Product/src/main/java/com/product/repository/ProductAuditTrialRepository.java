package com.product.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.product.entity.ProductAuditTrial;

public interface ProductAuditTrialRepository extends CrudRepository<ProductAuditTrial, Long>{

	List<ProductAuditTrial> findAll(Pageable pageable);

}
