package com.customer.service;

import java.util.Date;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.customer.entity.Address;
import com.customer.entity.AddressAuditTrial;
import com.customer.entity.Customer;
import com.customer.entity.CustomerAuditTrial;
import com.customer.repository.AddressAuditTrialRepository;
import com.customer.repository.AddressRepository;
import com.customer.repository.CustomerAuditTrialRepository;
import com.customer.repository.CustomerRepository;
import com.customer.request.AddressReq;
import com.customer.request.CustomerReq;


@Service
public class CustomerService{

	@Autowired	private CustomerRepository custRepo;
	@Autowired 	private CustomerAuditTrialRepository custATRepo;
	@Autowired 	private AddressRepository addressRepo;
	@Autowired 	private AddressAuditTrialRepository addATRepo;

	public Customer dbCust(CustomerReq req) {
		Customer cust = null;
		if(req.getId()	!= null) {
			cust = custRepo.findById(req.getId()).orElseThrow(() -> new RuntimeException("Customer not found with id: " + req.getId()));
		}else if(req.getName() != null) {
			cust = custRepo.findByName(req.getName()).orElseThrow(() -> new RuntimeException("Customer not found with Name: " + req.getName()));
		}

		return cust;
	}

	public Address dbAdd(AddressReq req) {
		Address add = null;
		if(req.getId() != null) {
			add = addressRepo.findById(req.getId()).orElseThrow(() -> new RuntimeException("Address not found with id: " + req.getId())); 
		}
		return add;
	}


	public Customer getCustomer(CustomerReq req) {
		Customer cust = dbCust(req);
		return cust;
	}

	public Customer createCust(CustomerReq req) {
		Customer cust = new Customer();
		BeanUtils.copyProperties(req, cust);
		Customer customer = custRepo.save(cust);
		customer.setStatus("N");
		customer.setCr_date(new Date());
		if (req.getAddresses() != null) {
			for (AddressReq addReq : req.getAddresses()) {
				Address addr = new Address();
				BeanUtils.copyProperties(addReq, addr);
				addr.setCustomer(customer);
				addr.setStatus("N");
				addr.setCreatedDate(new Date());
				customer.getAddresses().add(addr);
				addressRepo.save(addr);
			}
		}

		Customer customer2 = custRepo.save(customer);
		return customer2;
	}

	public Customer updateCust(CustomerReq req) {
		Customer existing = dbCust(req);

		if("D".equals(existing.getStatus())) {
			throw new RuntimeException("Customer not found");
		}else {
			custAT(existing, req);

			if (req.getId() != null) existing.setId(req.getId());
			if (req.getName() != null) existing.setName(req.getName());
			if (req.getEmail() != null) existing.setEmail(req.getEmail());
			if (req.getPhone() != null) existing.setPhone(req.getPhone());

			existing.setStatus("U");
			existing.setUpdt_date(new Date());
			if (req.getAddresses() != null) {
				for (AddressReq addReq : req.getAddresses()) {
					Address add;
					if (addReq.getId() != null) {
						add = updateAdd(addReq);
					}else {
						add = new Address();
						BeanUtils.copyProperties(addReq, add);
						add.setStatus("N");
						add.setCreatedDate(new Date());
						add.setCustomer(existing);
						existing.getAddresses().add(add);

					}
					addressRepo.save(add);
				}
			}

			Customer customer = custRepo.save(existing);
			return customer;
		}
	}

	public Customer deleteCust(CustomerReq req) {
		Customer existing = dbCust(req);

		if("D".equals(existing.getStatus())) {
			throw new RuntimeException("Customer not found");
		}else {
			custAT(existing, req);
			existing.setStatus("D");
			existing.setUpdt_date(new Date());
			if (req.getAddresses() != null) {
				for (AddressReq addReq : req.getAddresses()) {
					Address add;
					add = deleteAdd(addReq);
					addressRepo.save(add);
				}
			}

			Customer customer = custRepo.save(existing);
			return customer;
		}
	}

	public Address updateAdd(AddressReq req) {
		Address existing = dbAdd(req);

		if("D".equals(existing.getStatus())) {
			throw new RuntimeException("Address not found with Id");
		}else {
			addAT(existing, req);
			// Partial update for address
			if ((req.getId() != null)) existing.setId(req.getId());
			if ((req.getAddressType() != null)) existing.setAddressType(req.getAddressType());
			if ((req.getAddress_1() != null)) existing.setAddress_1(req.getAddress_1());
			if (req.getAddress_2() != null) existing.setAddress_2(req.getAddress_2());
			if (req.getAddress_3() != null) existing.setAddress_3(req.getAddress_3());
			if (req.getLandMark() != null) existing.setLandMark(req.getLandMark());
			if (req.getLocality() != null) existing.setLocality(req.getLocality());
			if (req.getCity() != null) existing.setCity(req.getCity());
			if (req.getState() != null) existing.setState(req.getState());
			if (req.getStreetNo() != null) existing.setStreetNo(req.getStreetNo());
			if (req.getPincode() != null) existing.setPincode(req.getPincode());
			existing.setUpdatedDate(new Date());
			existing.setStatus("U");

		}
		return addressRepo.save(existing);
	}

	public Address deleteAdd(AddressReq req) {
		Address existing = dbAdd(req);

		if("D".equals(existing.getStatus())) {
			throw new RuntimeException("Address not found with Id");
		}else {
			addAT(existing, req);
			existing.setStatus("D");
			existing.setUpdatedDate(new Date());
		}
		return addressRepo.save(existing);
	}
	
	private void custAT(Customer customer, CustomerReq req) {
		CustomerAuditTrial custAt = new CustomerAuditTrial();
		BeanUtils.copyProperties(customer, custAt);
		custAt.setCr_description(req.getCr_description());
		custATRepo.save(custAt);
	}
	
	private void addAT(Address address, AddressReq addReq) {
		AddressAuditTrial addAT = new AddressAuditTrial();
		BeanUtils.copyProperties(address, addAT);
		addAT.setAddreq_description(addReq.getAddreq_description());
		addATRepo.save(addAT);
	}
}
