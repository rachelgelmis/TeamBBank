package com.teambbank.standalonedemo.service;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.teambbank.standalonedemo.entity.BankAccountEntity;
import com.teambbank.standalonedemo.entity.CustomerEntity;
import com.teambbank.standalonedemo.repository.CustomerRepository;

@Service
public class CustomerService {

	@Autowired
	private CustomerRepository customerRepo;
	@Autowired
	private EmployeeService employeeService;
	
	
	/*============================================
	 * Function: CustomerService()
	 * constructs a CustomerService
	 * params: none
	 * returns: none
	 ============================================*/
	public CustomerService() {
	}

	
	/*=============================================================
	 * Function: findByName()
	 * lookup a CustomerEntity by name
	 * params: String name
	 * String name: the name of the customer we're searching for
	 * returns: the CustomerEntity we're searching for
	 =============================================================*/
	public CustomerEntity findByName(String name) {
		Optional<CustomerEntity> cust = customerRepo.findByName(name);
		if (cust.isPresent())
			return cust.get();
		return null;
	}

	
	/*=============================================================
	 * Function: findByID()
	 * lookup a CustomerEntity by ID
	 * params: long id
	 * long id:  The id of the CustomerEntity we're searching for
	 * returns: the CustomerEntity we're searching for
	 =============================================================*/
	public CustomerEntity findById(long id) {
		Optional<CustomerEntity> cust = customerRepo.findById(id);
		if (cust.isPresent())
			return cust.get();
		return null;
	}
	
	
	/*===============================================================
	 * Function: findByUsername()
	 * lookup a customer by the username associated with the account
	 * params: String username
	 * String username:  the username we're searching by
	 * returns: the CustomerEntity with the username
	 ==============================================================*/
	public CustomerEntity findByUsername(String username) {
		Optional<CustomerEntity> cust = customerRepo.findByUsername(username);
		if (cust.isPresent())
			return cust.get();
		return null;
	}
	
	
	/* ===============================================================================
	 * Function: findByQuery()
	 * Looks up customers by name or username, and returns all customers with the name
	 * or username of the query input.
	 * params:  String query
	 * String query: the name or username being searched for
	 * returns: a list of CustomerEntity with name or username the same as the query
	 ================================================================================*/
	//TODO: examine whether this should be a Customer Entity or a list of entities.
	public Optional<List<CustomerEntity>> findByQuery(String query){
		try{
			return customerRepo.findByNameOrUsername(query, query);
			//return customerRepo.findBylNameOrFName(query, query);
		} catch (IllegalArgumentException ex) {
			return null;
		}
	}
	
	
	/*======================================================================================
	 * Function: createCustomer()
	 * create a new CustomerEntity, and add it to the CustomerRepository
	 * params: String firstName, String lastName, String socialSecNum, Calendar birthDate,
			String address, String username, String password, String phoneNumber
		String firstName: The first name of the new customer
		String lastName: the last name fo the new customer
		String socialSecNum:  The social security number of the new customer
		Calendar birthDate:  The birth date of the new customer
		String address: The current address of the new customer
		String username:  The username chosen by the new customer
		String password:  The password chosen by the new customer
		String phoneNumber:  The phone number of the new customer
	 * returns: The newly created CustomerEntity
	 ======================================================================================*/
	@Transactional
	public CustomerEntity createCustomer(String firstName, String lastName, String socialSecNum, Calendar birthDate,
			String address, String username, String password, String phoneNumber) {
		if (this.findByUsername(username) != null || employeeService.findByUsername(username) != null) {
			throw new IllegalArgumentException("Username is already in use.");
		}
		// TODO add the validation/input checking of these fields
		CustomerEntity cust = new CustomerEntity();
		cust.setFirstName(firstName);
		cust.setLastName(lastName);
		cust.setSocialSecNum(socialSecNum);
		cust.setBirthDate(birthDate.getTime());
		cust.setAddress(address);
		cust.setPhone(phoneNumber);
		cust.setUsername(username);
		cust.setPassword(password);
		if (cust.hasAllInfo()) {
			// Save the customer to the repository
			cust = customerRepo.save(cust);
			return cust;
		} else {
			throw new IllegalArgumentException("Not all fields for " + cust + " were initialized.");
		}
	}
	
	/*==================================================
	 * Function: saveCustomer()
	 * Saves the state of the Customer given to the database
	 * params: CustomerEntity customer
	 	CustomerEntity customer: the customer to save
	 * returns: The newly saved CustomerEntity
	 ==================================================*/
	@Transactional
	public void saveCustomer(CustomerEntity customer) {
		customerRepo.save(customer);
	}

	
	/*==================================================
	 * Function: saveChanges()
	 * flushes the repository, making changes permanent
	 * params: none
	 * returns: none
	 ==================================================*/
	@Transactional
	public void saveChanges() {
		customerRepo.flush();
	}

	
	/*============================================
	 * Function: delete all
	 * Deletes all Customers in a Repository
	 * params: none
	 * returns: none
	 ============================================*/
	@Transactional
	public void deleteAll() {
		customerRepo.deleteAll();
	}
	
	/*=========================================================
	 * Function: deleteCustomer()
	 * deletes a customer entity from the customer Repository
	 * params: CustomerEntity customer
	 * CustomerEntity customer:  The customer to be deleted
	 * returns: none.
	 =========================================================*/
	@Transactional
	public void deleteCustomer(CustomerEntity customer) {
		if(customer != null) {
			customerRepo.delete(customer);
		}
		else {
			throw new EntityNotFoundException(customer + ": customer not found in database.");
		}
	}
	
	
	/*===================================================================
	 * Function: addBankAccount()
	 * adds a BankAccountEntity to an arraylist of a customer
	 * params: CustomerEntity cust, BankAccountEntity account
	 * CustomerEntity cust: The customer associated with the account
	 * BankAccountEntity account:  The account to add
	 * returns: none
	 ===================================================================*/
	@Transactional
	public void addBankAccount(CustomerEntity cust, BankAccountEntity account) {
		cust.addBankAccount(account);
	}
	
	/*===================================================================
	 * Function: addBankAccount()
	 * adds a BankAccountEntity to an arraylist of a customer
	 * params: CustomerEntity cust, BankAccountEntity account
	 * CustomerEntity cust: The customer associated with the account
	 * BankAccountEntity account:  The account to add
	 * returns: none
	 ===================================================================*/
	@Transactional
	public void removeBankAccount(CustomerEntity cust, BankAccountEntity account) {
		cust.removeBankAccount(account);
	}


}
