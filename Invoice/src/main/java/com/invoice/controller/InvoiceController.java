package com.invoice.controller;

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
import com.invoice.appresource.AppConst;
import com.invoice.entity.Invoice;
import com.invoice.repository.InvoiceRepository;
import com.invoice.request.InvoiceReq;
import com.invoice.response.EndResult;
import com.invoice.service.InvoiceService;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

	private static final Logger log = LoggerFactory.getLogger(InvoiceController.class);

	@Autowired private InvoiceService invoiceService;
	@Autowired private InvoiceRepository invoiceRepo;

	@PostMapping
	public String createInv(@RequestBody InvoiceReq req) {
		String finalResp = null;
		EndResult result = null;
		try {
			Invoice Inv = invoiceService.createInv(req);
			result= setEndResult(result,AppConst.SUCCESS, "Invoice data Saved",AppConst.ERRORCODE_SUCCESS);
			result.setData(Inv);
			ObjectMapper mapper = new ObjectMapper();
			finalResp = mapper.writeValueAsString(result);
			log.info("In CreateInvoice(), finalResp:"+finalResp);
		} catch (Exception e) {
			result= setEndResult(result,AppConst.FAILURE, "Invalid Invoice",AppConst.ERRORCODE_EXCEPTION);
			finalResp = new Gson().toJson(result);
			log.error("In CreateInvoice(), finalResp:"+finalResp);
			return finalResp;
		}
		return finalResp;
	}

//	@GetMapping
//	public String getAllInvoices() {
//
//		String finalResp = null;
//		List<Invoice> InvList = null;
//		EndResult result = null;
//		try {
//			Sort sort = Sort.by(Sort.Direction.ASC, "id");
//			Pageable pageable = PageRequest.of(0, 2, sort);
//			InvList = (List<Invoice>) invoiceRepo.findAll(pageable);
//			result= setEndResult(result,AppConst.SUCCESS, "Invuiries data Recieved",AppConst.ERRORCODE_SUCCESS);
//			result.setData(InvList);
//			ObjectMapper mapper = new ObjectMapper();
//			finalResp = mapper.writeValueAsString(result);
//			log.info("In getAllInvuiries(), finalResp:"+finalResp);
//		} catch (Exception e) {
//			result= setEndResult(result,AppConst.FAILURE, "Something went wrong!",AppConst.ERRORCODE_EXCEPTION);
//			finalResp = new Gson().toJson(result);
//			log.error("In getAllInvuiries(), finalResp:"+finalResp);
//			return finalResp;
//		}
//		return finalResp;
//	}
	
	@GetMapping
	public String getAllUsers(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size) {

		String finalResp;
		EndResult result = null;

		try {
			Sort sort = Sort.by(Sort.Direction.ASC, "id");
			Pageable pageable = PageRequest.of(page, size, sort);

			Page<Invoice> invoicePage = invoiceRepo.findByStatusNot("D", pageable);

			result = setEndResult(result, AppConst.SUCCESS,"Invoice data received", AppConst.ERRORCODE_SUCCESS);

			// Include both list and total count for frontend pagination
			Map<String, Object> data = new HashMap<>();
			data.put("data", invoicePage.getContent());
			data.put("total", invoicePage.getTotalElements());
			result.setData(data);

			ObjectMapper mapper = new ObjectMapper();
			finalResp = mapper.writeValueAsString(result);

			log.info("In getAllInvoice(), finalResp: {}", finalResp);

		} catch (Exception e) {
			result = setEndResult(result, AppConst.FAILURE,
					"Something went wrong!", AppConst.ERRORCODE_EXCEPTION);
			finalResp = new Gson().toJson(result);
			log.error("In getAllInvoice(), finalResp: {}", finalResp, e);
			return finalResp;
		}
		return finalResp;
	}
	
	@PostMapping("/get")
	public String getInvoice(@RequestBody InvoiceReq req) {
		String finalResp = null;
		EndResult result = null;
		try {
			Invoice Inv = invoiceService.getInvoice(req);
			result= setEndResult(result,AppConst.SUCCESS, "Invoice data Recieved",AppConst.ERRORCODE_SUCCESS);
			result.setData(Inv);
			ObjectMapper mapper = new ObjectMapper();
			finalResp = mapper.writeValueAsString(result);
			log.info("In getInvoice(), finalResp:"+finalResp);
		} catch (Exception e) {
			result= setEndResult(result,AppConst.FAILURE,e.getMessage(),AppConst.ERRORCODE_EXCEPTION);
			finalResp = new Gson().toJson(result);
			log.error("In getInvoice(), finalResp:"+finalResp);
			return finalResp;
		}
		return finalResp;
	
	}
	
	@PostMapping("/update")
	public String updateInvoice(@RequestBody InvoiceReq req) {
		String finalResp = null;
		EndResult result = null;
		try {
			Invoice updatedInv = invoiceService.updateInvoice(req);
			result= setEndResult(result,AppConst.SUCCESS, "Invoice data Updated",AppConst.ERRORCODE_SUCCESS);
			result.setData(updatedInv);
			ObjectMapper mapper = new ObjectMapper();
			finalResp = mapper.writeValueAsString(result);
			log.info("In updateInvoice(), finalResp:"+finalResp);
		} catch (Exception e) {
			result= setEndResult(result,AppConst.FAILURE,e.getMessage(),AppConst.ERRORCODE_EXCEPTION);
			finalResp = new Gson().toJson(result);
			log.error("In updateInvoice(), finalResp:"+finalResp);
			return finalResp;
		}
		return finalResp;
	}
	
	@PostMapping("/delete")
	public String deleteInv(@RequestBody InvoiceReq req) {

		String finalResp = null;
		EndResult result = null;
		try {
			Invoice deletedInv = invoiceService.deleteInv(req);
			result= setEndResult(result,AppConst.SUCCESS, "Invoice data Deleted",AppConst.ERRORCODE_SUCCESS);
			result.setData(deletedInv);
			ObjectMapper mapper = new ObjectMapper();
			finalResp = mapper.writeValueAsString(result);
			log.info("In deleteInvoice(), finalResp:"+finalResp);
		} catch (Exception e) {
			result= setEndResult(result,AppConst.FAILURE,e.getMessage(),AppConst.ERRORCODE_EXCEPTION);
			finalResp = new Gson().toJson(result);
			log.error("In deleteInvoice(), finalResp:"+finalResp);
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
