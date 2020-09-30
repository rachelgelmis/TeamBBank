package com.teambbank.standalonedemo.model;

public enum TransactionType {
	WITHDRAWAL("Withdrawal"), DEPOSIT("Deposit"), TRANSFER("Transfer");

	private final String name;

	private TransactionType(String name) {
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