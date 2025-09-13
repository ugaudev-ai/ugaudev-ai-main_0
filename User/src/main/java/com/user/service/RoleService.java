package com.user.service;

import java.util.Date;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.user.entity.Role;
import com.user.entity.RoleAuditTrial;
import com.user.repository.RoleAuditTrialRepository;
import com.user.repository.RoleRepository;
import com.user.request.RoleReq;

@Service
public class RoleService {

	@Autowired private RoleRepository roleRepo;
	@Autowired private RoleAuditTrialRepository roleATRepo;

	//Checking the dbRole by Id given by RequestBody
	public Role dbRole(RoleReq req) {
		Role existing = null;
		if(req.getId()	!= null) {
			existing = roleRepo.findById(req.getId()).orElseThrow(() -> new RuntimeException("Role not found with id: " + req.getId()));
		}else if(req.getRole() != null) {
			existing = roleRepo.findByRole(req.getRole()).orElseThrow(() -> new RuntimeException("Role not found with Role: " + req.getRole()));
		}
		return existing;
	}

	public Role createRole(RoleReq req) {
		Role role = new Role();
		BeanUtils.copyProperties(req, role);
		role.setStatus("N");
		role.setCreatedDate(new Date());
		Role createdRole = roleRepo.save(role);
		return createdRole;
	}

	//Get Role by dbRole by Id
	public Role getRole(RoleReq req) {
		Role existing = dbRole(req);
		if("D".equals(existing.getStatus())) {
			throw new RuntimeException("Role not found!");
		}else {
			return existing;
		}
	}

	//updating Role
	public Role updateRole(RoleReq req) {
		Role existing = dbRole(req); 
		if("D".equals(existing.getStatus())) {
			throw new RuntimeException("Role not found!");
		}else {
			saveRoleAT(existing);

			if (req.getAuthorisedPages() != null) existing.setAuthorisedPages(req.getAuthorisedPages());
			if (req.getAuthorisedServices() != null) existing.setAuthorisedServices(req.getAuthorisedServices());
			if (req.getRole() != null) existing.setRole(req.getRole());

			existing.setStatus("U");
			existing.setUpdateDate(new Date());
			Role updatedRole = roleRepo.save(existing);

			return updatedRole;
		}
	}

	//Deleting Enquiry
	public Role deleteRole(RoleReq req) {
		Role existing = dbRole(req);
		if("D".equals(existing.getStatus())) {
			throw new RuntimeException("Role not found!");
		}else {
			saveRoleAT(existing);

			existing.setStatus("D");
			existing.setUpdateDate(new Date());
			Role deletedRole = roleRepo.save(existing);
			return deletedRole;
		}
	}

	private void saveRoleAT(Role existing) {
		RoleAuditTrial roleAT = new RoleAuditTrial();
		BeanUtils.copyProperties(existing, roleAT);
		roleATRepo.save(roleAT);
	}


}
