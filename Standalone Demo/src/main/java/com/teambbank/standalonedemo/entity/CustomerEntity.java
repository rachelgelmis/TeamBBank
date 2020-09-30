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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.teambbank.standalonedemo.model.IUserAccount;

@Entity(name = "CustomerEntity")
@Table(name = "customer")
public class CustomerEntity implements IUserAccount {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name = "NAME", nullable = false, length = 129)
	private String name;

	@Column(name = "FIRST_NAME", nullable = false, length = 64)
	private String fName;

	@Column(name = "LAST_NAME", nullable = false, length = 64)
	private String lName;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "customer")
	private Set<BankAccountEntity> bankAccounts;

	@Column(name = "SSN", nullable = false, length = 11)
	private String socialSecurityNumber;

	@Column(name = "ADDRESS", nullable = false, length = 128)
	private String address;

	@Column(name = "BIRTH_DATE", nullable = false, length = 8)
	private Date dateOfBirth;

	@Column(name = "PHONE_NUMBER", nullable = false, length = 16)
	private String phone;

	@Column(name = "USERNAME", nullable = false, length = 64, unique = true)
	private String username;

	@Column(name = "PASSWORD", nullable = false, length = 128)
	private String password;

	public CustomerEntity() {
		this.bankAccounts = new HashSet<>();
	}

	// GETTERS

	@Override
	public long getId() {
		return id;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getFirstName() {
		return fName;
	}

	@Override
	public String getLastName() {
		return lName;
	}

	public List<BankAccountEntity> getBankAccounts() {
		return new ArrayList<>(bankAccounts);
	}

	public String getSSN() {
		return socialSecurityNumber;
	}

	public String getAddress() {
		return address;
	}

	public Date getBirthDate() {
		return dateOfBirth;
	}

	public String getPhone() {
		return phone;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public String getPassword() {
		return password;
	}

	// SETTERS
	public void setFirstName(String firstName) {
		fName = firstName;
		if (lName != null)
			name = fName + " " + lName;
	}

	public void setLastName(String lastName) {
		lName = lastName;
		if (fName != null)
			name = fName + " " + lName;
	}

	public void setSocialSecNum(String sSN) {
		socialSecurityNumber = sSN;
	}

	public void setAddress(String adr) {
		address = adr;
	}

	public void setBirthDate(Date date) {
		dateOfBirth = date;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void addBankAccount(BankAccountEntity account) {
		this.bankAccounts.add(account);
	}

	public boolean removeBankAccount(BankAccountEntity account) {
		return bankAccounts.remove(account);
	}

	@Override
	public String toString() {
		return name + " " + Long.toString(id);
	}
	
	/*==================================================
	 * Function: hasAllInfo()
	 * Checks if all required fields contain values.
	 * param: none
	 * return: bool that determines if all requirements
	 * are met
	 =================================================*/
	public boolean hasAllInfo() {
		if (fName != null && lName != null && name != null && username != null && password != null && address != null
				&& phone != null && socialSecurityNumber != null && dateOfBirth != null) {
			return true; // Consider revamping this
		}
		return false;
	}
}
