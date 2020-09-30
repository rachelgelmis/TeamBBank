package com.teambbank.standalonedemo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.teambbank.standalonedemo.entity.CustomerEntity;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {
	Optional<CustomerEntity> findByName(String name);
	
	Optional<CustomerEntity> findByUsername(String username);
	
	Optional<CustomerEntity> findByfName(String fName);
	
	Optional<CustomerEntity> findBylName(String lName);
	
	Optional<List<CustomerEntity>> findByNameOrUsername(String name, String username);
	
	//Optional<List<CustomerEntity>> findByNameOrUsernameOrlNameOrfName(String name, String username, String lName, String fName);

	//Optional<List<CustomerEntity>> findBylNameOrByfName(String lName, String fName);
}
