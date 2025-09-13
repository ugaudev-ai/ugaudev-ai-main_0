package com.report.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.report.entity.Report;

public interface ReportRepository extends CrudRepository<Report, Long>{

	Optional<Report> findByReportName(String reportName);

	List<Report> findAll(Pageable pageable);

	Page<Report> findByStatusNot(String string, Pageable pageable);
	

}
