package com.teambbank.standalonedemo.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.teambbank.standalonedemo.entity.EmployeeEntity;
import com.teambbank.standalonedemo.model.EmployeeType;
import com.teambbank.standalonedemo.repository.EmployeeRepository;

@Service
public class EmployeeService {

	@Autowired
	private EmployeeRepository employeeRepo;
	@Autowired
	private CustomerService customerService;

	
	/*============================================
	 * Function: findByName()
	 * params: String name
	 * 	String name: the name of the employee we're searching for
	 * returns: the employee with name
	 ============================================*/
	public EmployeeEntity findByName(String name) {
		Optional<EmployeeEntity> employee = employeeRepo.findByName(name);
		if (employee.isPresent())
			return employee.get();
		return null;
	}

	
	/*============================================
	 * Function: findById()
	 * params: long id
	 * 	long id: the id of the employee we're searching for
	 * returns: the employee with the id
	 ============================================*/
	public EmployeeEntity findById(long id) {
		Optional<EmployeeEntity> employee = employeeRepo.findById(id);
		if (employee.isPresent())
			return employee.get();
		return null;
	}

	
	/*============================================
	 * Function: findByUsername()
	 * params: String username
	 * 	String username: the username of the employee we're searching for
	 * returns:  The employee with the username
	 ============================================*/
	public EmployeeEntity findByUsername(String username) {
		Optional<EmployeeEntity> employee = employeeRepo.findByUsername(username);
		if (employee.isPresent())
			return employee.get();
		return null;
	}

	
	/*============================================
	 * Function: findByQuery()
	 * params: String query
	 * 	String query: the name or username of the employee we're searching for
	 * returns: a list of customers that have the username or name
	 ============================================*/
	public List<EmployeeEntity> findByQuery(String query) {
		Optional<List<EmployeeEntity>> list = employeeRepo.findByNameOrUsername(query, query);
		if (list.isPresent())
			return list.get();
		return Collections.emptyList();
	}

	
	/*============================================
	 * Function: createEmployee()
	 * params: String firstName, String lastName, String username, String password, EmployeeType empType
	 * 	String firstName: the first name of the new employee
	 * 	String lastName: the last name of the new employee
	 * 	String username: the username associated with the employee
	 *	String passowrd: The new employees password
	 *	EmployeeType empType: admin or teller
	 * returns: The newly created employee
	 ============================================*/
	@Transactional
	public EmployeeEntity createEmployee(String firstName, String lastName, String username, String password,
			EmployeeType empType) {
		if (this.findByUsername(username) != null || customerService.findByUsername(username) != null) {
			throw new IllegalArgumentException("Username is already in use.");
		}
		// TODO add the validation/input checking of these fields
		EmployeeEntity employee = new EmployeeEntity();
		employee.setFirstName(firstName);
		employee.setLastName(lastName);
		employee.setUsername(username);
		employee.setPassword(password);
		employee.setEmployeeType(empType);
		if (employee.hasAllInfo()) {
			// Save the entity
			employee = employeeRepo.save(employee);
			return employee;
		} else {
			throw new IllegalArgumentException("Not all fields for " + employee + " were initialized.");
		}
	}

	
	
	/*============================================
	 * Function: saveChanges()
	 * save the changes to the employee repository
	 * params: none
	 * returns: none
	 ============================================*/
	@Transactional
	public void saveChanges() {
		employeeRepo.flush();
	}

	
	
	/*============================================
	 * Function: deleteAll()
	 * Delete all employees in the repository
	 * params: none
	 * returns: none
	 ============================================*/
	@Transactional
	public void deleteAll() {
		employeeRepo.deleteAll();
	}

	
	
	/*============================================
	 * Function: deleteEmployee()
	 * Delete a single employee
	 * params: EmployeeEntity employee
	 * 	EmployeeEntity employee: the employee we're deleting
	 * returns: none
	 ============================================*/
	@Transactional
	public void deleteEmployee(EmployeeEntity employee) {
		if (employee != null) {
			employeeRepo.delete(employee);
		} else {
			throw new EntityNotFoundException("Employee not found: " + employee);
		}
	}
}
