package com.order.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.order.appresource.AppConst;
import com.order.entity.Order;
import com.order.repository.OrderRepository;
import com.order.request.OrderReq;
import com.order.response.EndResult;
import com.order.service.OrderService;
import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

	private static final Logger log = LoggerFactory.getLogger(OrderController.class);

	@Autowired private OrderRepository orderRepo;
	@Autowired private OrderService orderServ;

	@PostMapping
	public String createOrder(@RequestBody OrderReq req) {
		String finalResp = null;
		EndResult result = null;
		try {
			Order createdOrder = orderServ.saveOrder(req);
			result= setEndResult(result,AppConst.SUCCESS, "Order data Saved",AppConst.ERRORCODE_SUCCESS);
			result.setData(createdOrder);
			ObjectMapper mapper = new ObjectMapper();
			finalResp = mapper.writeValueAsString(result);
			log.info("In CreateOrder(), finalResp:"+finalResp);
		} catch (Exception e) {
			result= setEndResult(result,AppConst.FAILURE, "Invalid Order",AppConst.ERRORCODE_EXCEPTION);
			finalResp = new Gson().toJson(result);
			log.error("In CreateOrder(), finalResp:"+finalResp);
			return finalResp;
		}
		return finalResp;
	}
	
//	@GetMapping
//	@Transactional
//	public String getAllOrders() {
//		String finalResp = null;
//		List<Order> orderList = null;
//		EndResult result = null;
//		try {
//			Sort sort = Sort.by(Sort.Direction.ASC, "id");
//			Pageable pageable = PageRequest.of(0, 2, sort);
//			orderList = (List<Order>) orderRepo.findAll(pageable);
//			result= setEndResult(result,AppConst.SUCCESS, "Orders data Recieved",AppConst.ERRORCODE_SUCCESS);
//			result.setData(orderList);
//			ObjectMapper mapper = new ObjectMapper();
//			finalResp = mapper.writeValueAsString(result);
//			log.info("In getAllOrder(), finalResp:"+finalResp);
//		} catch (Exception e) {
//			result= setEndResult(result,AppConst.FAILURE, "Something went wrong!",AppConst.ERRORCODE_EXCEPTION);
//			finalResp = new Gson().toJson(result);
//			log.error("In getAllOrder(), finalResp:"+finalResp);
//			return finalResp;
//		}
//		return finalResp;
//	}
	
	@GetMapping
	@Transactional
	public String getAllCustomers(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size) {

		String finalResp;
		EndResult result = null;

		try {
			Sort sort = Sort.by(Sort.Direction.ASC, "id");
			Pageable pageable = PageRequest.of(page, size, sort);

			Page<Order> custPage = orderRepo.findByStatusNot("D", pageable);

			result = setEndResult(result, AppConst.SUCCESS,"Admins data received", AppConst.ERRORCODE_SUCCESS);

			// Include both list and total count for frontend pagination
			Map<String, Object> data = new HashMap<>();
			data.put("data", custPage.getContent());
			data.put("total", custPage.getTotalElements());
			result.setData(data);

			ObjectMapper mapper = new ObjectMapper();
			finalResp = mapper.writeValueAsString(result);

			log.info("In getAllAdmins(), finalResp: {}", finalResp);

		} catch (Exception e) {
			result = setEndResult(result, AppConst.FAILURE,
					"Something went wrong!", AppConst.ERRORCODE_EXCEPTION);
			finalResp = new Gson().toJson(result);
			log.error("In getAllAdmins(), finalResp: {}", finalResp, e);
			return finalResp;
		}
		return finalResp;
	}
	
	@PostMapping("/get")
	@Transactional
	public String getOrder(@RequestBody OrderReq req) {
		String finalResp = null;
		EndResult result = null;
		try {
			Order ord = orderServ.getOrder(req);
			result= setEndResult(result,AppConst.SUCCESS, "Order data Recieved",AppConst.ERRORCODE_SUCCESS);
			result.setData(ord);
			ObjectMapper mapper = new ObjectMapper();
			finalResp = mapper.writeValueAsString(result);
			log.info("In getOrder(), finalResp:"+finalResp);
		} catch (Exception e) {
			result= setEndResult(result,AppConst.FAILURE, "Invalid Id",AppConst.ERRORCODE_EXCEPTION);
			finalResp = new Gson().toJson(result);
			log.error("In getOrder(), finalResp:"+finalResp);
			return finalResp;
		}
		return finalResp;
	}
	
	@PostMapping("/update")
	public String updateOrder(@RequestBody OrderReq req) {
		String finalResp = null;
		EndResult result = null;
		try {
			Order updatedOrd = orderServ.updateOrder(req);
			result= setEndResult(result,AppConst.SUCCESS, "Order data Updated",AppConst.ERRORCODE_SUCCESS);
			result.setData(updatedOrd);
			ObjectMapper mapper = new ObjectMapper();
			finalResp = mapper.writeValueAsString(result);
			log.info("In updateOrder(), finalResp:"+finalResp);
		} catch (Exception e) {
			result= setEndResult(result,AppConst.FAILURE, "Invalid Id",AppConst.ERRORCODE_EXCEPTION);
			finalResp = new Gson().toJson(result);
			log.error("In updateOrder(), finalResp:"+finalResp);
			return finalResp;
		}
		return finalResp;
	}
	
	@PostMapping("/delete")
	@Transactional
	public String deleteOrder(@RequestBody OrderReq req) {
		String finalResp = null;
		EndResult result = null;
		try {
			Order deletedOrder = orderServ.deleteOrder(req);
			result= setEndResult(result,AppConst.SUCCESS, "Order data Deleted",AppConst.ERRORCODE_SUCCESS);
			result.setData(deletedOrder);
			ObjectMapper mapper = new ObjectMapper();
			finalResp = mapper.writeValueAsString(result);
			log.info("In deleteOrder(), finalResp:"+finalResp);
		} catch (Exception e) {
			result= setEndResult(result,AppConst.FAILURE, "Invalid Id",AppConst.ERRORCODE_EXCEPTION);
			finalResp = new Gson().toJson(result);
			log.error("In deleteOrder(), finalResp:"+finalResp);
			return finalResp;
		}
		return finalResp;
	
	}
	
	public EndResult setEndResult(EndResult result,String status, String message, String errorCode) {
		if(result==null) {
			result = new EndResult();
		}
		result.setMessage(message);
		result.setStatus(status);
		result.setErrorCode(errorCode);
		return result;
	}
}
