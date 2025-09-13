package com.customer.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.customer.entity.Customer;
import com.customer.request.CustomerReq;

public interface CustomerRepository  extends CrudRepository<Customer, Long> {

	List<Customer> findAll(Pageable pageable);

	Optional<Customer> findByName(String name);

	Customer save(CustomerReq req);

	Page<Customer> findByStatusNot(String string, Pageable pageable);

}