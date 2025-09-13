package com.customer.request;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotEmpty;

public class CustomerReq {
	
	private Long id;//Not used to create new record
	
	private Long cr_no;
	
	private String cr_desciption;
	
	//@NotNull(message = "Name should not null")
	@NotEmpty(message = "name should not blank")
	private String name;
	
	private String email;
	
	private String phone;
	
	private String status;
	
	@Column(name="creation_date")
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Asia/Kolkata")
	private Date cr_date;

	@Column(name = "updated_date")
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Asia/Kolkata")
	private Date updt_date;
	
	private List<AddressReq> addresses = new ArrayList<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCr_desciption() {
		return cr_desciption;
	}

	public void setCr_desciption(String cr_desciption) {
		this.cr_desciption = cr_desciption;
	}

	public List<AddressReq> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<AddressReq> addresses) {
		this.addresses = addresses;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getCr_date() {
		return cr_date;
	}

	public void setCr_date(Date cr_date) {
		this.cr_date = cr_date;
	}

	public Date getUpdt_date() {
		return updt_date;
	}

	public void setUpdt_date(Date updt_date) {
		this.updt_date = updt_date;
	}
	
	public Long getCr_no() {
		return cr_no;
	}

	public void setCr_no(Long cr_no) {
		this.cr_no = cr_no;
	}

	public String getCr_description() {
		return cr_desciption;
	}

	public void setCr_description(String cr_desciption) {
		this.cr_desciption = cr_desciption;
	}

}
