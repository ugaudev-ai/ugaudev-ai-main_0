package com.inventory.request;

import java.util.Date;

import com.inventory.enums.InventoryStatus;

public class InventoryReq {

    private Long id;

    private Long product;

    private Integer quantityAvailable;

    private Integer quantityReserved;

    private Integer reorderLevel;

    private String location;

    private InventoryStatus status;

    private Date lastUpdated;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

//	public Product getProduct() {
//		return product;
//	}
//
//	public void setProduct(Product product) {
//		this.product = product;
//	}

	public Integer getQuantityAvailable() {
		return quantityAvailable;
	}

	public void setQuantityAvailable(Integer quantityAvailable) {
		this.quantityAvailable = quantityAvailable;
	}

	public Integer getQuantityReserved() {
		return quantityReserved;
	}

	public void setQuantityReserved(Integer quantityReserved) {
		this.quantityReserved = quantityReserved;
	}

	public Integer getReorderLevel() {
		return reorderLevel;
	}

	public void setReorderLevel(Integer reorderLevel) {
		this.reorderLevel = reorderLevel;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public InventoryStatus getStatus() {
		return status;
	}

	public void setStatus(InventoryStatus status) {
		this.status = status;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public Long getProduct() {
		return product;
	}

	public void setProduct(Long product) {
		this.product = product;
	}
	
}
