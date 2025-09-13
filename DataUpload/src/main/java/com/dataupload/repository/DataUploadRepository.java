package com.dataupload.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.dataupload.entity.DataUpload;

public interface DataUploadRepository extends CrudRepository<DataUpload, Long>{

	List<DataUpload> findAll(Pageable pageable);

	Page<DataUpload> findByStatusNot(String string, Pageable pageable);

}
