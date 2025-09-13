package com.customer.repository;


import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.customer.entity.Customer;
import com.customer.entity.CustomerAuditTrial;



public interface CustomerAuditTrialRepository  extends CrudRepository<CustomerAuditTrial, Long> {

	List<CustomerAuditTrial> findAll(Pageable pageable);

	Customer deleteById(Customer custIn);

}