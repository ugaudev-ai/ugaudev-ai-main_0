package com.payment.service;

import java.util.Date;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.payment.entity.Payment;
import com.payment.entity.PaymentAuditTrial;
import com.payment.repository.PaymentAuditTrialRepository;
import com.payment.repository.PaymentRepository;
import com.payment.request.PaymentReq;

@Service
public class PaymentService {
	
	@Autowired private PaymentRepository paymentRepo;
	@Autowired private PaymentAuditTrialRepository paymentATRepo;
	
	//Checking the dbPayment by Id given by RequestBody
		public Payment dbPayment(PaymentReq req) {
			Payment existing = null;
			if(req.getId()	!= null) {
				existing = paymentRepo.findById(req.getId()).orElseThrow(() -> new RuntimeException("Payment not found with id: " + req.getId()));
			}
			return existing;
		}
		
		public Payment createPayment(PaymentReq req) {
			Payment payment = new Payment();
			BeanUtils.copyProperties(req, payment);
			payment.setStatus("N");
			payment.setCreatedAt(new Date());
			Payment pmt = paymentRepo.save(payment);
			return pmt;
		}
		
		//Get Payment by dbPayment
		public Payment getPayment(PaymentReq req) {
			Payment existing = dbPayment(req);
			if("D".equals(existing.getStatus())) {
				throw new RuntimeException("Payment data not exist with Id:"+req.getId());
			}else {
			return existing;
			}
		}

		//updating Payment
		public Payment updatePayment(PaymentReq req) {
			Payment existing = dbPayment(req); 
			if("D".equals(existing.getStatus())) {
				throw new RuntimeException("Payment data not exist with Id:"+req.getId());
			}else {
			savePaymentAT(existing);

			if (req.getId() != null) existing.setId(req.getId());
			if (req.getAmount() != null) existing.setAmount(req.getAmount());
			if (req.getCreatedAt() != null) existing.setCreatedAt(req.getCreatedAt());
			if (req.getCurrency() != null) existing.setCurrency(req.getCurrency());
			if (req.getPayerEmail() != null) existing.setPayerEmail(req.getPayerEmail());
			if (req.getPayerName() != null) existing.setPayerName(req.getPayerName());
//			if (req.getPaymentDate() != null) existing.setPaymentDate(req.getPaymentDate());
			if (req.getReferenceNumber() != null) existing.setReferenceNumber(req.getReferenceNumber());
			if (req.getRemarks() != null) existing.setRemarks(req.getRemarks());
			if (req.getP_status() != null) existing.setP_status(req.getP_status());
			if (req.getPaymentMethod() != null) existing.setPaymentMethod(req.getPaymentMethod());
			if(req.getPaymentDate() != null ) existing.setPaymentDate(req.getPaymentDate());
			
			existing.setStatus("U");
			existing.setUpdatedAt(new Date());
			Payment updatedPayment = paymentRepo.save(existing);

			return updatedPayment;
			}
		}

		//Deleting Payment
		public Payment deletePayment(PaymentReq req) {
			Payment existing = dbPayment(req);
			if("D".equals(existing.getStatus())) {
				throw new RuntimeException("Payment data not exist with Id:"+req.getId());
			}else {
			savePaymentAT(existing);

			existing.setStatus("D");
			existing.setUpdatedAt(new Date());
			Payment deletedPayment = paymentRepo.save(existing);
			return deletedPayment;
			}

		}

		private void savePaymentAT(Payment existing) {
			PaymentAuditTrial PaymentAT = new PaymentAuditTrial();
			BeanUtils.copyProperties(existing, PaymentAT);
			paymentATRepo.save(PaymentAT);
		}

}
