package com.inventory.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.inventory.enums.InventoryStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "inventory_at")
public class InventoryAuditTrial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long invreq_id;
    
    @Column(name = "inventory_id")
    private Long id;

    private Long product;

    private Integer quantityAvailable;

    private Integer quantityReserved;

    private Integer reorderLevel;

    private String location;

    @Enumerated(EnumType.STRING)
    @Column(name = "inventory_status")
    private InventoryStatus inv_status;

    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-YYYY")
    private Date createdAt;
    
    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-YYYY")
    private Date updatedAt;
    
    private String status;

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

	public Long getInvreq_id() {
		return invreq_id;
	}

	public void setInvreq_id(Long invreq_id) {
		this.invreq_id = invreq_id;
	}

	public InventoryStatus getInv_status() {
		return inv_status;
	}

	public void setInv_status(InventoryStatus inv_status) {
		this.inv_status = inv_status;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getProduct() {
		return product;
	}

	public void setProduct(Long product) {
		this.product = product;
	}

}
