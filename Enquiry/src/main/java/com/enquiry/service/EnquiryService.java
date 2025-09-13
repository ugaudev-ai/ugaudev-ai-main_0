package com.enquiry.service;

import java.util.Date;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enquiry.entity.Enquiry;
import com.enquiry.entity.EnquiryAuditTrial;
import com.enquiry.repository.EnquiryAuditTrialRepository;
import com.enquiry.repository.EnquiryRepository;
import com.enquiry.request.EnquiryReq;

@Service
public class EnquiryService {

	@Autowired  private EnquiryRepository enquiryRepo;
	@Autowired	private EnquiryAuditTrialRepository enquiryATRepo;

	//Checking the dbEnquiry by Id/CustomerName given by RequestBody
	public Enquiry dbEnq(EnquiryReq req) {
		Enquiry enq = null;
		if(req.getId()	!= null) {
			enq = enquiryRepo.findById(req.getId()).orElseThrow(() -> new RuntimeException("Enquiry not found with id: " + req.getId()));
		}else if(req.getCustomerName() != null) {
			enq = enquiryRepo.findByCustomerName(req.getCustomerName()).orElseThrow(() -> new RuntimeException("Enquiry not found with CustomerName: " + req.getCustomerName()));
		}

		return enq;
	}
	
	public Enquiry createEnq(EnquiryReq req) {
		Enquiry enquiry = new Enquiry();
		BeanUtils.copyProperties(req, enquiry);
		enquiry.setStatus("N");
		enquiry.setEnquiryDate(new Date());
		enquiry.setCreatedAt(new Date());
		enquiryRepo.save(enquiry);	
		return enquiry;
	}

	//Get Enquiry by dbEnquiry by Id/CustomerName
	public Enquiry getEnquiry(EnquiryReq req) {
		Enquiry existing = dbEnq(req);
		if("D".equals(existing.getStatus())) {
			throw new RuntimeException("Enquiry not exists for Id:"+req.getId());
		}else {
		return existing;
		}
	}
	
	public Enquiry getEnqById(Long id) {
		Enquiry enq = enquiryRepo.findById(id).orElseThrow(() -> new RuntimeException("Enquiry not found with id: " + id));
		if("D".equals(enq.getStatus())) {
			throw new RuntimeException("Enquiry not exists for Id:"+id);
		}else {
			enq.getEnq_status();
		return enq;
		}
	}

	//updating Enquiry
	public Enquiry updateEnquiry(EnquiryReq req) {
		Enquiry existing = dbEnq(req); 
		if("D".equals(existing.getStatus())) {
			throw new RuntimeException("Enquiry not exists for Id:"+req.getId());
		}else {

		saveEnqAT(existing);

		if (req.getId() != null) existing.setId(req.getId());
		if (req.getCustomerName() != null) existing.setCustomerName(req.getCustomerName());
		if (req.getEmail() != null) existing.setEmail(req.getEmail());
		if (req.getPhoneNumber() != null) existing.setPhoneNumber(req.getPhoneNumber());
		if (req.getSubject() != null) existing.setSubject(req.getSubject());
		if (req.getMessage() != null) existing.setMessage(req.getMessage());
		if (req.getAssignedTo() != null) existing.setAssignedTo(req.getAssignedTo());
		if (req.getEnq_status() != null) existing.setEnq_status(req.getEnq_status());
		if (req.getRemarks() != null) existing.setRemarks(req.getRemarks());

		if("CLOSED".equalsIgnoreCase(req.getEnq_status())) {
			existing.setEnq_status(req.getEnq_status());
			existing.setResponseDate(new Date());
		}
		else {
			existing.setStatus("IN PROGRESS");
		}

		existing.setStatus("U");
		existing.setUpdatedAt(new Date());
		Enquiry updatedEnquiry = enquiryRepo.save(existing);

		return updatedEnquiry;
		}

	}

	//Deleting Enquiry
	public Enquiry deleteEnq(EnquiryReq req) {
		Enquiry existing = dbEnq(req);
		if("D".equals(existing.getStatus())) {
			throw new RuntimeException("Enquiry not exists for Id:"+req.getId());
		}else {
		saveEnqAT(existing);
		existing.setStatus("D");
		existing.setUpdatedAt(new Date());

		Enquiry deletedEnq = enquiryRepo.save(existing);
		return deletedEnq;
		}

	}

	//EnquiryAuditTrail
	private void saveEnqAT(Enquiry enq) {
		EnquiryAuditTrial enqAT = new EnquiryAuditTrial();
		BeanUtils.copyProperties(enq, enqAT);
		enquiryATRepo.save(enqAT);
	}
}
