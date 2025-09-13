package com.dataupload.controller;

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

import com.dataupload.appresource.AppConst;
import com.dataupload.entity.DataUpload;
import com.dataupload.repository.DataUploadRepository;
import com.dataupload.request.DataUploadReq;
import com.dataupload.response.EndResult;
import com.dataupload.service.DataUploadService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

@RestController
@RequestMapping("/api/dataUpload")
public class DataUploadController {

	private static final Logger log = LoggerFactory.getLogger(DataUploadController.class);

	@Autowired private DataUploadService dataUploadServ;
	@Autowired private DataUploadRepository dataUploadRepo;

	@PostMapping
	public String createDU(@RequestBody DataUploadReq req) {
		String finalResp = null;
		EndResult result = null;
		try {
			DataUpload createdDU = dataUploadServ.createDU(req);
			result= setEndResult(result,AppConst.SUCCESS, "DataUpload data Saved",AppConst.ERRORCODE_SUCCESS);
			result.setData(createdDU);
			ObjectMapper mapper = new ObjectMapper();
			finalResp = mapper.writeValueAsString(result);
			log.info("In CreateDataUpload(), finalResp:"+finalResp);
		} catch (Exception e) {
			result= setEndResult(result,AppConst.FAILURE, "Invalid DataUpload",AppConst.ERRORCODE_EXCEPTION);
			finalResp = new Gson().toJson(result);
			log.error("In CreateDataUpload(), finalResp:"+finalResp);
			return finalResp;
		}
		return finalResp;
	}

//	@GetMapping
//	public String getAllDUuiries() {
//
//		String finalResp = null;
//		List<DataUpload> dataUpldList = null;
//		EndResult result = null;
//		try {
//			Sort sort = Sort.by(Sort.Direction.ASC, "id");
//			Pageable pageable = PageRequest.of(0, 2, sort);
//			dataUpldList = (List<DataUpload>) dataUploadRepo.findAll(pageable);
//			result= setEndResult(result,AppConst.SUCCESS, "DUuiries data Recieved",AppConst.ERRORCODE_SUCCESS);
//			result.setData(dataUpldList);
//			ObjectMapper mapper = new ObjectMapper();
//			finalResp = mapper.writeValueAsString(result);
//			log.info("In getAllDUuiries(), finalResp:"+finalResp);
//		} catch (Exception e) {
//			result= setEndResult(result,AppConst.FAILURE, "Something went wrong!",AppConst.ERRORCODE_EXCEPTION);
//			finalResp = new Gson().toJson(result);
//			log.error("In getAllDUuiries(), finalResp:"+finalResp);
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

			Page<DataUpload> dataUploadPage = dataUploadRepo.findByStatusNot("D", pageable);

			result = setEndResult(result, AppConst.SUCCESS,"Admins data received", AppConst.ERRORCODE_SUCCESS);

			// Include both list and total count for frontend pagination
			Map<String, Object> data = new HashMap<>();
			data.put("data", dataUploadPage.getContent());
			data.put("total", dataUploadPage.getTotalElements());
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
	public String getDataUpload(@RequestBody DataUploadReq req) {
		String finalResp = null;
		EndResult result = null;
		try {
			DataUpload dataUpload = dataUploadServ.getDataUpload(req);
			result= setEndResult(result,AppConst.SUCCESS, "DataUpload data Recieved",AppConst.ERRORCODE_SUCCESS);
			result.setData(dataUpload);
			ObjectMapper mapper = new ObjectMapper();
			finalResp = mapper.writeValueAsString(result);
			log.info("In getDataUpload(), finalResp:"+finalResp);
		} catch (Exception e) {
			result= setEndResult(result,AppConst.FAILURE, e.getMessage(),AppConst.ERRORCODE_EXCEPTION);
			finalResp = new Gson().toJson(result);
			log.error("In getDataUpload(), finalResp:"+finalResp);
			return finalResp;
		}
		return finalResp;
	
	}
	
	@PostMapping("/update")
	public String updateDataUpload(@RequestBody DataUploadReq req) {
		String finalResp = null;
		EndResult result = null;
		try {
			DataUpload updatedDU = dataUploadServ.updateDataUpload(req);
			result= setEndResult(result,AppConst.SUCCESS, "DataUpload data Updated",AppConst.ERRORCODE_SUCCESS);
			result.setData(updatedDU);
			ObjectMapper mapper = new ObjectMapper();
			finalResp = mapper.writeValueAsString(result);
			log.info("In updateDataUpload(), finalResp:"+finalResp);
		} catch (Exception e) {
			result= setEndResult(result,AppConst.FAILURE, e.getMessage(),AppConst.ERRORCODE_EXCEPTION);
			finalResp = new Gson().toJson(result);
			log.error("In updateDataUpload(), finalResp:"+finalResp);
			return finalResp;
		}
		return finalResp;
	}
	
	@PostMapping("/delete")
	public String deleteDU(@RequestBody DataUploadReq req) {
		String finalResp = null;
		EndResult result = null;
		try {
			DataUpload deletedDU = dataUploadServ.deleteDU(req);
			result= setEndResult(result,AppConst.SUCCESS, "DataUpload data Deleted",AppConst.ERRORCODE_SUCCESS);
			result.setData(deletedDU);
			ObjectMapper mapper = new ObjectMapper();
			finalResp = mapper.writeValueAsString(result);
			log.info("In deleteDataUpload(), finalResp:"+finalResp);
		} catch (Exception e) {
			result= setEndResult(result,AppConst.FAILURE,e.getMessage(),AppConst.ERRORCODE_EXCEPTION);
			finalResp = new Gson().toJson(result);
			log.error("In deleteDataUpload(), finalResp:"+finalResp);
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
