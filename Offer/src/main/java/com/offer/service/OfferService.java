package com.offer.service;

import java.util.Date;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.offer.entity.Offer;
import com.offer.entity.OfferAuditTrial;
import com.offer.repository.OfferAuditTrialRepository;
import com.offer.repository.OfferRepository;
import com.offer.request.OfferReq;

@Service
public class OfferService {

	@Autowired private OfferRepository offerRepo;
	@Autowired private OfferAuditTrialRepository offerATRepo;

	//Checking the dbOffer by Id/OfferCode given by RequestBody
	public Offer dbOfr(OfferReq req) {
		Offer ofr = null;
		if(req.getId()	!= null) {
			ofr = offerRepo.findById(req.getId()).orElseThrow(() -> new RuntimeException("Offer not found with id: " + req.getId()));
		}else if(req.getOfferCode() != null) {
			ofr = offerRepo.findByOfferCode(req.getOfferCode()).orElseThrow(() -> new RuntimeException("Offer not found with CustomerName: " + req.getOfferCode()));
		}
		return ofr;
	}
	
	public Offer createOffer(OfferReq req) {
		Offer offer = new Offer();
		BeanUtils.copyProperties(req, offer);
		offer.setStatus("N");
		offer.setCreatedAt(new Date());
		return offerRepo.save(offer);
	}
	

	//Get Offer by dbOffer by Id/OfferCode
	public Offer getOffer(OfferReq req) {
		Offer existing = dbOfr(req);
		if("D".equals(existing.getStatus())) {
			throw new RuntimeException("Admin not found with Id");
		}else {
		return existing;
		}
	}

	//updating Offer
	public Offer updateOffer(OfferReq req) {
		Offer existing = dbOfr(req); 
		if("D".equals(existing.getStatus())) {
			throw new RuntimeException("Admin not found with Id");
		}else {
		saveOfrAT(existing);

		if (req.getId() != null) existing.setId(req.getId());
		if (req.getIsActive() != null) existing.setIsActive(req.getIsActive());
		if (req.getCreatedAt() != null) existing.setCreatedAt(req.getCreatedAt());
		if (req.getDescription() != null) existing.setDescription(req.getDescription());
		if (req.getDiscountValue() != null) existing.setDiscountValue(req.getDiscountValue());
		if (req.getMaxDiscountValue() != null) existing.setMaxDiscountValue(req.getMaxDiscountValue());
		if (req.getMinOrderValue() != null) existing.setMinOrderValue(req.getMinOrderValue());
		if (req.getOfferCode() != null) existing.setOfferCode(req.getOfferCode());
		if (req.getStartDate() != null) existing.setStartDate(req.getStartDate());
		if (req.getTitle() != null) existing.setTitle(req.getTitle());
		if (req.getUpdatedAt() != null) existing.setUpdatedAt(req.getUpdatedAt());
		if (req.getUsageLimit() != null) existing.setUsageLimit(req.getUsageLimit());
		if (req.getUsedCount() != null) existing.setUsedCount(req.getUsedCount());
		if(req.getEndDate() != null) existing.setEndDate(req.getEndDate());

		existing.setStatus("U");
		existing.setUpdatedAt(new Date());
		Offer updatedOffer = offerRepo.save(existing);

		return updatedOffer;
		}
	}
	
	//Deleting Offer
		public Offer deleteOffer(OfferReq req) {
			Offer existing = dbOfr(req);
			if("D".equals(existing.getStatus())) {
				throw new RuntimeException("Admin not found with Id");
			}else {
			saveOfrAT(existing);
			
			existing.setIsActive(false);
			existing.setStatus("D");
			existing.setUpdatedAt(new Date());
			Offer deletedOfr = offerRepo.save(existing);
			return deletedOfr;
			}

		}

		//OfferAuditTrail
		private void saveOfrAT(Offer ofr) {
			OfferAuditTrial ofrAT = new OfferAuditTrial();
			BeanUtils.copyProperties(ofr, ofrAT);
			offerATRepo.save(ofrAT);
		}

}
