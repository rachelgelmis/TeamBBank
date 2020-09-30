package com.teambbank.standalonedemo.service;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.teambbank.standalonedemo.entity.BankAccountEntity;
import com.teambbank.standalonedemo.entity.CustomerEntity;
import com.teambbank.standalonedemo.entity.TransactionEntity;
import com.teambbank.standalonedemo.model.BankAccountType;
import com.teambbank.standalonedemo.repository.BankAccountRepository;
import com.teambbank.standalonedemo.view.ViewController;

@Service
public class BankAccountService {
	
	@Autowired
	private BankAccountRepository accountRepo;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private transient ViewController viewController;
	
	/*==================================================
	 * Function: BankAccountService()
	 * Constructs the service
	 * params: none
	 * return: none
	 =================================================*/
	public BankAccountService() {
	}
	
	/*=======================================================================================
	 * Function: findByName
	 * Looks up a BankAccountEntity in the Bank Account Repository by name, and returns it
	 * params: name
	 * name is the name we are searching with.
	 * return:  the found BankAccountEntity
	 =======================================================================================*/
	public BankAccountEntity findByName(String name) {
		Optional<BankAccountEntity> account = accountRepo.findByName(name);
		if (account.isPresent())
			return account.get();
		return null;
	}
	
	
	/*=======================================================================================
	 * Function: findById
	 * Looks up a BankAccountEntity in the Bank Account Repository by id, and returns it
	 * params: long id
	 * long id: the ID we are searching with
	 * return: the found BankAccountEntity
	 =======================================================================================*/
	public BankAccountEntity findById(long id) {
		Optional<BankAccountEntity> account = accountRepo.findById(id);
		if (account.isPresent())
			return account.get();
		return null;
	}
	
	/*=======================================================================================
	 * Function: findByAccountType
	 * Looks up BankAccountEntitys in the Bank Account Repository by account type and returns them
	 * params: BankAccountType type
	 * BankAccountType type: the type we are searching with
	 * return: a list of found BankAccountEntitys. Empty list if none found.
	 =======================================================================================*/
	public List<BankAccountEntity> findByAccountType(BankAccountType type) {
		Optional<List<BankAccountEntity>> accounts = accountRepo.findByAccountType(type);
		if (accounts.isPresent())
			return accounts.get();
		return Collections.emptyList();
	}

	
	/*=======================================================================================
	 * Function: createAccount()
	 * Creates a bank account, sets all information, and adds it to the repository
	 * params: name, cust, type, balance, interestRate, minBalance
	 * Name: name of the bank account
	 * cust: a CustomerEntity this account will belong to
	 * type: type of account (e.g. checking, savings, etc.)
	 * balance: the starting balance for the account
	 * interestRate: the interest rate the account will have.
	 * minBalance: the minimum balance the account must have at all times.
	 * return: the newly created account
	 =======================================================================================*/
	@Transactional
	public BankAccountEntity createAccount(String name, CustomerEntity cust, BankAccountType type, int balance,
			float interestRate, long minBalance) {
		if (customerService.findById(cust.getId()) == null) {
			throw new EntityNotFoundException(cust + " customer not found in database.");
		}
		cust = customerService.findById(cust.getId());
		if (customerHasAccountOfName(cust, name)) {
			throw new IllegalArgumentException("Bank account with same name already exists for customer.");
		}
		BankAccountEntity account = new BankAccountEntity();
		if (name == null || name.compareTo("") == 0)
			name = type.getName();

		account.setName(name);
		account.setCustomer(cust);
		account.setCreationDate(Calendar.getInstance().getTime());
		account.setAccountType(type);
		account.setBalance(Math.abs(balance));
		account.setInterestRate(0f);
		account.setMinimumBalance(minBalance);
		// Add balances or interestRates if applicable
		switch (type) {
		case HOME_MORTGAGE:
			// account.setBalance(Math.abs(balance));
			break;
		case CHECKING:
			break;
		case MONEY_MARKET:
		case SAVINGS:
			account.setInterestRate(Math.abs(interestRate));
			break;
		case CREDIT_CARD:
			account.setInterestRate(Math.abs(interestRate));
			break;
		default:
			break;
		}
		if (account.hasAllInfo()) {
			// Save the account to the repository
			account = accountRepo.save(account);
			return account;
		} else {
			throw new IllegalArgumentException("Not all fields for " + account + " were initialized.");
		}
	}

	public List<String> getColumnRep(BankAccountEntity account) {

		return Arrays.asList(account.getName() + " - " + account.getAccountType(), getMoneyFormat(account.getBalance()),
				String.valueOf(account.getInterestRate()));
	}

	public static String getMoneyFormat(long amount) {
		NumberFormat formatter = NumberFormat.getCurrencyInstance();
		return formatter.format((double) amount / 100.0);
	}

	@Transactional
	public void addTransaction(BankAccountEntity account, TransactionEntity transaction) {
		account.addTransaction(transaction);
	}
	
	/*==================================================
	 * Function: saveChanges()
	 * Makes changes in the repository permanent
	 * param: none
	 * return: none
	 =================================================*/
	@Transactional
	public void saveChanges() {
		accountRepo.flush();
	}
	
	/*=========================================================
	 * Function: deleteBankAccount()
	 * deletes a bank account entity from the bank account Repository
	 * params: BankAccountEntity account
	 * BankAccountEntity account:  The account to be deleted
	 * returns: none.
	 =========================================================*/
	@Transactional
	public void deleteBankAccount(BankAccountEntity account) {
		if (account != null) {
			accountRepo.delete(account);
		} else {
			throw new EntityNotFoundException(account + ": bank account not found in database.");
		}
	}
	
	/*===========================================================
	 * Function: deleteAll()
	 * removes all entities in a repository
	 * param: none
	 * return: none
	===========================================================*/
	@Transactional
	public void deleteAll() {
		accountRepo.deleteAll();
	}
	
	/*===============================================================	
	 * function: accrue()
	 * finds a list of bankAccounts and adds relevant interest if today is the same as the creation date
	 * params: none
	 * return: none
	===============================================================*/
	public void accrue() {
		Date today = new Date();

		List<BankAccountEntity> savingsList = this.findByAccountType(BankAccountType.SAVINGS);
		if (savingsList.isEmpty() == false) {
			savingsList.forEach((n) -> calculateInterest(n, today));
		}

		List<BankAccountEntity> homeMortgageList = this.findByAccountType(BankAccountType.SAVINGS);
		if (savingsList.isEmpty() == false) {
			homeMortgageList.forEach((n) -> calculateInterest(n, today));
		}

		List<BankAccountEntity> creditCardList = this.findByAccountType(BankAccountType.SAVINGS);
		if (savingsList.isEmpty() == false) {
			creditCardList.forEach((n) -> calculateInterest(n, today));
		}
	}
	
	
	/*==============================================================
	 * function: accrue()
	 * accrues interest for a bank account if it is the billing date.
	 * params: BankAccountEntity account, Date today
	 * BankAccountEntity account: the account we're checking to see if it's time to add interest
	 * Date today: today's date to check the start date against.
	 * Return: None
	 ===============================================================*/
	public void calculateInterest(BankAccountEntity account, Date today) {
		Calendar startDay = Calendar.getInstance();
		startDay.setTime(account.getCreationDate());
		Calendar day = Calendar.getInstance();
		day.setTime(today);
		if (startDay.get(Calendar.DAY_OF_MONTH) == day.get(Calendar.DAY_OF_MONTH)) {
			long bal = account.getBalance();
			BankAccountType accType = account.getAccountType();
			float rate = account.getInterestRate();
			bal = java.lang.Math.round(bal * (1 + rate));
			account.setBalance(bal);
			accountRepo.save(account);
		}
	}

	public boolean customerHasAccountOfName(CustomerEntity customer, String name) {
		for (BankAccountEntity b : customer.getBankAccounts()) {
			if (b.getName().equals(name)) {
				return true;
			}
		}
		return false;
	}

	/*=========================================================================
	 * Sorting Algorithms
	 * In our program, array lists are sorted by date of creation by default.
	==========================================================================*/
	
	
	/*=================================================================================
	 * Function: sortByNameAscending()
	 * Sorts a list of BankAccounts for a customer by there names in alphabetical order
	 * params: CustomerEntity customer
	 * CustomerEntity customer:  The customer whose account list will be sorted
	 * returns: The sorted list of accounts
	 ================================================================================*/
	public List<BankAccountEntity> sortByNameAscending(CustomerEntity customer) {
		List<BankAccountEntity> accountList = customer.getBankAccounts();

		int size = accountList.size();
		return sortNameAsc(accountList, 0, size - 1);
	}

	
	/*============================================
	 * Function: partitionNameAsc()
	 * The partitioning algorithm for Quick Sort
	 * params: List<BankAccountEntity> list, int low, int high
	 * List<BankAccountEntity> list: The list that's being sorted
	 I int low:
	 I int high:
	 * returns: The partition for quick sort
	 ============================================*/
	private int partitionNameAsc(List<BankAccountEntity> list, int low, int high) {
		String pivot = list.get(high).getName();
		int i = (low - 1); 
		for (int j = low; j < high; j++) {
			if (list.get(j).getName().toUpperCase().compareTo(pivot.toUpperCase()) < 0) {
				i++;
				Collections.swap(list, i, j);
			}
		}
		Collections.swap(list, i + 1, high);
		return i + 1;
	}

	
	/*=============================================================
	 * Function: sortNameAsc()
	 * Recursively sort elements before and after the partition
	 * params: List<BankAccountEntity> list, int low, int high
	 * List<BankAccountEntity> list:
	 * int low: an integer marking the lower portion of the list
	 * int high: an integer marking the higher portion of the list
	 * returns: Return the 
	 ============================================================*/
	private List<BankAccountEntity> sortNameAsc(List<BankAccountEntity> list, int low, int high) {
		if (low < high) {
			int part = partitionNameAsc(list, low, high);
			sortNameAsc(list, low, part - 1);
			sortNameAsc(list, part + 1, high);
		}
		return list;
	}

	
	/*=============================================================
	 * Function: sortNameDesc()
	 * Reverses the sorted array
	 * params: CustomerEntity customer
	 * CustomerEntity customer: The customer whose account list will be reversed
	 * returns: Return the reversed list
	 ============================================================*/
	public List<BankAccountEntity> sortByNameDescending(CustomerEntity customer) {
		List<BankAccountEntity> accountList = customer.getBankAccounts();

		int size = accountList.size();
		return this.reverse(sortByNameAscending(customer));
	}


	/*===========================================================================
	 * Function: sortByBalanceAscending()
	 * Sort by account balance in ascending order using quick sort
	 * params: CustomerEntity customer:
	 * CustomerEntity customer: the customer whose account list will be sorted
	 * return: The sorted list of bank accounts
	 ==========================================================================*/
	public List<BankAccountEntity> sortByBalanceAscending(CustomerEntity customer) {
		List<BankAccountEntity> accountList = customer.getBankAccounts();

		int size = accountList.size();
		return sortBalAsc(accountList, 0, size - 1);
	}

	
	/*=============================================================
	 * Function: partitionBalAsc()
	 * find the partition for quick sort and swap elements 
	 * params: List<BankAccountEntity> list, int low, int high
	 * List<BankAccountEntity> list: the list being sorted
	 * int low: an integer marking the lower portion of the list
	 * int high: an integer marking the higher portion of the list
	 * returns: Return the partition
	 ============================================================*/
	private int partitionBalAsc(List<BankAccountEntity> list, int low, int high) {
		long pivot = list.get(high).getBalance();
		int i = (low - 1);
		for (int j = low; j < high; j++) {
			if (list.get(j).getBalance() < pivot) {
				i++;
				Collections.swap(list, i, j);
			}
		}
		Collections.swap(list, i + 1, high);
		return i + 1;
	}

	
	/*=============================================================
	 * Function: sortBalAsc()
	 * Recursively sort elements before and after the partition
	 * params: List<BankAccountEntity> list, int low, int high
	 * List<BankAccountEntity> list:
	 * int low: an integer marking the lower portion of the list
	 * int high: an integer marking the higher portion of the list
	 * returns: Return the list as it's sorted
	 ============================================================*/
	private List<BankAccountEntity> sortBalAsc(List<BankAccountEntity> list, int low, int high) {
		if (low < high) {
			int part = partitionBalAsc(list, low, high);
			sortBalAsc(list, low, part - 1);
			sortBalAsc(list, part + 1, high);
		}
		return list;
	}
	

	/*=============================================================
	 * Function: sortNameDesc()
	 * Reverse the sorted list
	 * params: CustomerEntity customer
	 * CustomerEntity customer: The customer whose account list is being reversed
	 * returns: Return the reversed list
	 ============================================================*/
	public List<BankAccountEntity> sortByBalanceDescending(CustomerEntity customer) {
		return this.reverse(sortByBalanceAscending(customer));
	}

	 
	/*===========================================================================
	 * Function: sortByInterestAscending()
	 * Sort by account interest rate in ascending order using quick sort
	 * params: CustomerEntity customer
	 * CustomerEntity customer: the customer whose account list will be sorted
	 * return: The sorted list of bank accounts
	 ==========================================================================*/	
	public List<BankAccountEntity> sortByInterestRateAscending(CustomerEntity customer) {
		List<BankAccountEntity> accountList = customer.getBankAccounts();

		int size = accountList.size();
		return sortNameAsc(accountList, 0, size - 1);
	}

	
	/*=============================================================
	 * Function: partitionInterestAsc()
	 * find the partition for quick sort, and swap elements to sort
	 * params: List<BankAccountEntity> list, int low, int high
	 * List<BankAccountEntity> list: the list being sorted
	 * int low: an integer marking the lower portion of the list
	 * int high: an integer marking the higher portion of the list
	 * returns: Return the partition
	 ============================================================*/
	private int partitionInterestAsc(List<BankAccountEntity> list, int low, int high) {
		String pivot = list.get(high).getName();
		int i = (low - 1);
		for (int j = low; j < high; j++) {
			if (list.get(j).getInterestRate() < 0) {
				i++;
				Collections.swap(list, i, j);
			}
		}
		Collections.swap(list, i + 1, high);
		return i + 1;
	}

	
	/*=============================================================
	 * Function: sortInterestAsc()
	 * Recursively sort elements before and after the partition
	 * params: List<BankAccountEntity> list, int low, int high
	 * List<BankAccountEntity> list:
	 * int low: an integer marking the lower portion of the list
	 * int high: an integer marking the higher portion of the list
	 * returns: Return the list as it's sorted
	 ============================================================*/
	private List<BankAccountEntity> sortInterestAsc(List<BankAccountEntity> list, int low, int high) {
		if (low < high) {
			int part = partitionNameAsc(list, low, high);
			sortInterestAsc(list, low, part - 1);
			sortInterestAsc(list, part + 1, high);
		}
		return list;
	}
	
	
	 /* ================================================================ 
	 * function:
	 * sortByInterestRateDescending()
	 * Reverses a list that's sorted by ascending interest rate
	 * params: CustomerEntity account
	 * CustomertEntity customer: the customer whose list will be sorted
	 * return: the descending list of BankAccount interest rates.
	 * ================================================================*/

	public List<BankAccountEntity> sortByInterestRateDescending(CustomerEntity customer) {
		return reverse(sortByInterestRateAscending(customer));
	}
	
	
	/* ===========================================================================
	 * function: reverse()
	 * Reverse a list
	 * parmas: List<BankAccountEntity> transactions
	 * List<BankAccountEntity> transactions:  the list to be reversed
	 * return: the reversed list
	 * ========================================================================== */
	private List<BankAccountEntity> reverse(List<BankAccountEntity> accounts) {
		List<BankAccountEntity> list = accounts;
		Collections.reverse(list);
		return list;
	}

}
