package com.admin.controller;

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

import com.admin.appresource.AppConst;
import com.admin.entity.Admin;
import com.admin.repository.AdminRepository;
import com.admin.request.AdminReq;
import com.admin.response.EndResult;
import com.admin.service.AdminService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

@RestController
@RequestMapping("/api/admins")
public class AdminController {

	private static final Logger log = LoggerFactory.getLogger(AdminController.class);


	@Autowired private AdminService adminService;
	@Autowired private AdminRepository adminRepo;

	@PostMapping
	public String createadm(@RequestBody Admin admin) {
		String finalResp = null;
		EndResult result = null;
		try {
			Admin adm = adminService.createAdm(admin);
			result= setEndResult(result,AppConst.SUCCESS, "Admin data Saved",AppConst.ERRORCODE_SUCCESS);
			result.setData(adm);
			ObjectMapper mapper = new ObjectMapper();
			finalResp = mapper.writeValueAsString(result);
			log.info("In CreateAdmin(), finalResp:"+finalResp);
		} catch (Exception e) {
			result= setEndResult(result,AppConst.FAILURE,e.getMessage()+" Invalid Admin",AppConst.ERRORCODE_EXCEPTION);
			finalResp = new Gson().toJson(result);
			log.error("In CreateAdmin(), finalResp:"+finalResp);
			return finalResp;
		}
		return finalResp;
	}

//	@GetMapping
//	public String getAlladmins() {
//
//		String finalResp = null;
//		List<Admin> admList = null;
//		EndResult result = null;
//		try {
//			Sort sort = Sort.by(Sort.Direction.ASC, "id");
//			Pageable pageable = PageRequest.of(0, 2, sort);
//			admList = (List<Admin>) adminRepo.findAll(pageable);
//			result= setEndResult(result,AppConst.SUCCESS, "admuiries data Recieved",AppConst.ERRORCODE_SUCCESS);
//			result.setData(admList);
//			ObjectMapper mapper = new ObjectMapper();
//			finalResp = mapper.writeValueAsString(result);
//			log.info("In getAlladmuiries(), finalResp:"+finalResp);
//		} catch (Exception e) {
//			result= setEndResult(result,AppConst.FAILURE, "Something went wrong!",AppConst.ERRORCODE_EXCEPTION);
//			finalResp = new Gson().toJson(result);
//			log.error("In getAlladmuiries(), finalResp:"+finalResp);
//			return finalResp;
//		}
//		return finalResp;
//	}
	
	@GetMapping
	public String getAllAdmins(
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "5") int size) {

	    String finalResp;
	    EndResult result = null;

	    try {
	        Sort sort = Sort.by(Sort.Direction.ASC, "id");
	        Pageable pageable = PageRequest.of(page, size, sort);

	        Page<Admin> adminPage = adminRepo.findByStatusNot("D", pageable);

	        result = setEndResult(result, AppConst.SUCCESS,
	                "Admins data received", AppConst.ERRORCODE_SUCCESS);

	        // Include both list and total count for frontend pagination
	        Map<String, Object> data = new HashMap<>();
	        data.put("data", adminPage.getContent());
	        data.put("total", adminPage.getTotalElements());
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
	public String getAdmin(@RequestBody AdminReq req) {
		String finalResp = null;
		EndResult result = null;
		try {
			Admin adm = adminService.getAdmin(req);
			result= setEndResult(result,AppConst.SUCCESS, "Admin data Recieved",AppConst.ERRORCODE_SUCCESS);
			result.setData(adm);
			ObjectMapper mapper = new ObjectMapper();
			finalResp = mapper.writeValueAsString(result);
			log.info("In getAdmin(), finalResp:"+finalResp);
		} catch (Exception e) {
			result= setEndResult(result,AppConst.FAILURE,e.getMessage(),AppConst.ERRORCODE_EXCEPTION);
			finalResp = new Gson().toJson(result);
			log.error("In getAdmin(), finalResp:"+finalResp);
			return finalResp;
		}
		return finalResp;
	
	}
	
	@PostMapping("/update")
	public String updateAdmin(@RequestBody AdminReq req) {
		String finalResp = null;
		EndResult result = null;
		try {
			Admin updatedAdm = adminService.updateAdmin(req);
			result= setEndResult(result,AppConst.SUCCESS, "Admin data Updated",AppConst.ERRORCODE_SUCCESS);
			result.setData(updatedAdm);
			ObjectMapper mapper = new ObjectMapper();
			finalResp = mapper.writeValueAsString(result);
			log.info("In updateAdmin(), finalResp:"+finalResp);
		} catch (Exception e) {
			result= setEndResult(result,AppConst.FAILURE, e.getMessage(),AppConst.ERRORCODE_EXCEPTION);
			finalResp = new Gson().toJson(result);
			log.error("In updateAdmin(), finalResp:"+finalResp);
			return finalResp;
		}
		return finalResp;
	}
	
	@PostMapping("/delete")
	public String deleteadm(@RequestBody AdminReq req) {

		String finalResp = null;
		EndResult result = null;
		try {
			Admin deletedAdm = adminService.deleteadm(req);
			result= setEndResult(result,AppConst.SUCCESS, "Admin data Deleted",AppConst.ERRORCODE_SUCCESS);
			result.setData(deletedAdm);
			ObjectMapper mapper = new ObjectMapper();
			finalResp = mapper.writeValueAsString(result);
			log.info("In deleteAdmin(), finalResp:"+finalResp);
		} catch (Exception e) {
			result= setEndResult(result,AppConst.FAILURE,e.getMessage(),AppConst.ERRORCODE_EXCEPTION);
			finalResp = new Gson().toJson(result);
			log.error("In deleteAdmin(), finalResp:"+finalResp);
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
