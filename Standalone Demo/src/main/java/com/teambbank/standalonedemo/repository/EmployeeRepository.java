package com.teambbank.standalonedemo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.teambbank.standalonedemo.entity.EmployeeEntity;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {
	Optional<EmployeeEntity> findByName(String name);
	
	Optional<EmployeeEntity> findById(long id);
	
	Optional<EmployeeEntity> findByUsername(String username);
	
	Optional<List<EmployeeEntity>> findByNameOrUsername(String name, String username);
}
