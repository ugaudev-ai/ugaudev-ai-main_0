package com.order.repository;

import org.springframework.data.repository.CrudRepository;

import com.order.entity.OrderItem;
import com.order.request.OrderItemReq;

public interface OrderItemRepository extends CrudRepository<OrderItem, Long>{

	OrderItem save(OrderItemReq req);

}
