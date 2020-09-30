package com.teambbank.standalonedemo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.teambbank.standalonedemo.entity.BankAccountEntity;
import com.teambbank.standalonedemo.model.BankAccountType;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccountEntity, Long> {

	Optional<BankAccountEntity> findByName(String name);

	Optional<List<BankAccountEntity>> findByAccountType(BankAccountType type);
}
