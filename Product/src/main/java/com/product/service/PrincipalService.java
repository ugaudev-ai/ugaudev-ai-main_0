package com.product.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.product.entity.Principal;
import com.product.entity.PrincipalAuditTrial;
import com.product.repository.PrincipalAuditTrialRepository;
import com.product.repository.PrincipalRepository;
import com.product.request.PrincipalReq;

@Service
public class PrincipalService {
	
	@Autowired	private PrincipalRepository principalRepo;
	@Autowired	private PrincipalAuditTrialRepository principalAuditRepo;
	
	private Principal princ(PrincipalReq req) {
		Principal principal = null;

		if (req.getId() != null) {
			principal = principalRepo.findById(req.getId()).orElse(null);
		} else if (req.getPrincipalName() != null) {
			principal = principalRepo.findByPrincipalName(req.getPrincipalName()).orElse(null);
		}
		return principal;
	}

	private void savedPrinAT(Principal principal, PrincipalReq prinReq) {
		
		PrincipalAuditTrial audit = new PrincipalAuditTrial();
		audit.setId(principal.getId());
		audit.setPrincipalName(principal.getPrincipalName());
		audit.setPrincipalEmail(principal.getPrincipalEmail());
		audit.setPrincipalPhone(principal.getPrincipalPhone());
		audit.setPrincipalAddress(principal.getPrincipalAddress());
		audit.setPrinReq_description(prinReq.getPrinReq_description());
		principalAuditRepo.save(audit);
	}
	
	public Principal savePrincipal(PrincipalReq req) {
		Principal principal = new Principal();
		BeanUtils.copyProperties(req, principal);
		return principalRepo.save(principal);
	}
	
	public Principal getPrincipal(PrincipalReq prinReq) {
		Principal existing = princ(prinReq);
		return existing;
	}
	
	public Principal updatePrincipal(PrincipalReq prinReq) {
		Principal existing = princ(prinReq);
		savedPrinAT(existing, prinReq);
		if (prinReq.getPrincipalName() != null) existing.setPrincipalName(prinReq.getPrincipalName());
		if (prinReq.getId() != null) existing.setId(prinReq.getId());
		if (prinReq.getPrincipalEmail() != null) existing.setPrincipalEmail(prinReq.getPrincipalEmail());
		if (prinReq.getPrincipalPhone() != null) existing.setPrincipalPhone(prinReq.getPrincipalPhone());
		if (prinReq.getPrincipalAddress() != null) existing.setPrincipalAddress(prinReq.getPrincipalAddress());
		Principal principal = principalRepo.save(existing);
		return principal;
	}
	
	public Principal deletedPrincipal(PrincipalReq prinReq) {
		Principal existing = princ(prinReq);
		savedPrinAT(existing, prinReq);
		return principalRepo.save(existing);
	}
	
}
