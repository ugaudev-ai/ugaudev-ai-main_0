package com.invoice.repository;

import org.springframework.data.repository.CrudRepository;

import com.invoice.entity.InvoiceAuditTrial;

public interface InvoiceAuditTrialRepository extends CrudRepository<InvoiceAuditTrial, Long>{

}
