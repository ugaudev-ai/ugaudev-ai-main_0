package com.customer.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.customer.entity.Address;
import com.customer.entity.AddressAuditTrial;

public interface AddressRepository extends CrudRepository<Address, Long> {

	void save(AddressAuditTrial addAT);

	Optional<Address> findByAddressType(String addressType);


}
