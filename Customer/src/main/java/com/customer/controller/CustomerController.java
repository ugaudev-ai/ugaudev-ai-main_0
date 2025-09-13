package com.customer.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.customer.appresouce.AppConst;
import com.customer.entity.Customer;
import com.customer.repository.CustomerRepository;
import com.customer.request.CustomerReq;
import com.customer.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.javaguides.response.EndResult;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

	private static final Logger log = LoggerFactory.getLogger(CustomerController.class);
	
	@Autowired	private CustomerRepository custRepo;
	@Autowired	private CustomerService custService;

//	@GetMapping
//	public String getAllCustomers() {
//		String finalResp = null;
//		List<Customer> custList = null;
//		EndResult result = null;
//		try {
//			Sort sort = Sort.by(Sort.Direction.ASC, "id");
//			Pageable pageable = PageRequest.of(0, 2, sort);
//			custList = (List<Customer>) custRepo.findAll(pageable);
//			result= setEndResult(result,AppConst.SUCCESS, "Customers data Recieved",AppConst.ERRORCODE_SUCCESS);
//			result.setData(custList);
//			ObjectMapper mapper = new ObjectMapper();
//			finalResp = mapper.writeValueAsString(result);
//			log.info("In getAllCustomers(), finalResp:"+finalResp);
//		} catch (Exception e) {
//			result= setEndResult(result,AppConst.FAILURE,"Something went wrong!",AppConst.ERRORCODE_EXCEPTION);
//			finalResp = new Gson().toJson(result);
//			log.error("In getAllCustomers(), finalResp:"+finalResp);
//			return finalResp;
//		}
//		return finalResp;
//	}

	@GetMapping
	public String getAllCustomers(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size) {

		String finalResp;
		EndResult result = null;

		try {
			Sort sort = Sort.by(Sort.Direction.ASC, "id");
			Pageable pageable = PageRequest.of(page, size, sort);

			Page<Customer> ordPage = custRepo.findByStatusNot("D", pageable);

			result = setEndResult(result, AppConst.SUCCESS,"Order data received", AppConst.ERRORCODE_SUCCESS);

			// Include both list and total count for frontend pagination
			Map<String, Object> data = new HashMap<>();
			data.put("data", ordPage.getContent());
			data.put("total", ordPage.getTotalElements());
			result.setData(data);

			ObjectMapper mapper = new ObjectMapper();
			finalResp = mapper.writeValueAsString(result);

			log.info("In getAllOrder(), finalResp: {}", finalResp);

		} catch (Exception e) {
			result = setEndResult(result, AppConst.FAILURE,
					"Something went wrong!", AppConst.ERRORCODE_EXCEPTION);
			finalResp = new Gson().toJson(result);
			log.error("In getAllOrder(), finalResp: {}", finalResp, e);
			return finalResp;
		}
		return finalResp;
	}
	
	@PostMapping("/get")
	public String getCustomer(@RequestBody CustomerReq req) {
		String finalResp = null;
		EndResult result = null;
		Customer dbCust = null;
		try {
			dbCust = custService.getCustomer(req);
			result= setEndResult(result,AppConst.SUCCESS, "Customer data Recieved",AppConst.ERRORCODE_SUCCESS);
			result.setData(dbCust);
			ObjectMapper mapper = new ObjectMapper();
			finalResp = mapper.writeValueAsString(result);
			log.info("In getCustomer(), finalResp:"+finalResp);
	} catch (Exception e) {
			result= setEndResult(result,AppConst.FAILURE,e.getMessage(),AppConst.ERRORCODE_EXCEPTION);
			finalResp = new Gson().toJson(result);
			log.error("In getCustomer(), finalResp:"+finalResp);
			return finalResp;
		}
		return finalResp;
		
	}

	@PostMapping
	public String createCustomer(@RequestBody CustomerReq customer) {
		String finalResp = null;
		EndResult result = null;
		Customer dbCust = null;
		try {
			dbCust = custService.createCust(customer);
			result= setEndResult(result,AppConst.SUCCESS, "Customer data Saved",AppConst.ERRORCODE_SUCCESS);
			result.setData(dbCust);
			ObjectMapper mapper = new ObjectMapper();
			finalResp = mapper.writeValueAsString(result);
			log.info("In createCustomer(), finalResp:"+finalResp);
	} catch (Exception e) {
			result= setEndResult(result,AppConst.FAILURE,"Invalid Customer",AppConst.ERRORCODE_EXCEPTION);
			finalResp = new Gson().toJson(result);
			log.error("In createCustomer(), finalResp:"+finalResp);
			return finalResp;
		}
		return finalResp;
		
	}
	
	@PostMapping("/update")
	public String updateCustomer(@RequestBody CustomerReq custReq) {
		String finalResp = null;
		EndResult result = null;
		Customer dbCust = null;
		try {
			dbCust = custService.updateCust(custReq);
			result= setEndResult(result,AppConst.SUCCESS, "Customer data Saved",AppConst.ERRORCODE_SUCCESS);
			result.setData(dbCust);
			ObjectMapper mapper = new ObjectMapper();
			finalResp = mapper.writeValueAsString(result);
			log.info("In updateCustomer(), finalResp:"+finalResp);
	} catch (Exception e) {
			result= setEndResult(result,AppConst.FAILURE,e.getMessage(),AppConst.ERRORCODE_EXCEPTION);
			finalResp = new Gson().toJson(result);
			log.error("In updateCustomer(), finalResp:"+finalResp);
			return finalResp;
		}
		return finalResp;
	}

	@PostMapping("/delete")
	public String deletecustomer(@RequestBody CustomerReq custReq) {
		String finalResp = null;
		EndResult result = null;
		try {
			Customer deletedCust = custService.deleteCust(custReq);
			result = setEndResult(result, AppConst.SUCCESS, "Customer updated", AppConst.ERRORCODE_SUCCESS);
			result.setData(deletedCust);
			ObjectMapper mapper = new ObjectMapper();
			finalResp = mapper.writeValueAsString(result);
			log.info("In deleteCustomer(), finalResp:"+finalResp);
		} catch (Exception e) {
			log.error("Error updating customer", e);
			result = setEndResult(result, AppConst.FAILURE, e.getMessage(), AppConst.ERRORCODE_EXCEPTION);
			finalResp = new Gson().toJson(result);
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

	/*
	 * public List<String> validateCustomer(Customer cust){
	 * 
	 * List<String> errorList = new ArrayList<>(); List<Address> addresses = cust !=
	 * null ? cust.getAddresses() : null;
	 * 
	 * if(cust==null) { errorList.add("Customer data is empty"); }
	 * 
	 * if(cust!=null && cust.getId()==null) {
	 * errorList.add("Customer id is null, please enter id and try again"); }
	 * 
	 * if(cust!=null && cust.getName()==null) {
	 * errorList.add("Customer name is null, please enter name and try again"); }
	 * 
	 * if(cust!=null && addresses==null) {
	 * errorList.add("Customer Address is null, please enter Address and try again"
	 * ); }
	 * 
	 * if(addresses.size()>0) { for (Iterator iterator = addresses.iterator();
	 * iterator.hasNext();) { Address address = (Address) iterator.next();
	 * if(address!=null && address.getAddressType()==null) {
	 * errorList.add("AddressType is null, please enter AddresType and try again");
	 * } } }
	 * 
	 * return errorList; }
	 */
	
}
