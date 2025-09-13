package com.product.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.product.entity.Product;

public interface ProductRepository  extends CrudRepository<Product, Long>{

	List<Product> findAll(Pageable pageable);

	Optional<Product> findById(Long productId);

	Product save(List<Product> products);

	Page<Product> findByStatusNot(String string, Pageable pageable);

}



