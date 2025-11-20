package com.admin.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.admin.entity.Admin;
import com.admin.entity.AdminAuditTrial;
import com.admin.enums.AdminRole;
import com.admin.enums.AdminStatus;
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
	
	public List<Admin> saveAll(List<AdminReq> adminDTOList) {
        if (adminDTOList == null || adminDTOList.isEmpty()) {
            throw new IllegalArgumentException("Admin list cannot be empty");
        }

        // Convert DTOs to Entities
        List<Admin> admins = adminDTOList.stream()
                .map(this::convertToEntity)
                .collect(Collectors.toList());

        // Bulk save
        adminRepo.saveAll(admins);
		return admins;
    }
	private Admin convertToEntity(AdminReq dto) {
        Admin a = new Admin();
        a.setUsername(dto.getUsername());
        a.setEmail(dto.getEmail());
        a.setPhoneNumber(dto.getPhoneNumber());
        a.setPassword(dto.getPassword());
        a.setFullName(dto.getFullName());
        a.setAdm_status(dto.getAdm_status());
        a.setRole(dto.getRole());
        a.setLastLogin(dto.getLastLogin());
//        c.setAddresses(dto.getAddresses());
        // map more fields as needed
        return a;
    }
	
	public List<Admin> importAdminsFromFile(MultipartFile file) throws Exception {
	    String filename = file.getOriginalFilename();

	    if (filename == null) {
	        throw new IllegalArgumentException("Filename is missing.");
	    }

	    if (filename.endsWith(".xlsx") || filename.endsWith(".xls")) {
	        return importAdminsFromExcel(file);
	    } else if (filename.endsWith(".csv")) {
	        return importAdminsFromCSV(file);
	    } else {
	        throw new IllegalArgumentException("Unsupported file format. Only .xlsx, .xls, and .csv are allowed.");
	    }
	}
	
	public List<Admin> importAdminsFromExcel(MultipartFile file) throws Exception {
        List<Admin> admins = new ArrayList<>();

        try (InputStream is = file.getInputStream(); 
        	Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            boolean firstRow = true;

            for (Row row : sheet) {
                if (firstRow) {
                    firstRow = false; // Skip header row
                    continue;
                }

                String username = getCellValue(row, 0);
                String email = getCellValue(row, 1);
                String password = getCellValue(row, 2);
                String fullName = getCellValue(row, 3);
                String phoneNumber = getCellValue(row, 4);
//                String profileImage = getCellValue(row, 5);
                String roleStr = getCellValue(row, 5);
                String admStatusStr = getCellValue(row, 6);

                if (username == null || username.trim().isEmpty()) {
                    System.out.println("Skipping row " + row.getRowNum() + " — missing username");
                    continue;
                }

                AdminRole role = null;
                AdminStatus admStatus = null;

                try {
                    role = AdminRole.valueOf(roleStr.toUpperCase());
                } catch (IllegalArgumentException e) {
                    throw new RuntimeException("Invalid role value at row " + row.getRowNum());
                }

                try {
                    admStatus = AdminStatus.valueOf(admStatusStr.toUpperCase());
                } catch (IllegalArgumentException e) {
                    throw new RuntimeException("Invalid admin status value at row " + row.getRowNum());
                }

                Admin admin = new Admin();
                admin.setUsername(username);
                admin.setEmail(email);
                admin.setPassword(password);
                admin.setFullName(fullName);
                admin.setPhoneNumber(phoneNumber);
//                admin.setProfileImage(profileImage);
                admin.setRole(role);
                admin.setAdm_status(admStatus);
                admin.setCreatedAt(new Date());
//                admin.setUpdatedAt(new Date());
                admin.setLastLogin(null);
                admin.setStatus("N");

                admins.add(adminRepo.save(admin));
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to import Admins from Excel: " + e.getMessage());
        }

        return admins;
    }

    // ✅ Utility method for reading cell values
    private String getCellValue(Row row, int cellNum) {
        Cell cell = row.getCell(cellNum);
        if (cell == null) return "";

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                return String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            case BLANK:
            default:
                return "";
        }
    }
    
    public List<Admin> importAdminsFromCSV(MultipartFile file) throws Exception {
        List<Admin> admins = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            boolean firstRow = true;

            while ((line = reader.readLine()) != null) {
                if (firstRow) {
                    firstRow = false; // Skip header
                    continue;
                }

                // Split line by commas — assumes a clean CSV with no quoted commas
                String[] fields = line.split(",", -1); // -1 keeps trailing empty strings

                if (fields.length < 8) {
                    System.out.println("Skipping row due to insufficient data: " + line);
                    continue;
                }

                String username = fields[0].trim();
                String email = fields[1].trim();
                String password = fields[2].trim();
                String fullName = fields[3].trim();
                String phoneNumber = fields[4].trim();
//                String profileImage = fields[5].trim();
                String roleStr = fields[5].trim();
                String admStatusStr = fields[6].trim();

                if (username.isEmpty()) {
                    System.out.println("Skipping row — missing username: " + line);
                    continue;
                }

                AdminRole role;
                AdminStatus admStatus;

                try {
                    role = AdminRole.valueOf(roleStr.toUpperCase());
                } catch (IllegalArgumentException e) {
                    throw new RuntimeException("Invalid role: " + roleStr + " in line: " + line);
                }

                try {
                    admStatus = AdminStatus.valueOf(admStatusStr.toUpperCase());
                } catch (IllegalArgumentException e) {
                    throw new RuntimeException("Invalid admin status: " + admStatusStr + " in line: " + line);
                }

                Admin admin = new Admin();
                admin.setUsername(username);
                admin.setEmail(email);
                admin.setPassword(password);
                admin.setFullName(fullName);
                admin.setPhoneNumber(phoneNumber);
//                admin.setProfileImage(profileImage);
                admin.setRole(role);
                admin.setAdm_status(admStatus);
                admin.setCreatedAt(new Date());
//                admin.setUpdatedAt(new Date());
                admin.setLastLogin(null);
                admin.setStatus("N");

                admins.add(adminRepo.save(admin));
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to import Admins from CSV: " + e.getMessage());
        }

        return admins;
    }


	//AdminAuditTrail
	private void saveAdmAT(Admin adm) {
		AdminAuditTrial admAT = new AdminAuditTrial();
		BeanUtils.copyProperties(adm, admAT);
		adminATRepo.save(admAT);
	}

}
