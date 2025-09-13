package com.admin.service;

import java.util.Date;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.admin.entity.Admin;
import com.admin.entity.AdminAuditTrial;
import com.admin.repository.AdminAuditTrialRepository;
import com.admin.repository.AdminRepository;
import com.admin.request.AdminReq;

@Service
public class AdminService {

	@Autowired  private AdminRepository adminRepo;
	@Autowired	private AdminAuditTrialRepository adminATRepo;

	//Checking the dbAdmin by Id given by RequestBody
	public Admin dbadm(AdminReq req) {
		Admin adm = null;
		if(req.getId()	!= null) {
			adm = adminRepo.findById(req.getId()).orElseThrow(() -> new RuntimeException("Admin not found with id: " + req.getId()));
	}
		return adm;
	}
	
	public Admin createAdm(Admin adm) {
		if (adminRepo.existsByUsername(adm.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
		Admin admin = adminRepo.save(adm);
		Date date = new Date();
		admin.setCreatedAt(date);
		admin.setStatus("N");
		Admin admin2 = adminRepo.save(admin);
		return admin2;
	}

	//Get Admin by dbAdmin by Id
	public Admin getAdmin(AdminReq req) {
		Admin existing = dbadm(req);
		if("D".equals(existing.getStatus())) {
			throw new RuntimeException("Admin not found with Id");
		}else {
		return existing;
		}
	}

	//updating Admin
	public Admin updateAdmin(AdminReq req) {
		Admin existing = dbadm(req); 

		if("D".equals(existing.getStatus())) {
			throw new RuntimeException("Admin not Found");
		}else {
		saveAdmAT(existing);

		if (req.getId() != null) existing.setId(req.getId());
		if (req.getEmail() != null) existing.setEmail(req.getEmail());
		if (req.getPhoneNumber() != null) existing.setPhoneNumber(req.getPhoneNumber());
		if (req.getFullName() != null) existing.setFullName(req.getFullName());
		if (req.getLastLogin() != null) existing.setLastLogin(req.getLastLogin());
		if (req.getPassword() != null) existing.setPassword(req.getPassword());
		if (req.getProfileImage() != null) existing.setProfileImage(req.getProfileImage());
		if (req.getRole() != null) existing.setRole(req.getRole());
		if (req.getUsername() != null) existing.setUsername(req.getUsername());
		if(req.getAdm_status() != null)  existing.setAdm_status(req.getAdm_status());
		if(req.getCreatedAt() != null) existing.setCreatedAt(req.getCreatedAt());
		
		Admin updatedAdmin = adminRepo.save(existing);
		updatedAdmin.setStatus("U");
		Date date = new Date();
		updatedAdmin.setUpdatedAt(date);
		Admin admin = adminRepo.save(updatedAdmin);
		return admin;
		}
	}

	//Deleting Admin
	public Admin deleteadm(AdminReq req) {
		Admin existing = dbadm(req);
		
		if("D".equals(existing.getStatus())) {
			throw new RuntimeException("Admin Not Found");
		}else {
		saveAdmAT(existing);
		
		Admin deletedadm = adminRepo.save(existing);
		deletedadm.setStatus("D");
		Admin admin = adminRepo.save(deletedadm);
		return admin;
		}
	}

	//AdminAuditTrail
	private void saveAdmAT(Admin adm) {
		AdminAuditTrial admAT = new AdminAuditTrial();
		BeanUtils.copyProperties(adm, admAT);
		adminATRepo.save(admAT);
	}

}
