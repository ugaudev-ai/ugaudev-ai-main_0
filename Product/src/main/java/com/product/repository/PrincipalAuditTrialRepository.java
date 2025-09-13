package com.product.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.product.entity.PrincipalAuditTrial;

public interface PrincipalAuditTrialRepository extends CrudRepository<PrincipalAuditTrial, Long> {

	List<PrincipalAuditTrial> findAll(Pageable pageable);

}
