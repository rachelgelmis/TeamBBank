package com.teambbank.standalonedemo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.teambbank.standalonedemo.entity.TransactionEntity;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
	
}