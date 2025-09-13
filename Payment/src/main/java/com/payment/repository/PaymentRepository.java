package com.payment.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.payment.entity.Payment;

public interface PaymentRepository extends CrudRepository<Payment, Long>{

	List<Payment> findAll(Pageable pageable);
	Page<Payment> findByStatusNot(String status, Pageable pageable);
}
