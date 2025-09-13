package com.offer.controller;

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
import com.offer.appresource.AppConst;
import com.offer.entity.Offer;
import com.offer.repository.OfferRepository;
import com.offer.request.OfferReq;
import com.offer.response.EndResult;
import com.offer.service.OfferService;

@RestController
@RequestMapping("/api/offers")
public class OfferController {

	private static final Logger log = LoggerFactory.getLogger(OfferController.class);

	@Autowired private OfferRepository offerRepo;
	@Autowired private OfferService offerService;

	@PostMapping
	public String createOffer(@RequestBody OfferReq req) {
		String finalResp = null;
		EndResult result = null;
		try {
			Offer ofr = offerService.createOffer(req);
			result= setEndResult(result,AppConst.SUCCESS,"Offer data Saved",AppConst.ERRORCODE_SUCCESS);
			result.setData(ofr);
			ObjectMapper mapper = new ObjectMapper();
			finalResp = mapper.writeValueAsString(result);
//			finalResp = new Gson().toJson(result);
			log.info("In CreateOffer(), finalResp:"+finalResp);
		} catch (Exception e) {
			result= setEndResult(result,AppConst.FAILURE,e.getMessage()+"Invalid Offer",AppConst.ERRORCODE_EXCEPTION);
			finalResp = new Gson().toJson(result);
			log.error("In CreateOffer(), finalResp:"+finalResp);
			return finalResp;
		}
		return finalResp;
	}

//	@GetMapping
//	public String getAllOffer() {
//
//		String finalResp = null;
//		List<Offer> ofrList = null;
//		EndResult result = null;
//		try {
//			
//			Sort sort = Sort.by(Sort.Direction.ASC, "id");
//			Pageable pageable = PageRequest.of(0, 5, sort);
//			//ofrList = (List<Offer>) offerRepo.findAll(pageable);
//			ofrList = (List<Offer>) offerRepo.findByStatusNot("D", pageable);
//			result= setEndResult(result,AppConst.SUCCESS, "Offers data Recieved",AppConst.ERRORCODE_SUCCESS);
//			result.setData(ofrList);
//			ObjectMapper mapper = new ObjectMapper();
//			finalResp = mapper.writeValueAsString(result);
//			//finalResp = new Gson().toJson(result);
//			log.info("In getAllOffers(), finalResp:"+finalResp);
//		} catch (Exception e) {
//			result= setEndResult(result,AppConst.FAILURE, "Something went wrong!",AppConst.ERRORCODE_EXCEPTION);
//			finalResp = new Gson().toJson(result);
//			log.error("In getAllOffers(), finalResp:"+finalResp);
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

			Page<Offer> offerPage = offerRepo.findByStatusNot("D", pageable);

			result = setEndResult(result, AppConst.SUCCESS,"Offer data received", AppConst.ERRORCODE_SUCCESS);

			// Include both list and total count for frontend pagination
			Map<String, Object> data = new HashMap<>();
			data.put("data", offerPage.getContent());
			data.put("total", offerPage.getTotalElements());
			result.setData(data);

			ObjectMapper mapper = new ObjectMapper();
			finalResp = mapper.writeValueAsString(result);

			log.info("In getAllOffer(), finalResp: {}", finalResp);

		} catch (Exception e) {
			result = setEndResult(result, AppConst.FAILURE,
					"Something went wrong!", AppConst.ERRORCODE_EXCEPTION);
			finalResp = new Gson().toJson(result);
			log.error("In getAllOffer(), finalResp: {}", finalResp, e);
			return finalResp;
		}
		return finalResp;
	}

	@PostMapping("/get")
	public String getOffer(@RequestBody OfferReq req) {
		String finalResp = null;
		EndResult result = null;
		try {
			Offer ofr = offerService.getOffer(req);
			result= setEndResult(result,AppConst.SUCCESS, "Offers data Recieved",AppConst.ERRORCODE_SUCCESS);
			result.setData(ofr);
			ObjectMapper mapper = new ObjectMapper();
			finalResp = mapper.writeValueAsString(result);
//			finalResp = new Gson().toJson(result);
			log.info("In getOffer(), finalResp:"+finalResp);
		} catch (Exception e) {
			result= setEndResult(result,AppConst.FAILURE, "Invalid Id/OfferCode",AppConst.ERRORCODE_EXCEPTION);
			finalResp = new Gson().toJson(result);
			log.error("In getOffer(), finalResp:"+finalResp);
			return finalResp;
		}
		return finalResp;
	}

	@PostMapping("/update")
	public String updateOffer(@RequestBody OfferReq req) {
		String finalResp = null;
		EndResult result = null;
		try {
			Offer updatedOfr = offerService.updateOffer(req);
			result= setEndResult(result,AppConst.SUCCESS, "Offer data Updated",AppConst.ERRORCODE_SUCCESS);
			result.setData(updatedOfr);
			ObjectMapper mapper = new ObjectMapper();
			finalResp = mapper.writeValueAsString(result);
//			finalResp = new Gson().toJson(result);
			log.info("In updateOffer(), finalResp:"+finalResp);
		} catch (Exception e) {
			result= setEndResult(result,AppConst.FAILURE, "Invalid Id/OfferCode",AppConst.ERRORCODE_EXCEPTION);
			finalResp = new Gson().toJson(result);
			log.error("In updateOffer(), finalResp:"+finalResp);
			return finalResp;
		}
		return finalResp;
	}
	
	@PostMapping("/delete")
	public String deleteOffer(@RequestBody OfferReq req) {

		String finalResp = null;
		EndResult result = null;
		try {
			Offer deletedOfr = offerService.deleteOffer(req);
			result= setEndResult(result,AppConst.SUCCESS, "Offer data Deleted",AppConst.ERRORCODE_SUCCESS);
			result.setData(deletedOfr);
			ObjectMapper mapper = new ObjectMapper();
			finalResp = mapper.writeValueAsString(result);
//			finalResp = new Gson().toJson(result);
			log.info("In deleteOffer(), finalResp:"+finalResp);
		} catch (Exception e) {
			result= setEndResult(result,AppConst.FAILURE, "Invalid Id/OfferCode",AppConst.ERRORCODE_EXCEPTION);
			finalResp = new Gson().toJson(result);
			log.error("In deleteOffer(), finalResp:"+finalResp);
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
