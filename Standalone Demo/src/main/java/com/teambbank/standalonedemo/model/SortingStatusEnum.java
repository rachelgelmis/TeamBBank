package com.teambbank.standalonedemo.model;

public enum SortingStatusEnum {
	ASCENDING("Ascending"), DESCENDING("Descending"), OFF("Off");

	private final String name;

	private SortingStatusEnum(String name) {
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
