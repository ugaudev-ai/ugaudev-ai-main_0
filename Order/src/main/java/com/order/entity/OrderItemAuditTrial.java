package com.order.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "order_item_at")
public class OrderItemAuditTrial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ordItemReq;
    
    @Column(name = "OrderItemId")
    private Long id;

    private Long productId;
    
    private String productName;
    
    private Integer quantity;
    
    private BigDecimal price;
    
    private BigDecimal total; // quantity * price

	public Long getOrdItemReq() {
		return ordItemReq;
	}

	public void setOrdItemReq(Long ordItemReq) {
		this.ordItemReq = ordItemReq;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}
}
