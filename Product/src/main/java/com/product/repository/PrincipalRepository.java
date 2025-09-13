package com.product.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.product.entity.Principal;

public interface PrincipalRepository extends CrudRepository<Principal, Long> {

	void save(Long id);

	List<Principal> findAll(Pageable pageable);

	Optional<Principal> findByPrincipalName(String principalName);

	Optional<Principal> findById(Long id);

	List<Principal> findByIdIn(List<Long> principalIds);

	Page<Principal> findByStatusNot(String string, Pageable pageable);


}

