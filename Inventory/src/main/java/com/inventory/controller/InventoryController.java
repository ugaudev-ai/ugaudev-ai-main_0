package com.inventory.controller;

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
import com.inventory.appresource.AppConst;
import com.inventory.entity.Inventory;
import com.inventory.repository.InventoryRepository;
import com.inventory.request.InventoryReq;
import com.inventory.response.EndResult;
import com.inventory.service.InventoryService;

@RestController
@RequestMapping("/api/inventories")
public class InventoryController {

	private static final Logger log = LoggerFactory.getLogger(InventoryController.class);

	@Autowired private InventoryRepository inventoryRepo;
	@Autowired private InventoryService inventoryServ;

	@PostMapping
	public String createInventory(@RequestBody InventoryReq req) {
		String finalResp = null;
		EndResult result = null;
		try {
			Inventory createdInventory = inventoryServ.createInv(req);
			result= setEndResult(result,AppConst.SUCCESS, "Inventory data Saved",AppConst.ERRORCODE_SUCCESS);
			result.setData(createdInventory);
			ObjectMapper mapper = new ObjectMapper();
			finalResp = mapper.writeValueAsString(result);
			log.info("In CreateInventory(), finalResp:"+finalResp);
		} catch (Exception e) {
			result= setEndResult(result,AppConst.FAILURE, "Invalid Inventory",AppConst.ERRORCODE_EXCEPTION);
			finalResp = new Gson().toJson(result);
			log.error("In CreateInventory(), finalResp:"+finalResp);
			return finalResp;
		}
		return finalResp;
	}
	
//	@GetMapping
//	public String getAllInventories() {
//		String finalResp = null;
//		List<Inventory> invList = null;
//		EndResult result = null;
//		try {
//			Sort sort = Sort.by(Sort.Direction.ASC, "id");
//			Pageable pageable = PageRequest.of(0, 2, sort);
//			invList = (List<Inventory>) inventoryRepo.findAll(pageable);
//			result= setEndResult(result,AppConst.SUCCESS, "Inventory data Recieved",AppConst.ERRORCODE_SUCCESS);
//			result.setData(invList);
//			ObjectMapper mapper = new ObjectMapper();
//			finalResp = mapper.writeValueAsString(result);
//			log.info("In getAllInventories(), finalResp:"+finalResp);
//		} catch (Exception e) {
//			result= setEndResult(result,AppConst.FAILURE, "Something went wrong!",AppConst.ERRORCODE_EXCEPTION);
//			finalResp = new Gson().toJson(result);
//			log.error("In getAllInventories(), finalResp:"+finalResp);
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

			Page<Inventory> inventoryPage = inventoryRepo.findByStatusNot("D", pageable);

			result = setEndResult(result, AppConst.SUCCESS,"Admins data received", AppConst.ERRORCODE_SUCCESS);

			// Include both list and total count for frontend pagination
			Map<String, Object> data = new HashMap<>();
			data.put("data", inventoryPage.getContent());
			data.put("total", inventoryPage.getTotalElements());
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
	public String getInventory(@RequestBody InventoryReq req) {
		String finalResp = null;
		EndResult result = null;
		try {
			Inventory inv = inventoryServ.getInventory(req);
			result= setEndResult(result,AppConst.SUCCESS, "Inventory data Recieved",AppConst.ERRORCODE_SUCCESS);
			result.setData(inv);
			ObjectMapper mapper = new ObjectMapper();
			finalResp = mapper.writeValueAsString(result);
			log.info("In getInventory(), finalResp:"+finalResp);
		} catch (Exception e) {
			result= setEndResult(result,AppConst.FAILURE, "Invalid Id",AppConst.ERRORCODE_EXCEPTION);
			finalResp = new Gson().toJson(result);
			log.error("In getInventory(), finalResp:"+finalResp);
			return finalResp;
		}
		return finalResp;
	}
	
	@PostMapping("/update")
	public String updateInventory(@RequestBody InventoryReq req) {
		String finalResp = null;
		EndResult result = null;
		try {
			Inventory updatedInv = inventoryServ.updateInventory(req);
			result= setEndResult(result,AppConst.SUCCESS, "Inventory data Updated",AppConst.ERRORCODE_SUCCESS);
			result.setData(updatedInv);
			ObjectMapper mapper = new ObjectMapper();
			finalResp = mapper.writeValueAsString(result);
			log.info("In updateInventory(), finalResp:"+finalResp);
		} catch (Exception e) {
			result= setEndResult(result,AppConst.FAILURE, "Invalid Id",AppConst.ERRORCODE_EXCEPTION);
			finalResp = new Gson().toJson(result);
			log.error("In updateInventory(), finalResp:"+finalResp);
			return finalResp;
		}
		return finalResp;
	}
	
	@PostMapping("/delete")
	public String deleteInventory(@RequestBody InventoryReq req) {
		String finalResp = null;
		EndResult result = null;
		try {
			Inventory deletedInv = inventoryServ.deleteInventory(req);
			result= setEndResult(result,AppConst.SUCCESS, "Inventory data Deleted",AppConst.ERRORCODE_SUCCESS);
			result.setData(deletedInv);
			ObjectMapper mapper = new ObjectMapper();
			finalResp = mapper.writeValueAsString(result);
			log.info("In deleteInventory(), finalResp:"+finalResp);
		} catch (Exception e) {
			result= setEndResult(result,AppConst.FAILURE, "Invalid Id",AppConst.ERRORCODE_EXCEPTION);
			finalResp = new Gson().toJson(result);
			log.error("In deleteInventory(), finalResp:"+finalResp);
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
