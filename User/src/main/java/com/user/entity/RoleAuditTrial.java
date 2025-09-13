package com.user.entity;

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
@Table(name = "role_at")
public class RoleAuditTrial {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long rolereq_no;
	
	@Column(name = "role_id")
	private Long id;
	
	private String role;
	
	private String authorisedServices;
	
	private String authorisedPages;
	
	@Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Asia/Kolkata")
	private Date createdDate;
	
	@Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Asia/Kolkata")
	private Date updateDate;
	
	private String status;

	public Long getRolereq_no() {
		return rolereq_no;
	}

	public void setRolereq_no(Long rolereq_no) {
		this.rolereq_no = rolereq_no;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getAuthorisedServices() {
		return authorisedServices;
	}

	public void setAuthorisedServices(String authorisedServices) {
		this.authorisedServices = authorisedServices;
	}

	public String getAuthorisedPages() {
		return authorisedPages;
	}

	public void setAuthorisedPages(String authorisedPages) {
		this.authorisedPages = authorisedPages;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
