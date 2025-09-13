package com.product.response;

import java.util.List;

import com.product.entity.Principal;
import com.product.entity.Product;

public class ProductPrincipalResponse {

	private Product product;
	
	private List<Principal> principals;
	
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public List<Principal> getPrincipals() {
		return principals;
	}

	public void setPrincipals(List<Principal> principals) {
		this.principals = principals;
	}

	public ProductPrincipalResponse(Product product, List<Principal> principals) {
		super();
		this.product = product;
		this.principals = principals;
	}
	
}
