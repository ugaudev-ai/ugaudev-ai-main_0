package com.enquiry.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enquiry.appresource.AppConst;
import com.enquiry.entity.Enquiry;
import com.enquiry.repository.EnquiryRepository;
import com.enquiry.request.EnquiryReq;
import com.enquiry.service.EnquiryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import com.enquiry.response.EndResult;
@RestController
@RequestMapping("/api/enquiries")
public class EnquiryController {

	private static final Logger log = LoggerFactory.getLogger(EnquiryController.class);


	@Autowired private EnquiryService enquiryService;
	@Autowired private EnquiryRepository enquiryRepo;

	@PostMapping
	public String createEnq(@RequestBody EnquiryReq req) {
		String finalResp = null;
		EndResult result = null;
		try {
			Enquiry enq = enquiryService.createEnq(req);
			result= setEndResult(result,AppConst.SUCCESS, "Enquiry data Saved",AppConst.ERRORCODE_SUCCESS);
			result.setData(enq);
			ObjectMapper mapper = new ObjectMapper();
			finalResp = mapper.writeValueAsString(result);
			log.info("In CreateEnquiry(), finalResp:"+finalResp);
		} catch (Exception e) {
			result= setEndResult(result,AppConst.FAILURE, "Invalid Enquiry",AppConst.ERRORCODE_EXCEPTION);
			finalResp = new Gson().toJson(result);
			log.error("In CreateEnquiry(), finalResp:"+finalResp);
			return finalResp;
		}
		return finalResp;
	}

	@GetMapping
	public String getAllEnquiries() {

		String finalResp = null;
		List<Enquiry> enqList = null;
		EndResult result = null;
		try {
			Sort sort = Sort.by(Sort.Direction.ASC, "id");
			Pageable pageable = PageRequest.of(0, 2, sort);
			enqList = (List<Enquiry>) enquiryRepo.findAll(pageable);
			result= setEndResult(result,AppConst.SUCCESS, "Enquiries data Recieved",AppConst.ERRORCODE_SUCCESS);
			result.setData(enqList);
			ObjectMapper mapper = new ObjectMapper();
			finalResp = mapper.writeValueAsString(result);
			log.info("In getAllEnquiries(), finalResp:"+finalResp);
		} catch (Exception e) {
			result= setEndResult(result,AppConst.FAILURE, "Something went wrong!",AppConst.ERRORCODE_EXCEPTION);
			finalResp = new Gson().toJson(result);
			log.error("In getAllEnquiries(), finalResp:"+finalResp);
			return finalResp;
		}
		return finalResp;
	}
	
	@PostMapping("/get")
	public String getEnquiry(@RequestBody EnquiryReq req) {
		String finalResp = null;
		EndResult result = null;
		try {
			Enquiry enq = enquiryService.getEnquiry(req);
			result= setEndResult(result,AppConst.SUCCESS, "Enquiry data Recieved",AppConst.ERRORCODE_SUCCESS);
			result.setData(enq);
			ObjectMapper mapper = new ObjectMapper();
			finalResp = mapper.writeValueAsString(result);
			log.info("In getEnquiry(), finalResp:"+finalResp);
		} catch (Exception e) {
			result= setEndResult(result,AppConst.FAILURE, e.getMessage(),AppConst.ERRORCODE_EXCEPTION);
			finalResp = new Gson().toJson(result);
			log.error("In getEnquiry(), finalResp:"+finalResp);
			return finalResp;
		}
		return finalResp;
	
	}
	
	@GetMapping("/{id}")
	public Enquiry getEnqById(@PathVariable Long id) {
		Enquiry enquiry = enquiryService.getEnqById(id);
		return enquiry;
	}
	
	@PostMapping("/update")
	public String updateEnquiry(@RequestBody EnquiryReq req) {
		String finalResp = null;
		EndResult result = null;
		try {
			Enquiry updatedEnq = enquiryService.updateEnquiry(req);
			result= setEndResult(result,AppConst.SUCCESS, "Enquiry data Updated",AppConst.ERRORCODE_SUCCESS);
			result.setData(updatedEnq);
			ObjectMapper mapper = new ObjectMapper();
			finalResp = mapper.writeValueAsString(result);
			log.info("In updateEnquiry(), finalResp:"+finalResp);
		} catch (Exception e) {
			result= setEndResult(result,AppConst.FAILURE, e.getMessage(),AppConst.ERRORCODE_EXCEPTION);
			finalResp = new Gson().toJson(result);
			log.error("In updateEnquiry(), finalResp:"+finalResp);
			return finalResp;
		}
		return finalResp;
	}
	
	@PostMapping("/delete")
	public String deleteEnq(@RequestBody EnquiryReq req) {

		String finalResp = null;
		EndResult result = null;
		try {
			Enquiry deletedEnq = enquiryService.deleteEnq(req);
			result= setEndResult(result,AppConst.SUCCESS, "Enquiry data Deleted",AppConst.ERRORCODE_SUCCESS);
			result.setData(deletedEnq);
			ObjectMapper mapper = new ObjectMapper();
			finalResp = mapper.writeValueAsString(result);
			log.info("In deleteEnquiry(), finalResp:"+finalResp);
		} catch (Exception e) {
			result= setEndResult(result,AppConst.FAILURE, e.getMessage(),AppConst.ERRORCODE_EXCEPTION);
			finalResp = new Gson().toJson(result);
			log.error("In deleteEnquiry(), finalResp:"+finalResp);
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
