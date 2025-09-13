package com.product.request;

public class PrincipalReq {
	
	private Long id;
	
	private String prinReq_description;
	
    private String principalName;
    
    private String principalEmail;
    
    private String principalPhone;
    
    private String principalAddress;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPrinReq_description() {
		return prinReq_description;
	}

	public void setPrinReq_description(String prinReq_description) {
		this.prinReq_description = prinReq_description;
	}

	public String getPrincipalName() {
		return principalName;
	}

	public void setPrincipalName(String principalName) {
		this.principalName = principalName;
	}

	public String getPrincipalEmail() {
		return principalEmail;
	}

	public void setPrincipalEmail(String principalEmail) {
		this.principalEmail = principalEmail;
	}

	public String getPrincipalPhone() {
		return principalPhone;
	}

	public void setPrincipalPhone(String principalPhone) {
		this.principalPhone = principalPhone;
	}

	public String getPrincipalAddress() {
		return principalAddress;
	}

	public void setPrincipalAddress(String principalAddress) {
		this.principalAddress = principalAddress;
	}
    
}
