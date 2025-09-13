package com.order.repository;

import org.springframework.data.repository.CrudRepository;

import com.order.entity.OrderAuditTrial;

public interface OrderAuditTrialRepository extends CrudRepository<OrderAuditTrial, Long> {

}
