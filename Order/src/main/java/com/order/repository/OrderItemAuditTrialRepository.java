package com.order.repository;

import org.springframework.data.repository.CrudRepository;

import com.order.entity.OrderItemAuditTrial;

public interface OrderItemAuditTrialRepository extends CrudRepository<OrderItemAuditTrial, Long>{

}
