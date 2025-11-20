package com.enquiry.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enquiry.config.CustomerClient;
import com.enquiry.config.ProductClient;
import com.enquiry.entity.Enquiry;
import com.enquiry.entity.EnquiryAuditTrial;
import com.enquiry.repository.EnquiryAuditTrialRepository;
import com.enquiry.repository.EnquiryRepository;
import com.enquiry.request.CustomerDTO;
import com.enquiry.request.EnquiryDetailsResponse;
import com.enquiry.request.EnquiryReq;
import com.enquiry.request.ProductDTO;


@Service
public class EnquiryService {

	@Autowired  private EnquiryRepository enquiryRepo;
	@Autowired	private EnquiryAuditTrialRepository enquiryATRepo;
	@Autowired  private CustomerClient customerClient;
	@Autowired  private ProductClient productClient;

	//Checking the dbEnquiry by Id/CustomerName given by RequestBody
	public Enquiry dbEnq(EnquiryReq req) {
		Enquiry enq = null;
		if(req.getId()	!= null) {
			enq = enquiryRepo.findById(req.getId()).orElseThrow(() -> new RuntimeException("Enquiry not found with id: " + req.getId()));
		}else if(req.getCustomerName() != null) {
//			enq = enquiryRepo.findByCustomerName(req.getCustomerName()).orElseThrow(() -> new RuntimeException("Enquiry not found with CustomerName: " + req.getCustomerName()));
		}

		return enq;
	}
	
	public Enquiry createEnq(EnquiryReq req) {
		Enquiry enquiry = new Enquiry();
		BeanUtils.copyProperties(req, enquiry);
		enquiry.setStatus("N");
		
		CustomerDTO savedCustomer = customerClient.saveCustomer(req.getCustomer());
		Long customerId = savedCustomer.getId();
		
		List<Long> productIds = new ArrayList<>();

        for (ProductDTO productDto : req.getProducts()) {
            ProductDTO savedProduct = productClient.saveProduct(productDto);
            productIds.add(savedProduct.getId());
        }
        enquiry.setProductIds(productIds);
		enquiry.setCustomerId(customerId);
		enquiry.setCreatedAt(new Date());
		System.out.println("Saving enquiry with customerId: " + customerId);
		enquiryRepo.save(enquiry);	
		System.out.println("Enquiry saved successfully");
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
		return enq;
		}
	}
	
	public EnquiryDetailsResponse getEnquiryDetailsById(Long enquiryId) {
        Enquiry enquiry = enquiryRepo.findById(enquiryId)
                .orElseThrow(() -> new RuntimeException("Enquiry not found with id: " + enquiryId));

        // Fetch Customer from Customer microservice
        CustomerDTO customer = customerClient.getCustomerById(enquiry.getCustomerId());
        

        // Fetch Products from Product microservice
        List<ProductDTO> products = new ArrayList<>();
        for (Long productId : enquiry.getProductIds()) {
            ProductDTO product = productClient.getProductById(productId);
            products.add(product);
        }

        // Build combined response
        EnquiryDetailsResponse response = new EnquiryDetailsResponse();
        response.setEnquiry(enquiry);
        response.setCustomer(customer);
        response.setProducts(products);

        return response;
    }

	//updating Enquiry
	public Enquiry updateEnquiry(EnquiryReq req) {
//		Enquiry existing = dbEnq(req); 
		Enquiry existing = enquiryRepo.findById(req.getId())
	            .orElseThrow(() -> new RuntimeException("Enquiry not found with id: " + req.getId()));

		if (existing == null) {
	        throw new RuntimeException("Enquiry not exists for Id: " + req.getId());
	    }
		if("D".equals(existing.getStatus())) {
			throw new RuntimeException("Enquiry not exists for Id:"+req.getId());
		}else {

		saveEnqAT(existing);

		if (req.getId() != null) existing.setId(req.getId());
//		if (req.getCustomerName() != null) existing.setCustomerName(req.getCustomerName());
//		if (req.getEmail() != null) existing.setEmail(req.getEmail());
//		if (req.getPhoneNumber() != null) existing.setPhoneNumber(req.getPhoneNumber());
		if (req.getEnquiryMode() != null) existing.setEnquiryMode(req.getEnquiryMode());
		if (req.getSubject() != null) existing.setSubject(req.getSubject());
		if (req.getMessage() != null) existing.setMessage(req.getMessage());
		if (req.getAssignedTo() != null) existing.setAssignedTo(req.getAssignedTo());
//		if (req.getPhoneNumber() != null) existing.setPhoneNumber(req.getPhoneNumber());
		if (req.getEnquiryDate() != null) existing.setEnquiryDate(req.getEnquiryDate());
		if (req.getRemarks() != null) existing.setRemarks(req.getRemarks());
		
		if (req.getCustomer() != null) {
			
			CustomerDTO newDto = req.getCustomer();
			newDto.setId(existing.getCustomerId());
	        customerClient.updateCustomer(newDto);
	        // If you maintain a reference to customer in Enquiry, update it here
	        // existing.setCustomerId(updatedCustomer.getId());
	    }
		
		// ✅ Update product list if present
		if (req.getProducts() != null && !req.getProducts().isEmpty()) {
//	        List<Long> updatedProductIds = new ArrayList<>();
	        List<Long> existingProductIds = existing.getProductIds();

	        for (int i = 0; i < req.getProducts().size(); i++) {
	            ProductDTO productDto = req.getProducts().get(i);
	            if (existingProductIds != null && existingProductIds.size() > i) {
	                productDto.setId(existingProductIds.get(i));
	            }
	            productClient.updateProduct(productDto);
//	            updatedProductIds.add(updatedProduct.getId());
	        }
//	        existing.setProductIds(updatedProductIds);
	    }
	    
    
		if("CLOSED".equalsIgnoreCase(req.getEnq_status())) {
			existing.setEnq_status(req.getEnq_status());
			existing.setResponseDate(new Date());
		}
		else {
			existing.setEnq_status(req.getEnq_status());
			existing.setResponseDate(new Date());
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
	
	public List<Enquiry> saveAll(List<EnquiryReq> enqDTOList) {
        if (enqDTOList == null || enqDTOList.isEmpty()) {
            throw new IllegalArgumentException("Enquiry list cannot be empty");
        }

        // Convert DTOs to Entities
        List<Enquiry> enquiries = enqDTOList.stream()
                .map(this::convertToEntity)
                .collect(Collectors.toList());

        // Bulk save
        enquiryRepo.saveAll(enquiries);
		return enquiries;
    }
	private Enquiry convertToEntity(EnquiryReq dto) {
        Enquiry e = new Enquiry();
        e.setEnquiryMode(dto.getEnquiryMode());
        e.setCustomerId(dto.getCustomerId());
        e.setSubject(dto.getSubject());
        e.setMessage(dto.getMessage());
        e.setEnq_status(dto.getEnq_status());
        e.setAssignedTo(dto.getAssignedTo());
        e.setEnquiryDate(dto.getEnquiryDate());
        e.setResponseDate(dto.getResponseDate());
        return e;
    }

	//EnquiryAuditTrail
	private void saveEnqAT(Enquiry enq) {
		EnquiryAuditTrial enqAT = new EnquiryAuditTrial();
		BeanUtils.copyProperties(enq, enqAT);
		enquiryATRepo.save(enqAT);
	}
}
