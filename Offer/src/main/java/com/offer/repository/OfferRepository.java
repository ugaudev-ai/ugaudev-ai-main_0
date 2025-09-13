package com.offer.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.offer.entity.Offer;

public interface OfferRepository  extends CrudRepository<Offer, Long>{

	List<Offer> findAll(Pageable pageable);
	Page<Offer> findByStatusNot(String status, Pageable pageable);
	
	Optional<Offer> findByOfferCode(String offerCode);

}
