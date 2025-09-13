package com.enquiry.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.enquiry.entity.Enquiry;

public interface EnquiryRepository extends CrudRepository<Enquiry, Long>{

	Optional<Enquiry> findById(Long id);

	List<Enquiry> findAll(Pageable pageable);

	Optional<Enquiry> findByCustomerName(String customerName);
}
