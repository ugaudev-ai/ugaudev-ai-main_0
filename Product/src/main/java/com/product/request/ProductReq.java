package com.product.request;


public class ProductReq {
	
	private Long id;//in create/save id is not used, in update only used
	
	private String pReq_description;
	
	private String productCode;
	
    private String productName;
    
    private String description;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getpReq_description() {
		return pReq_description;
	}

	public void setpReq_description(String pReq_description) {
		this.pReq_description = pReq_description;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
    

}
