package com.invoice.service;

import java.util.Date;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.invoice.entity.Invoice;
import com.invoice.entity.InvoiceAuditTrial;
import com.invoice.repository.InvoiceAuditTrialRepository;
import com.invoice.repository.InvoiceRepository;
import com.invoice.request.InvoiceReq;

@Service
public class InvoiceService {

	@Autowired  private InvoiceRepository invoiceRepo;
	@Autowired	private InvoiceAuditTrialRepository invoiceATRepo;

	//Checking the dbInvoice by Id/CustomerName given by RequestBody
	public Invoice dbInv(InvoiceReq req) {
		Invoice inv = null;
		if(req.getId()	!= null) {
			inv = invoiceRepo.findById(req.getId()).orElseThrow(() -> new RuntimeException("Invoice not found with id: " + req.getId()));
		}else if(req.getInvoiceNumber()	!= null) {
			inv = invoiceRepo.findByInvoiceNumber(req.getInvoiceNumber()).orElseThrow(() -> new RuntimeException("Invoice not found with id: " + req.getInvoiceNumber()));
		}
		return inv;
	}

	public Invoice createInv(InvoiceReq req) {
		Invoice invoice = new Invoice();
		BeanUtils.copyProperties(req, invoice);
		invoice.setStatus("N");
		invoice.setCreatedAt(new Date());
		invoice.setInvoiceDate(new Date());
		return invoiceRepo.save(invoice);
	}

	//Get Invoice by dbInvoice by Id/InvoiceNumber
	public Invoice getInvoice(InvoiceReq req) {
		Invoice existing = dbInv(req);
		if("D".equals(existing.getStatus())) {
			throw new RuntimeException("Invoice data not exist for Id:"+req.getId());
		}else {
			return existing;
		}
	}

	//updating Invoice
	public Invoice updateInvoice(InvoiceReq req) {
		Invoice existing = dbInv(req); 
		if("D".equals(existing.getStatus())) {
			throw new RuntimeException("Invoice data not exist for Id:"+req.getId());
		}else {

			saveInvAT(existing);

			if (req.getId() != null) existing.setId(req.getId());
			if (req.getCurrency() != null) existing.setCurrency(req.getCurrency());
			if (req.getDiscountAmount() != null) existing.setDiscountAmount(req.getDiscountAmount());
			if (req.getDueDate() != null) existing.setDueDate(req.getDueDate());
			if (req.getInvoiceDate() != null) existing.setInvoiceDate(req.getInvoiceDate());
			if (req.getInvoiceNumber() != null) existing.setInvoiceNumber(req.getInvoiceNumber());
			if (req.getNetAmount() != null) existing.setNetAmount(req.getNetAmount());
			if (req.getTaxAmount() != null) existing.setTaxAmount(req.getTaxAmount());
			if (req.getTotalAmount() != null) existing.setTotalAmount(req.getTotalAmount());
			if (req.getRemarks() != null) existing.setRemarks(req.getRemarks());
			if(req.getInv_status() != null) existing.setInv_status(req.getInv_status());

			existing.setStatus("U");
			existing.setUpdatedAt(new Date());
			Invoice updatedInvoice = invoiceRepo.save(existing);

			return updatedInvoice;
		}
	}

	//Deleting Invoice
	public Invoice deleteInv(InvoiceReq req) {
		Invoice existing = dbInv(req);
		if("D".equals(existing.getStatus())) {
			throw new RuntimeException("Invoice data not exist for Id:"+req.getId());
		}else {
			saveInvAT(existing);
			existing.setStatus("D");
			existing.setUpdatedAt(new Date());
			Invoice deletedInv = invoiceRepo.save(existing);
			return deletedInv;
		}

	}

	//InvoiceAuditTrail
	private void saveInvAT(Invoice inv) {
		InvoiceAuditTrial invAT = new InvoiceAuditTrial();
		BeanUtils.copyProperties(inv, invAT);
		invoiceATRepo.save(invAT);
	}

}
