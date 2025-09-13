package com.report.controller;

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
import com.report.appresource.AppConst;
import com.report.entity.Report;
import com.report.repository.ReportRepository;
import com.report.request.ReportReq;
import com.report.response.EndResult;
import com.report.service.ReportService;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

	private static final Logger log = LoggerFactory.getLogger(ReportController.class);

	@Autowired private ReportService reportService;
	@Autowired private ReportRepository reportRepo;

	@PostMapping
	public String createRpt(@RequestBody ReportReq req) {
		String finalResp = null;
		EndResult result = null;
		try {
			Report createdRpt = reportService.createReport(req);
			result= setEndResult(result,AppConst.SUCCESS, "Report data Saved",AppConst.ERRORCODE_SUCCESS);
			result.setData(createdRpt);
			ObjectMapper mapper = new ObjectMapper();
			finalResp = mapper.writeValueAsString(result);
			log.info("In CreateReport(), finalResp:"+finalResp);
		} catch (Exception e) {
			result= setEndResult(result,AppConst.FAILURE, "Invalid Report",AppConst.ERRORCODE_EXCEPTION);
			finalResp = new Gson().toJson(result);
			log.error("In CreateReport(), finalResp:"+finalResp);
			return finalResp;
		}
		return finalResp;
	}

//	@GetMapping
//	public String getAllReports() {
//
//		String finalResp = null;
//		List<Report> rptList = null;
//		EndResult result = null;
//		try {
//			Sort sort = Sort.by(Sort.Direction.ASC, "id");
//			Pageable pageable = PageRequest.of(0, 2, sort);
//			rptList = (List<Report>) reportRepo.findAll(pageable);
//			result= setEndResult(result,AppConst.SUCCESS, "Rptuiries data Recieved",AppConst.ERRORCODE_SUCCESS);
//			result.setData(rptList);
//			ObjectMapper mapper = new ObjectMapper();
//			finalResp = mapper.writeValueAsString(result);
//			log.info("In getAllRptuiries(), finalResp:"+finalResp);
//		} catch (Exception e) {
//			result= setEndResult(result,AppConst.FAILURE, "Something went wrong!",AppConst.ERRORCODE_EXCEPTION);
//			finalResp = new Gson().toJson(result);
//			log.error("In getAllRptuiries(), finalResp:"+finalResp);
//			return finalResp;
//		}
//		return finalResp;
//	}
	
	@GetMapping
	@Transactional
	public String getAllReports(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size) {

		String finalResp;
		EndResult result = null;

		try {
			Sort sort = Sort.by(Sort.Direction.ASC, "id");
			Pageable pageable = PageRequest.of(page, size, sort);

			Page<Report> adminPage = reportRepo.findByStatusNot("D", pageable);

			result = setEndResult(result, AppConst.SUCCESS,"Reports data received", AppConst.ERRORCODE_SUCCESS);

			// Include both list and total count for frontend pagination
			Map<String, Object> data = new HashMap<>();
			data.put("data", adminPage.getContent());
			data.put("total", adminPage.getTotalElements());
			result.setData(data);

			ObjectMapper mapper = new ObjectMapper();
			finalResp = mapper.writeValueAsString(result);

			log.info("In getAllReports(), finalResp: {}", finalResp);

		} catch (Exception e) {
			result = setEndResult(result, AppConst.FAILURE,
					"Something went wrong!", AppConst.ERRORCODE_EXCEPTION);
			finalResp = new Gson().toJson(result);
			log.error("In getAllReports(), finalResp: {}", finalResp, e);
			return finalResp;
		}
		return finalResp;
	}
	
	@PostMapping("/get")
	public String getReport(@RequestBody ReportReq req) {
		String finalResp = null;
		EndResult result = null;
		try {
			Report rpt = reportService.getReport(req);
			result= setEndResult(result,AppConst.SUCCESS, "Report data Recieved",AppConst.ERRORCODE_SUCCESS);
			result.setData(rpt);
			ObjectMapper mapper = new ObjectMapper();
			finalResp = mapper.writeValueAsString(result);
			log.info("In getReport(), finalResp:"+finalResp);
		} catch (Exception e) {
			result= setEndResult(result,AppConst.FAILURE,e.getMessage(),AppConst.ERRORCODE_EXCEPTION);
			finalResp = new Gson().toJson(result);
			log.error("In getReport(), finalResp:"+finalResp);
			return finalResp;
		}
		return finalResp;
	
	}
	
	@PostMapping("/update")
	public String updateReport(@RequestBody ReportReq req) {
		String finalResp = null;
		EndResult result = null;
		try {
			Report updatedRpt = reportService.updateReport(req);
			result= setEndResult(result,AppConst.SUCCESS, "Report data Updated",AppConst.ERRORCODE_SUCCESS);
			result.setData(updatedRpt);
			ObjectMapper mapper = new ObjectMapper();
			finalResp = mapper.writeValueAsString(result);
			log.info("In updateReport(), finalResp:"+finalResp);
		} catch (Exception e) {
			result= setEndResult(result,AppConst.FAILURE,e.getMessage(),AppConst.ERRORCODE_EXCEPTION);
			finalResp = new Gson().toJson(result);
			log.error("In updateReport(), finalResp:"+finalResp);
			return finalResp;
		}
		return finalResp;
	}
	
	@PostMapping("/delete")
	public String deleteRpt(@RequestBody ReportReq req) {

		String finalResp = null;
		EndResult result = null;
		try {
			Report deletedRpt = reportService.deleteRpt(req);
			result= setEndResult(result,AppConst.SUCCESS, "Report data Deleted",AppConst.ERRORCODE_SUCCESS);
			result.setData(deletedRpt);
			ObjectMapper mapper = new ObjectMapper();
			finalResp = mapper.writeValueAsString(result);
			log.info("In deleteReport(), finalResp:"+finalResp);
		} catch (Exception e) {
			result= setEndResult(result,AppConst.FAILURE,e.getMessage(),AppConst.ERRORCODE_EXCEPTION);
			finalResp = new Gson().toJson(result);
			log.error("In deleteReport(), finalResp:"+finalResp);
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
