package com.dataupload.service;

import java.util.Date;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dataupload.entity.DataUpload;
import com.dataupload.entity.DataUploadAuditTrial;
import com.dataupload.repository.DataUploadAuditTrialRepository;
import com.dataupload.repository.DataUploadRepository;
import com.dataupload.request.DataUploadReq;

@Service
public class DataUploadService {

	@Autowired  private DataUploadRepository dataUploadRepo;
	@Autowired	private DataUploadAuditTrialRepository dataUploadATRepo;

	//Checking the dbDataUpload by Id given by RequestBody
	public DataUpload dbDU(DataUploadReq req) {
		DataUpload existing = null;
		if(req.getId()	!= null) {
			existing = dataUploadRepo.findById(req.getId()).orElseThrow(() -> new RuntimeException("DataUpload not found with id: " + req.getId()));
		}
		return existing;
	}
	
	//creating dataUpload
	public DataUpload createDU(DataUploadReq req) {
		DataUpload dUpload = new DataUpload();
		BeanUtils.copyProperties(req, dUpload);
		DataUpload du = dataUploadRepo.save(dUpload);
		dUpload.setStatus("N");
		dUpload.setProcessedAt(new Date());
		dataUploadRepo.save(du);
		return du;
	}

	//Get DataUpload by dbDataUpload by Id
	public DataUpload getDataUpload(DataUploadReq req) {
		DataUpload existing = dbDU(req);
		if("D".equals(existing.getStatus())) {
			throw new RuntimeException("DataUpload not exist with Id:"+req.getId());
		}else {
		return existing;
		}
	}

	//updating DataUpload
	public DataUpload updateDataUpload(DataUploadReq req) {
		DataUpload existing = dbDU(req); 

		if("D".equals(existing.getStatus())) {
			throw new RuntimeException("DataUpload not exists with Id:"+req.getId());
		}else {
			saveDUAT(existing);

			if (req.getId() != null) existing.setId(req.getId());
			if (req.getErrorMessage() != null) existing.setErrorMessage(req.getErrorMessage());
			if (req.getFileName() != null) existing.setFileName(req.getFileName());
			if (req.getFileType() != null) existing.setFileType(req.getFileType());
			if (req.getProcessedAt() != null) existing.setProcessedAt(req.getProcessedAt());
			if (req.getRecordCount() != null) existing.setRecordCount(req.getRecordCount());
			if (req.getRemarks() != null) existing.setRemarks(req.getRemarks());
			if (req.getSourceModule() != null) existing.setSourceModule(req.getSourceModule());
			if (req.getUploadedBy() != null) existing.setUploadedBy(req.getUploadedBy());
			if (req.getUploadStatus() != null) existing.setUploadStatus(req.getUploadStatus());

			existing.setStatus("U");
			existing.setUpdatedAt(new Date());
			DataUpload updatedDataUpload = dataUploadRepo.save(existing);

			return updatedDataUpload;
		}
	}

	//Deleting DataUpload
	public DataUpload deleteDU(DataUploadReq req) {
		DataUpload existing = dbDU(req);
		if("D".equals(existing.getStatus())) {
			throw new RuntimeException("DataUpload not exists with Id:"+req.getId());
		}else {
			saveDUAT(existing);
			existing.setStatus("D");
			existing.setUpdatedAt(new Date());
			DataUpload deletedDU = dataUploadRepo.save(existing);
			return deletedDU;
		}

	}

	//DataUploadAuditTrail
	private void saveDUAT(DataUpload DU) {
		DataUploadAuditTrial DUAT = new DataUploadAuditTrial();
		BeanUtils.copyProperties(DU, DUAT);
		dataUploadATRepo.save(DUAT);
	}

}
