package com.user.service;

import java.util.Date;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.user.entity.User;
import com.user.entity.UserAuditTrial;
import com.user.repository.UserAuditTrialRepository;
import com.user.repository.UserRepository;
import com.user.request.UserReq;

@Service
public class UserService {

	@Autowired private UserRepository userRepo;
	@Autowired private UserAuditTrialRepository userATRepo;
	
	//Checking the dbUser by Id given by RequestBody
	public User dbUser(UserReq req) {
		User existing = null;
		if(req.getId()	!= null) {
			existing = userRepo.findById(req.getId()).orElseThrow(() -> new RuntimeException("User not found with id: " + req.getId()));
		}else if(req.getFullName() != null) {
			existing = userRepo.findByFullName(req.getFullName()).orElseThrow(() -> new RuntimeException("User not found with FullName: " + req.getFullName()));
		}
		return existing;
	}
	
	public User createUser(UserReq req) {
		User user = new User();
		BeanUtils.copyProperties(req, user);
		user.setStatus("N");
		user.setCreatedAt(new Date());
		return userRepo.save(user);
	}

	//Get User by dbUser by Id
	public User getUser(UserReq req) {
		User existing = dbUser(req);
		if("D".equals(existing.getStatus())) {
			throw new RuntimeException("User not found!");
		}else {
		return existing;
		}
	}
	
	//updating User
	public User updateUser(UserReq req) {
		User existing = dbUser(req); 
		if("D".equals(existing.getStatus())) {
			throw new RuntimeException("User not found!");
		}else {
		saveUserAT(existing);

		if (req.getFullName() != null) existing.setFullName(req.getFullName());
		if (req.getEmail() != null) existing.setEmail(req.getEmail());
		if (req.getCreatedAt() != null) existing.setCreatedAt(req.getCreatedAt());
		if (req.getLastLogin() != null) existing.setLastLogin(req.getLastLogin());
		if (req.getPassword() != null) existing.setPassword(req.getPassword());
		if (req.getPhoneNumber() != null) existing.setPhoneNumber(req.getPhoneNumber());
		if (req.getUpdatedAt() != null) existing.setUpdatedAt(req.getUpdatedAt());
		if (req.getRole() != null) existing.setRole(req.getRole());
		if (req.getUsername() != null) existing.setUsername(req.getUsername());
		if (req.getUsr_sts() != null) existing.setUsr_sts(req.getUsr_sts());
		existing.setStatus("U");
		existing.setUpdatedAt(new Date());
		User updatedUser = userRepo.save(existing);

		return updatedUser;
		}
	}

	//Deleting Enquiry
	public User deleteUser(UserReq req) {
		User existing = dbUser(req);
		if("D".equals(existing.getStatus())) {
			throw new RuntimeException("User not found!");
		}else {
		saveUserAT(existing);

		existing.setStatus("D");
		existing.setUpdatedAt(new Date());
		User deletedUser = userRepo.save(existing);
		return deletedUser;
		}
	}

	private void saveUserAT(User existing) {
		UserAuditTrial UserAT = new UserAuditTrial();
		BeanUtils.copyProperties(existing, UserAT);
		userATRepo.save(UserAT);
	}
}
