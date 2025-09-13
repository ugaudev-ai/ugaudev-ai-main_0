package com.product.response;

import java.util.List;

import com.product.entity.Principal;
import com.product.entity.Product;

public class PrincipalProductResponse {
	 
	private Principal principal;
	private List<Product> products;
	
	public Principal getPrincipal() {
		return principal;
	}
	public void setPrincipal(Principal principal) {
		this.principal = principal;
	}
	public List<Product> getProducts() {
		return products;
	}
	public void setProducts(List<Product> products) {
		this.products = products;
	}
	public PrincipalProductResponse(Principal principal, List<Product> products) {
		super();
		this.principal = principal;
		this.products = products;
	}
	
}
