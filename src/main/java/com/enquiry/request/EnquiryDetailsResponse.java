package com.enquiry.request;

import java.util.List;

import com.enquiry.entity.Enquiry;

public class EnquiryDetailsResponse {
    private Enquiry enquiry;
    private CustomerDTO customer;
    private List<ProductDTO> products;
    // getters and setters
	public Enquiry getEnquiry() {
		return enquiry;
	}
	public void setEnquiry(Enquiry enquiry) {
		this.enquiry = enquiry;
	}
	public CustomerDTO getCustomer() {
		return customer;
	}
	public void setCustomer(CustomerDTO customer) {
		this.customer = customer;
	}
	public List<ProductDTO> getProducts() {
		return products;
	}
	public void setProducts(List<ProductDTO> products) {
		this.products = products;
	}
    
}

