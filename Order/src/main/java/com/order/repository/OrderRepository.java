package com.order.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.order.entity.Order;
import com.order.request.OrderReq;

public interface OrderRepository extends CrudRepository<Order, Long>{

	List<Order> findAll(Pageable pageable);

	Order save(OrderReq req);

	Page<Order> findByStatusNot(String string, Pageable pageable);

}
