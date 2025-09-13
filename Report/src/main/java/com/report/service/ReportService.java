package com.report.service;

import java.util.Date;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.report.entity.Report;
import com.report.entity.ReportAuditTrial;
import com.report.repository.ReportAuditTrialRepository;
import com.report.repository.ReportRepository;
import com.report.request.ReportReq;

@Service
public class ReportService {

	@Autowired  private ReportRepository reportRepo;
	@Autowired	private ReportAuditTrialRepository reportATRepo;

	//Checking the dbReport by Id/CustomerName given by RequestBody
	public Report dbRpt(ReportReq req) {
		Report rpt = null;
		if(req.getId()	!= null) {
			rpt = reportRepo.findById(req.getId()).orElseThrow(() -> new RuntimeException("Report not found with id: " + req.getId()));
		}else if(req.getReportName() != null) {
			rpt = reportRepo.findByReportName(req.getReportName()).orElseThrow(() -> new RuntimeException("Report not found with CustomerName: " + req.getReportName()));
		}

		return rpt;
	}
	
	public Report createReport(ReportReq req) {
		Report report = new Report();
		BeanUtils.copyProperties(req, report);
		report.setStatus("N");
		report.setCreatedAt(new Date());
		report.setGeneratedAt(new Date());
		return reportRepo.save(report);
	}
	

	//Get Report by dbReport by Id/CustomerName
	public Report getReport(ReportReq req) {
		Report existing = dbRpt(req);
		if("D".equals(existing.getStatus())) {
			throw new RuntimeException("Report not found with Id:"+req.getId());
		}else {
		return existing;
		}
	}

	//updating Report
	public Report updateReport(ReportReq req) {
		Report existing = dbRpt(req); 
		if("D".equals(existing.getStatus())) {
			throw new RuntimeException("Report not found with Id:"+req.getId());
		}else {
		saveRptAT(existing);

		if (req.getId() != null) existing.setId(req.getId());
		if (req.getIsArchived() != null) existing.setIsArchived(req.getIsArchived());
		if (req.getDescription() != null) existing.setDescription(req.getDescription());
		if (req.getFilePath() != null) existing.setFilePath(req.getFilePath());
		if (req.getFormat() != null) existing.setFormat(req.getFormat());
		if (req.getFromDate() != null) existing.setFromDate(req.getFromDate());
		if (req.getGeneratedAt() != null) existing.setGeneratedAt(req.getGeneratedAt());
		if (req.getGeneratedBy() != null) existing.setGeneratedBy(req.getGeneratedBy());
		if (req.getReportName() != null) existing.setReportName(req.getReportName());
		if (req.getReportType() != null) existing.setReportType(req.getReportType());
		if (req.getRpt_status() != null) existing.setRpt_status(req.getRpt_status());
		if (req.getToDate() != null) existing.setToDate(req.getToDate());

		existing.setStatus("U");
		existing.setUpdateAt(new Date());
		Report updatedReport = reportRepo.save(existing);

		return updatedReport;
		}

	}

	//Deleting Report
	public Report deleteRpt(ReportReq req) {
		Report existing = dbRpt(req);
		if("D".equals(existing.getStatus())) {
			throw new RuntimeException("Report not found with Id:"+req.getId());
		}else {
		saveRptAT(existing);
		
		existing.setStatus("D");
		existing.setUpdateAt(new Date());
		Report deletedRpt = reportRepo.save(existing);
		return deletedRpt;
		}
	}

	//ReportAuditTrail
	private void saveRptAT(Report Rpt) {
		ReportAuditTrial RptAT = new ReportAuditTrial();
		BeanUtils.copyProperties(Rpt, RptAT);
		reportATRepo.save(RptAT);
	}
}
