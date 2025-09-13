package com.payment.controller;

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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.payment.appresource.AppConst;
import com.payment.entity.Payment;
import com.payment.repository.PaymentRepository;
import com.payment.request.PaymentReq;
import com.payment.response.EndResult;
import com.payment.service.PaymentService;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
	
	private static final Logger log = LoggerFactory.getLogger(PaymentController.class);


	@Autowired private PaymentRepository paymentRepo;
	@Autowired private PaymentService paymentServ;
	
	@PostMapping
	public String createPayment(@RequestBody PaymentReq req) {
		String finalResp = null;
		EndResult result = null;
		try {
			Payment createdPayment = paymentServ.createPayment(req);
			result= setEndResult(result,AppConst.SUCCESS, "Payment data Saved",AppConst.ERRORCODE_SUCCESS);
			result.setData(createdPayment);
			ObjectMapper mapper = new ObjectMapper();
			finalResp = mapper.writeValueAsString(result);
			log.info("In CreatePayment(), finalResp:"+finalResp);
		} catch (Exception e) {
			result= setEndResult(result,AppConst.FAILURE, "Invalid Payment",AppConst.ERRORCODE_EXCEPTION);
			finalResp = new Gson().toJson(result);
			log.error("In CreatePayment(), finalResp:"+finalResp);
			return finalResp;
		}
		return finalResp;
	}
	
	
	
	@GetMapping
	public String getPayments(
	    @RequestParam(defaultValue = "0") int page,
	    @RequestParam(defaultValue = "10") int size) {
	    
	    String finalResp = null;
	    Page<Payment> paymentPage = null;
	    EndResult result = null;
	    try {
	        Sort sort = Sort.by(Sort.Direction.ASC, "id");
	        Pageable pageable = PageRequest.of(page, size, sort);
//	        paymentList = (List<Payment>) paymentRepo.findAll(pageable);
	        paymentPage = paymentRepo.findByStatusNot("D", pageable);
	        // You may want to send totalElements and totalPages for frontend use:
//	        long totalElements = paymentRepo.count();
	        
	        result = setEndResult(result, AppConst.SUCCESS, "Payment data Received", AppConst.ERRORCODE_SUCCESS);
	        
	        // Include page metadata inside data or as a wrapper object
//	        Map<String, Object> responseData = new HashMap<>();
//	        responseData.put("payments", paymentList);
//	        responseData.put("currentPage", page);
//	        responseData.put("pageSize", size);
//	        responseData.put("totalElements", totalElements);
	        Map<String, Object> data = new HashMap<>();
	        data.put("data", paymentPage.getContent());
	        data.put("total", paymentPage.getTotalElements());
	        result.setData(data);
	        
	        
	        ObjectMapper mapper = new ObjectMapper();
	        finalResp = mapper.writeValueAsString(result);
	        log.info("In getPayments(), finalResp:" + finalResp);
	    } catch (Exception e) {
	        result = setEndResult(result, AppConst.FAILURE, "Something went wrong!", AppConst.ERRORCODE_EXCEPTION);
	        finalResp = new Gson().toJson(result);
	        log.error("In getPayments(), finalResp:" + finalResp);
	        return finalResp;
	    }
	    return finalResp;
	}

	
//	@GetMapping
//	public String getPayments() {
//		String finalResp = null;
//		List<Payment> paymentList = null;
//		EndResult result = null;
//		try {
//			Sort sort = Sort.by(Sort.Direction.ASC, "id");
//			Pageable pageable = PageRequest.of(0, 5, sort);
////			paymentList = (List<Payment>) paymentRepo.findAll(pageable);
//			paymentList = paymentRepo.findByStatusNot("D", pageable);
//			result= setEndResult(result,AppConst.SUCCESS, "Payment data Recieved",AppConst.ERRORCODE_SUCCESS);
//			result.setData(paymentList);
//			ObjectMapper mapper = new ObjectMapper();
//			finalResp = mapper.writeValueAsString(result);
//			log.info("In getAllPayment(), finalResp:"+finalResp);
//		} catch (Exception e) {
//			result= setEndResult(result,AppConst.FAILURE, "Something went wrong!",AppConst.ERRORCODE_EXCEPTION);
//			finalResp = new Gson().toJson(result);
//			log.error("In getAllPayment(), finalResp:"+finalResp);
//			return finalResp;
//		}
//		return finalResp;
//	}
	
	@PostMapping("/get")
	public String getPayment(@RequestBody PaymentReq req) {
		String finalResp = null;
		EndResult result = null;
		try {
			Payment enq = paymentServ.getPayment(req);
			result= setEndResult(result,AppConst.SUCCESS, "Payment data Recieved",AppConst.ERRORCODE_SUCCESS);
			result.setData(enq);
			ObjectMapper mapper = new ObjectMapper();
			finalResp = mapper.writeValueAsString(result);
			log.info("In getPayment(), finalResp:"+finalResp);
		} catch (Exception e) {
			result= setEndResult(result,AppConst.FAILURE, e.getMessage(),AppConst.ERRORCODE_EXCEPTION);
			finalResp = new Gson().toJson(result);
			log.error("In getPayment(), finalResp:"+finalResp);
			return finalResp;
		}
		return finalResp;
	}
	
	@PostMapping("/update")
	public String updatePayment(@RequestBody PaymentReq req) {
		String finalResp = null;
		EndResult result = null;
		try {
			Payment updatedPayment = paymentServ.updatePayment(req);
			result= setEndResult(result,AppConst.SUCCESS, "Payment data Updated",AppConst.ERRORCODE_SUCCESS);
			result.setData(updatedPayment);
			ObjectMapper mapper = new ObjectMapper();
			finalResp = mapper.writeValueAsString(result);
			log.info("In updatePayment(), finalResp:"+finalResp);
		} catch (Exception e) {
			result= setEndResult(result,AppConst.FAILURE, e.getMessage(),AppConst.ERRORCODE_EXCEPTION);
			finalResp = new Gson().toJson(result);
			log.error("In updatePayment(), finalResp:"+finalResp);
			return finalResp;
		}
		return finalResp;
	}
	
	@PostMapping("/delete")
	public String deletePaymet(@RequestBody PaymentReq req) {
		String finalResp = null;
		EndResult result = null;
		try {
			Payment deletedPayment = paymentServ.deletePayment(req);
			result= setEndResult(result,AppConst.SUCCESS, "Payment data Deleted",AppConst.ERRORCODE_SUCCESS);
			result.setData(deletedPayment);
			ObjectMapper mapper = new ObjectMapper();
			finalResp = mapper.writeValueAsString(result);
			log.info("In deletePayment(), finalResp:"+finalResp);
		} catch (Exception e) {
			result= setEndResult(result,AppConst.FAILURE, e.getMessage(),AppConst.ERRORCODE_EXCEPTION);
			finalResp = new Gson().toJson(result);
			log.error("In deletePayment(), finalResp:"+finalResp);
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
