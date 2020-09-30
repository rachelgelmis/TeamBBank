package com.teambbank.standalonedemo.model;

public enum EmployeeType {

	ADMIN("Administrator"), TELLER("Teller");

	private final String name;

	private EmployeeType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
