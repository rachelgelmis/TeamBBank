package com.teambbank.standalonedemo.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.teambbank.standalonedemo.model.BankAccountType;

@Entity(name = "BankAccountEntity")
@Table(name = "BANK_ACCOUNTS")
public class BankAccountEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name = "name", nullable = false, length = 64)
	private String name;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "customer_id", nullable = false)
	private CustomerEntity customer;

	@Column(name = "account_type", nullable = false)
	private BankAccountType accountType;

	// We leave the temporal type as default implicitly because Date has enough
	// precision (millisecond)
	@Column(name = "creation_date", nullable = false)
	private Date creationDate;

	@Column(name = "balance") // balance is not nullable since its a primitive
	private long balance;

	@Column(name = "interest_rate")
	private float interestRate;
	
	@Column(name = "min_balance", nullable = true)
	private long minBalance;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<TransactionEntity> transactions;

	public BankAccountEntity() {
		this.transactions = new HashSet<>();
	}

	// Getters
	public long getId() {
		return this.id;
	}

	public String getName() {
		return name;
	}

	public CustomerEntity getCustomer() {
		return customer;
	}

	public BankAccountType getAccountType() {
		return accountType;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public long getBalance() {
		return balance;
	}

	public float getInterestRate() {
		return interestRate;
	}

	public long getMinimumBalance() {
		return minBalance;
	}
	
	public List<TransactionEntity> getTransactions() {
		return new ArrayList<>(transactions);
	}

	// Setters
	public void setName(String name) {
		this.name = name;
	}

	public void setCustomer(CustomerEntity customer) {
		this.customer = customer;
	}

	public void setAccountType(BankAccountType accountType) {
		this.accountType = accountType;
	}

	public void setBalance(long balance) {
		this.balance = balance;
	}

	public void setInterestRate(float interestRate) {
		this.interestRate = interestRate;
	}
	
	public void setMinimumBalance(long min) {
		this.minBalance = min;
	}

	public void setCreationDate(Date date) {
		this.creationDate = date;
	}

	public void addTransaction(TransactionEntity transaction) {
		transactions.add(transaction);
	}

	public void removeTransaction(TransactionEntity transaction) {
		transactions.remove(transaction);
	}
	
	/*==================================================
	 * Function: hasAllInfo()
	 * Checks if all required fields contain values.
	 * param: none
	 * return: bool that determines if all requirements
	 * are met
	 =================================================*/
	public boolean hasAllInfo() {
		if (name != null && customer != null && accountType != null) {
			return true;
		}
		return false;
	}

	public void withdraw(int ammount) {
		balance = balance - ammount;
	}

	public void deposit(int ammount) {
		balance = balance + ammount;
	}

	@Override
	public String toString() {
		return getName() + " - $ " + getBalance();
	}

}
