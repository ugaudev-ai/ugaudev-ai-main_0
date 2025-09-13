package com.invoice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.invoice.entity.Invoice;

public interface InvoiceRepository extends CrudRepository<Invoice, Long>{

	Optional<Invoice> findByInvoiceNumber(String invoiceNumber);

	List<Invoice> findAll(Pageable pageable);

	Page<Invoice> findByStatusNot(String string, Pageable pageable);

}
