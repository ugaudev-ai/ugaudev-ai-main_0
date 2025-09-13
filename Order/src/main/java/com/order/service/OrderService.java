package com.order.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.order.entity.Order;
import com.order.entity.OrderAuditTrial;
import com.order.entity.OrderItem;
import com.order.entity.OrderItemAuditTrial;
import com.order.repository.OrderAuditTrialRepository;
import com.order.repository.OrderItemAuditTrialRepository;
import com.order.repository.OrderItemRepository;
import com.order.repository.OrderRepository;
import com.order.request.OrderItemReq;
import com.order.request.OrderReq;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

@Service
public class OrderService {

	@Autowired private OrderRepository orderRepo;
	@Autowired private OrderItemRepository orderItemRepo;
	@Autowired private OrderAuditTrialRepository orderATRepo;
	@Autowired private OrderItemAuditTrialRepository orderItemATRepo;

	//Checking the dbOrder by Id given by RequestBody
	public Order dbOrder(OrderReq req) {
		Order existing = null;
		if(req.getId()	!= null) {
			existing = orderRepo.findById(req.getId()).orElseThrow(() -> new RuntimeException("Enquiry not found with id: " + req.getId()));
		}
		return existing;
	}
	
//	private static final String PRODUCT_API_BASE = "http://localhost:8080/api/master";
	
	public Order saveOrder(OrderReq req) throws JsonProcessingException {
        Order order = new Order();
        BeanUtils.copyProperties(req, order);
        Order ord = orderRepo.save(order);

        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderItemReq itemDTO : req.getOrderItems()) {
//        	String productUrl = PRODUCT_API_BASE + "/" + itemDTO.getProduct();
//            ProductDTO product = restTemplate.getForObject(productUrl, ProductDTO.class);
            
            OrderItem item = new OrderItem();
            HttpResponse<String> response = Unirest.post("http://localhost:8080/api/master/map/" + itemDTO.getProduct())
                    .header("Content-Type", "application/json")
                    .body("{\"id\":" + itemDTO.getProduct() + "}")
                    .asString();

            if (response.getStatus() != 200) {
                throw new RuntimeException("Failed to fetch product map with ID " + itemDTO.getProduct() + ". Status: " + response.getStatus());
            }

            String product = response.getBody();
//            ObjectMapper mapper = new ObjectMapper();
//            String ProductJson = mapper.writeValueAsString(product);
//			item.setProductJson(ProductJson);
			
//            item.setProductName(product.getProductName());
            item.setProduct(product);
            item.setQuantity(itemDTO.getQuantity());
            item.setPrice(itemDTO.getPrice()); // assuming price exists in Product
            item.setOrder(order); // set back reference
            orderItems.add(item);
        }
        ord.setTotalAmount(req.getTotalAmount());
        ord.setNetAmount(req.getNetAmount());
        ord.getOrderItems().addAll(orderItems);
        ord.setStatus("N");
        ord.setCreatedAt(new Date());

        return orderRepo.save(ord);
    }
	
	//Get Order by dbOrder by Id
	public Order getOrder(OrderReq req) {
		Order existing = dbOrder(req);
		if("D".equals(existing.getStatus())) {
			throw new RuntimeException("Order not found with Id");
		}else {
		return existing;
		}
	}

	//updating Order
	public Order updateOrder(OrderReq req) {
		Order existing = dbOrder(req); 
		if("D".equals(existing.getStatus())) {
			throw new RuntimeException("Order not found with Id");
		}else {
		saveOrderAT(existing);
		
		if (req.getId() != null) existing.setId(req.getId());
		if (req.getCreatedAt() != null) existing.setCreatedAt(req.getCreatedAt());
		if (req.getCustomerId() != null) existing.setCustomerId(req.getCustomerId());
		if (req.getDiscount() != null) existing.setDiscount(req.getDiscount());
		if (req.getNetAmount() != null) existing.setNetAmount(req.getNetAmount());
		if (req.getOrderDate() != null) existing.setOrderDate(req.getOrderDate());
		if (req.getOrderNumber() != null) existing.setOrderNumber(req.getOrderNumber());
		if (req.getPaymentMethod() != null) existing.setPaymentMethod(req.getPaymentMethod());;
		if (req.getPaymentStatus() != null) existing.setPaymentStatus(req.getPaymentStatus());
		if (req.getShippingAddress() != null) existing.setShippingAddress(req.getShippingAddress());
		if (req.getStatus() != null) existing.setStatus(req.getStatus());;
		if (req.getTax() != null) existing.setTax(req.getTax());
		if (req.getTotalAmount() != null) existing.setTotalAmount(req.getTotalAmount());
		if (req.getUpdatedAt() != null) existing.setUpdatedAt(req.getUpdatedAt());
		if(req.getOrd_sts() != null) existing.setOrd_sts(req.getOrd_sts());

		if (req.getOrderItems() != null) {
			for (OrderItemReq ordItemReq : req.getOrderItems()) {
				OrderItem ordItem;

				if (ordItemReq.getId() != null) {
					ordItem = orderItemRepo.findById(ordItemReq.getId())
							.orElseThrow(() -> new RuntimeException("OrderItem not found with id: " + ordItemReq.getId()));
					saveOrderItemAT(ordItem);
				} else {
					ordItem = new OrderItem();
					ordItem.setOrder(existing);
					existing.getOrderItems().add(ordItem);
				}

				// Partial update for OrderItem
				if (ordItemReq.getId() != null) ordItem.setId(ordItemReq.getId());
				if (ordItemReq.getOrder() != null) ordItem.setOrder(ordItemReq.getOrder());
				if (ordItemReq.getPrice() != null) ordItem.setPrice(ordItemReq.getPrice());
				//if (ordItemReq.getProduct() != null) ordItem.setProduct(product(ordItemReq));
				if (ordItemReq.getProductName() != null) ordItem.setProductName(ordItemReq.getProductName());
				if (ordItemReq.getQuantity() != null) ordItem.setQuantity(ordItemReq.getQuantity());
				if (ordItemReq.getTotal() != null) ordItem.setTotal(ordItemReq.getTotal());

				if(ordItemReq.getProduct() != null) {
					HttpResponse<String> response = Unirest.post("http://localhost:8080/api/master/map/" + ordItemReq.getProduct())
			                .header("Content-Type", "application/json")
			                .body("{\"id\":" + ordItemReq.getProduct() + "}")
			                .asString();

			        if (response.getStatus() != 200) {
			            throw new RuntimeException("Failed to fetch product map with ID " + ordItemReq.getProduct() + ". Status: " + response.getStatus());
			        }

			        String product = response.getBody();
			        
			        ordItem.setProduct(product);
					
					}
				orderItemRepo.save(ordItem);
			}
		}
		existing.setStatus("U");
		existing.setUpdatedAt(new Date());
		Order updatedOrder = orderRepo.save(existing);
		return updatedOrder;
		}
	}

	//Deleting Order
	public Order deleteOrder(OrderReq req) {
		Order existing = dbOrder(req);
		if("D".equals(existing.getStatus())) {
			throw new RuntimeException("Order not found with Id");
		}else {
		saveOrderAT(existing);

		existing.setStatus("D");
		Order deletedOrder = orderRepo.save(existing);
		return deletedOrder;
		}

	}

	private void saveOrderAT(Order order) {
		OrderAuditTrial orderAT = new OrderAuditTrial();
		BeanUtils.copyProperties(order, orderAT);
		orderATRepo.save(orderAT);
	}
	
	private void saveOrderItemAT(OrderItem ordItem) {
		OrderItemAuditTrial ordItemAT = new OrderItemAuditTrial();
		BeanUtils.copyProperties(ordItem, ordItemAT);
		orderItemATRepo.save(ordItemAT);
	}

}
