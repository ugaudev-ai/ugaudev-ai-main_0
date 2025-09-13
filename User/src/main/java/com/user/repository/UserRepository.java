package com.user.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.user.entity.User;

public interface UserRepository extends CrudRepository<User, Long>{

	List<User> findAll(Pageable pageable);

	Optional<User> findByFullName(String fullName);
	Page<User> findByStatusNot(String status, Pageable pageable);

}
