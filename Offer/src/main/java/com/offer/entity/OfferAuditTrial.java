package com.offer.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "offer_at")
public class OfferAuditTrial {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long offerReq_no;
	
	@Column(name = "offer_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private String offerCode;

    @Column(nullable = false)
    private BigDecimal discountValue;

    private BigDecimal minOrderValue;

    private BigDecimal maxDiscountValue;

    private Integer usageLimit;

    private Integer usedCount;

    private Boolean isActive;

    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Asia/Kolkata")
    private Date startDate;

    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Asia/Kolkata")
    private Date endDate;

    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Asia/Kolkata")
    private Date createdAt;

    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Asia/Kolkata")
    private Date updatedAt;
    
    private String status;
    
	public Long getofferReq_no() {
		return offerReq_no;
	}

	public void setofferReq_no(Long offerReq_no) {
		this.offerReq_no = offerReq_no;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getOfferCode() {
		return offerCode;
	}

	public void setOfferCode(String offerCode) {
		this.offerCode = offerCode;
	}

	public BigDecimal getDiscountValue() {
		return discountValue;
	}

	public void setDiscountValue(BigDecimal discountValue) {
		this.discountValue = discountValue;
	}

	public BigDecimal getMinOrderValue() {
		return minOrderValue;
	}

	public void setMinOrderValue(BigDecimal minOrderValue) {
		this.minOrderValue = minOrderValue;
	}

	public BigDecimal getMaxDiscountValue() {
		return maxDiscountValue;
	}

	public void setMaxDiscountValue(BigDecimal maxDiscountValue) {
		this.maxDiscountValue = maxDiscountValue;
	}

	public Integer getUsageLimit() {
		return usageLimit;
	}

	public void setUsageLimit(Integer usageLimit) {
		this.usageLimit = usageLimit;
	}

	public Integer getUsedCount() {
		return usedCount;
	}

	public void setUsedCount(Integer usedCount) {
		this.usedCount = usedCount;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
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

	public Long getOfferReq_no() {
		return offerReq_no;
	}

	public void setOfferReq_no(Long offerReq_no) {
		this.offerReq_no = offerReq_no;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
