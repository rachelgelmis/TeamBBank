package com.teambbank.standalonedemo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.teambbank.standalonedemo.model.EmployeeType;
import com.teambbank.standalonedemo.model.IUserAccount;

@Entity(name = "EmployeeEntity")
@Table(name = "employees")
public class EmployeeEntity implements IUserAccount {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name = "NAME", nullable = false, length = 129)
	private String name;

	@Column(name = "FIRST_NAME", nullable = false, length = 64)
	private String fName;

	@Column(name = "LAST_NAME", nullable = false, length = 64)
	private String lName;

	@Column(name = "USERNAME", nullable = false, length = 64, unique = true)
	private String username;

	@Column(name = "PASSWORD", nullable = false, length = 64)
	private String password;

	@Column(name = "EMPLOYEE_TYPE", nullable = false)
	private EmployeeType employeeType;

	public EmployeeType getEmployeeType() {
		return employeeType;
	}

	public void setEmployeeType(EmployeeType employeeType) {
		this.employeeType = employeeType;
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

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	/*==================================================
	 * Function: hasAllInfo()
	 * Checks if all required fields contain values.
	 * param: none
	 * return: bool that determines if all requirements
	 * are met
	 =================================================*/
	public boolean hasAllInfo() {
		if (fName != null && lName != null && username != null && password != null && employeeType != null) {
			return true; // TODO Consider revamping this
		}
		return false;
	}
}