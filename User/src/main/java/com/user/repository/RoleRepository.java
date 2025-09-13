package com.user.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.user.entity.Role;

public interface RoleRepository extends CrudRepository<Role, Long>{

	Optional<Role> findByRole(String role);

	List<Role> findAll(Pageable pageable);

	Page<Role> findByStatusNot(String string, Pageable pageable);

}
