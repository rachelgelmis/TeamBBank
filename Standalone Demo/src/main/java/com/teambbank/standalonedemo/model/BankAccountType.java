package com.teambbank.standalonedemo.model;

/**
 * Enumeration for the types of bank accounts available to a customer
 * 
 * @author Alex Barr
 *
 */
public enum BankAccountType {
	CHECKING("Checking"), SAVINGS("Savings"), MONEY_MARKET("Money Market"), HOME_MORTGAGE("Home Mortgage"),
	CREDIT_CARD("Credit Card");

	private final String name;
	public static final int MINIMUM_HOME_MORTGAGE_BALANCE = 250000;

	private BankAccountType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	/*=======================================================================================
	 * Function: isTransactionTypeValid()
	 * Determines if the passed in transaction type can be deposited, withdrawn from, or
	 * transferred with.
	 * params: TransactionType type
	 * type is the deposit, transfer, or withdrawal that is pending to occur.
	 * return: A bool that determines if the transaction can occur.
	 =======================================================================================*/
	public boolean isTransactionTypeValid(TransactionType type) {
		switch (this) {
		case CHECKING:
			switch(type) {
			case DEPOSIT:
				return true;
			case TRANSFER:
				return true;
			case WITHDRAWAL:
				return true;
			default:
				throw new IllegalStateException("Handling for transaction type: + " + type + " is not implemented.");
			}
		case CREDIT_CARD:
			switch(type) {
			case DEPOSIT:
				return true;
			case TRANSFER:
				return false;
			case WITHDRAWAL:
				return true;
			default:
				throw new IllegalStateException("Handling for transaction type: + " + type + " is not implemented.");
			}
		case HOME_MORTGAGE:
			switch(type) {
			case DEPOSIT:
				return true;
			case TRANSFER:
				return false;
			case WITHDRAWAL:
				return false;
			default:
				throw new IllegalStateException("Handling for transaction type: + " + type + " is not implemented.");
			}
		case MONEY_MARKET:
			switch(type) {
			case DEPOSIT:
				return true;
			case TRANSFER:
				return false;
			case WITHDRAWAL:
				return true;
			default:
				throw new IllegalStateException("Handling for transaction type: + " + type + " is not implemented.");
			}
		case SAVINGS:
			switch(type) {
			case DEPOSIT:
				return true;
			case TRANSFER:
				return true;
			case WITHDRAWAL:
				return true;
			default:
				throw new IllegalStateException("Handling for transaction type: + " + type + " is not implemented.");
			}
		default:
			throw new IllegalStateException("Handling for account type: + " + this + " is not implemented.");
		}
	}
	
	@Override
	public String toString() {
		return name;
	}
}
