package com.product.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.product.entity.ProductPrincipalMap;

public interface ProductPrincipalMapRepository extends CrudRepository<ProductPrincipalMap, Long>{

	Optional<ProductPrincipalMap> findByProductCode(String productCode);

	List<ProductPrincipalMap> findAll(Pageable pageable);

	Page<ProductPrincipalMap> findByStatusNot(String string, Pageable pageable);

}
