package com.product.response;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ProductResponse {

	private Long id;

    private String productCode;

    private String productName;
    
    private String description;
    
    private String status;
    
    @JsonIgnore
    private List<PrincipalResponse> principals = new ArrayList<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<PrincipalResponse> getPrincipals() {
		return principals;
	}

	public void setPrincipals(List<PrincipalResponse> principals) {
		this.principals = principals;
	}

}
