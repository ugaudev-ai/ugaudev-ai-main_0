package com.customer.entity;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "customers")
public class Customer {

	@Id
	@SequenceGenerator(name="cust_seq", sequenceName = "customer_sequence", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cust_seq")
	private Long id;

	@Column(name = "cust_name", unique = true)
	//@NotNull(message = "Name should not be null")
	//@NotBlank(message = "Name should not be blank")
	//@NotEmpty(message = "Name should be empty")
	private String name;

	private String email;

	private String phone;	

	@Column(name = "current_status")
	private String status;

	@Column(name="creation_date")
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Asia/Kolkata")
	private Date cr_date;

	@Column(name = "updated_date")
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Asia/Kolkata")
	private Date updt_date;

	@OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Address> addresses = new ArrayList<>();

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

	public List<Address> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<Address> addresses) {
		this.addresses = addresses;
	}

	public Customer(Long id, String name, String email, String phone, String status, Date cr_date, Date updt_date,
			List<Address> addresses) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.phone = phone;
		this.status = status;
		this.cr_date = cr_date;
		this.updt_date = updt_date;
		this.addresses = addresses;
	}

	public Customer() {
		super();
	}

	
}
