package com.teambbank.standalonedemo.entity;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.teambbank.standalonedemo.model.TransactionType;

@Entity
@Table(name = "TRANSACTION")
public class TransactionEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bank_account_id", nullable = false)
	private BankAccountEntity account;
	
	@Column(name = "transaction_type", nullable = false)
	private TransactionType transactionType;
	
	// Instant class gives us nanosecond precision and has an implicit TemporalType
	@Column(name = "timestamp", nullable = false)
	private Instant timestamp;
	
	@Column(name = "amount") //amount is not nullable since its a primitive
	private long amount;

	public TransactionEntity() {
		// Empty constructor
	}

	// Getters
	public BankAccountEntity getBankAccount() {
		return account;
	}

	public TransactionType getTransactionType() {
		return transactionType;
	}

	public Instant getTimestamp() {
		return timestamp;
	}

	public long getAmount() {
		return amount;
	}

	// Setters
	public void setBankAccount(BankAccountEntity account) {
		this.account = account;
	}

	public void setTransactionType(TransactionType type) {
		this.transactionType = type;
	}

	public void setTimestamp(Instant timestamp) {
		this.timestamp = timestamp;
	}

	public void setAmount(long amount) {
		this.amount = amount;
	}

	/*==================================================
	 * Function: hasAllInfo()
	 * Checks if all required fields contain values.
	 * param: none
	 * return: bool that determines if all requirements
	 * are met
	 =================================================*/
	public boolean hasAllInfo() {
		if (account != null && transactionType != null && timestamp != null) {
			return true;
		}
		return false;
	}
	
	@Override
	public String toString() {
		return transactionType + ";" + timestamp + ";" + amount + ";" + id;
	}
}