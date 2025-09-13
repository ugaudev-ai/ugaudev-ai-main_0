package com.payment.repository;

import org.springframework.data.repository.CrudRepository;

import com.payment.entity.PaymentAuditTrial;

public interface PaymentAuditTrialRepository extends CrudRepository<PaymentAuditTrial, Long>{

}
