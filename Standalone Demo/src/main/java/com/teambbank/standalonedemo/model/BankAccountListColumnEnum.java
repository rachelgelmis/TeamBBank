package com.teambbank.standalonedemo.model;

public enum BankAccountListColumnEnum {
	BANK_ACCOUNT_NAME("Bank Account Name"), BALANCE("Balance"), INTEREST_RATE("Interest Rate");

	private final String name;

	private BankAccountListColumnEnum(String name) {
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
