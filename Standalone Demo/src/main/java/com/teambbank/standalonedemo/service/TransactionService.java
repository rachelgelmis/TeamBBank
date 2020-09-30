package com.teambbank.standalonedemo.service;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.teambbank.standalonedemo.entity.BankAccountEntity;
import com.teambbank.standalonedemo.entity.TransactionEntity;
import com.teambbank.standalonedemo.exception.InvalidTransactionAmountException;
import com.teambbank.standalonedemo.exception.InvalidTransactionTypeException;
import com.teambbank.standalonedemo.model.BankAccountType;
import com.teambbank.standalonedemo.model.TransactionType;
import com.teambbank.standalonedemo.repository.BankAccountRepository;
import com.teambbank.standalonedemo.repository.TransactionRepository;
import com.teambbank.standalonedemo.view.ViewController;

@Service
public class TransactionService {

	@Autowired
	private TransactionRepository transactionRepo;
	@Autowired
	private BankAccountRepository bankRepo;
	@Autowired
	private BankAccountService accountService;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private transient ViewController viewController;

	/*
	 * ============================================ Function: TransactionService()
	 * Constructs the transactionservice params: none
	 * ============================================
	 */
	protected TransactionService() {
	}

	/*
	 * =============================================================================
	 * =========== Function: getTransaction() looks up and returns a transaction
	 * entity in the transaction repository by its id params: long id long id: the
	 * id of the transaction we're searching for returns: a transaction entity that
	 * we searched for
	 * =============================================================================
	 * ===========
	 */
	public TransactionEntity findById(long id) {
		Optional<TransactionEntity> transaction = transactionRepo.findById(id);
		if (transaction.isPresent())
			return transaction.get();
		return null;
	}

	/*
	 * ===========================================================================
	 * Function: createTransaction() creates a new TransactionEntity, and sets
	 * values for it params: BankAccountEntity account, long amount, TransactionType
	 * type BankAccountEntity account: The account associated with the transaction
	 * long amount: The amount deposited, withdrawn, transferred, or paid
	 * TransactionType type: deposit, withdrawal, transfer, payment return:
	 * TransactionEntity that we just created.
	 * ==========================================================================
	 */
	@Transactional
	public TransactionEntity createTransaction(BankAccountEntity account, long amount, TransactionType type) {
		if (accountService.findById(account.getId()) == null) {
			throw new EntityNotFoundException(account + " bank account not found in database.");
		}
		// TODO add the validation/input checking of these fields
		TransactionEntity transaction = new TransactionEntity();
		transaction.setBankAccount(account);
		transaction.setTimestamp(Instant.now());
		transaction.setAmount(amount);
		transaction.setTransactionType(type);
		// Check if the transaction type is applicable to the bankAccountType
		if (!account.getAccountType().isTransactionTypeValid(type)) {
			throw new IllegalStateException("Check that the transaction type is applicable to the bank account type.");
		}
		// Check if the amount is valid with the given transaction type
		if (type != TransactionType.TRANSFER && !isValidTransaction(account, amount, type)) {
			throw new InvalidTransactionAmountException(
					"Check that the amount given is applicable with the account's balance.");
		}
		// Check that everything has been populated in the entity
		if (transaction.hasAllInfo()) {
			// Perform the transaction in the way specific to the account type
			switch (account.getAccountType()) {
			case CHECKING:
				// Transact funds from bank account
				account.setBalance(account.getBalance() + amount);
				break;
			case HOME_MORTGAGE:
				if (type != TransactionType.DEPOSIT) {
					throw new IllegalStateException("Can only make deposits/payments on mortgage accounts.");
				}
				// Subtracting as we want the home mortgage balance to go down.
				account.setBalance(account.getBalance() - amount);
				break;
			case CREDIT_CARD:
				// Subtracting as we want the credit card balance to go down.
				account.setBalance(account.getBalance() - amount);
				break;
			case MONEY_MARKET:
				// Transact funds from bank account
				account.setBalance(account.getBalance() + amount);
				break;
			case SAVINGS:
				// Transact funds from bank account
				account.setBalance(account.getBalance() + amount);
				break;
			default:
				break;
			}
			/// Save the transaction to the repository
			transaction = transactionRepo.save(transaction);
			bankRepo.save(account);
			return transaction;
		} else {
			throw new IllegalArgumentException("Not all fields for " + transaction + " were initialized.");
		}
	}

	/*=======================================================================
	 * Function: isValidTransaction
	 * Determines if the pending transaction is valid and can occur
	 * params: BankAccountEntity account, long amount, TransactionType type
	 * account: The bank account entity the transaction is to occur in.
	 * amount: The amount of money the pending transaction is dealing with
	 * type: What type of transaction (Deposit, withdrawal, or transfer) 
	 * is pending.
	 * returns: A boolean that passes if the transaction can occur.
	 ======================================================================*/
	public boolean isValidTransaction(BankAccountEntity account, long amount, TransactionType type) {
		// Is the account type valid for the given transaction type
		if (!account.getAccountType().isTransactionTypeValid(type)) {
			return false;
		}
		// Is the amount given valid for the given transaction type (negative values and
		// zeroes etc.)
		switch (type) {
		case DEPOSIT:
			if (amount < 0) {
				throw new InvalidTransactionAmountException("Cannot deposit a negative amount.");
			}
			if (amount > account.getBalance() && (account.getAccountType() == BankAccountType.HOME_MORTGAGE
					|| account.getAccountType() == BankAccountType.CREDIT_CARD)) {
				throw new InvalidTransactionAmountException(
						"Cannot deposit and amount greater than the account balance.");
			}
			return true;
		case TRANSFER:
			throw new IllegalArgumentException("Use isValidTransfer() instead!");
		case WITHDRAWAL:
			long leftoverBalance = amount + account.getBalance();

			if (leftoverBalance < account.getMinimumBalance()) {
				throw new InvalidTransactionAmountException(
						"Cannot withdraw an amount greater than the minimum mortgage balance.");
			}
			if (amount > 0) {
				throw new InvalidTransactionAmountException("Withdraw amount must be negative.");
			}
			if (amount * -1.0f > account.getBalance()) {

				throw new InvalidTransactionAmountException("Invalid balance for withdrawing.");
			}
			return true;
		default:
			throw new IllegalStateException(
					"Handling for the transaction type: " + type + " has not been implemented.");
		}
	}

	/*=======================================================================
	 * Function: isValidTransfer
	 * Determines if the pending transfer is valid and can occur
	 * params: BankAccountEntity accountfrom, BankAccountEntity accountTo,
	 * long amount
	 * accountFrom: The bank account entity the transfer is occurring from
	 * accountTo: The bank account entity the transfer is occurring to
	 * amount: The amount of money the pending transfer is dealing with
	 * returns: A boolean that passes if the transfer can occur.
	 ======================================================================*/
	public boolean isValidTransfer(BankAccountEntity accountFrom, BankAccountEntity accountTo, long amount) {
		// Is the account type valid for the given transaction type
		if (!accountTo.getAccountType().isTransactionTypeValid(TransactionType.TRANSFER)) {
			throw new InvalidTransactionTypeException(
					"Cannot transfer to account type: " + accountFrom.getAccountType());
		}
		if (!accountFrom.getAccountType().isTransactionTypeValid(TransactionType.TRANSFER)) {
			throw new InvalidTransactionTypeException(
					"Cannot transfer from account type: " + accountFrom.getAccountType());
		}
		if (amount > accountFrom.getBalance()) {
			throw new InvalidTransactionAmountException(
					"Cannot transfer an amount from the account greater than its balance.");
		}
		if (amount <= 0) {
			throw new InvalidTransactionAmountException(
					"Cannot transfer a non-positive amount.");
		}
		return true;
	}
	
	/* ============================================ 
	 * function: saveChanges() flush
	 * the repository params: none return: none
	 * ============================================
	 */
	@Transactional
	public void saveChanges() {
		transactionRepo.flush();
	}

	/*=====================================================
	 * function: sortByAmountAscending() 
	 =====================================================*/
	public List<TransactionEntity> sortByAmountAscending(BankAccountEntity account){
		List<TransactionEntity> transactionList = account.getTransactions();

		int size = transactionList.size();
		return sortAmountAsc(transactionList, 0, size-1);
	}
	
	private int partitionAmountAsc(List<TransactionEntity> list, int low, int high) 
	{ 
		long pivot = list.get(high).getAmount();  
		int i = (low-1);
		for (int j=low; j<high; j++) 
		{ 
			if (list.get(j).getAmount() > pivot) 
			{ 
				i++; 
				Collections.swap(list, i, j);
			} 
     } 
     Collections.swap(list, i+1, high);
     return i+1; 
	} 

	private List<TransactionEntity> sortAmountAsc( List<TransactionEntity> list, int low, int high) 
	{ 
		if (low < high) 
		{ 
			int pi = partitionAmountAsc(list, low, high);
			sortAmountAsc(list, low, pi-1); 
			sortAmountAsc(list, pi+1, high); 
		} 
		return list;
	}
	

	/*=====================================================
	 * function: sortByAmountDescending() 
	 =====================================================*/
	public List<TransactionEntity> sortByAmountDescending(BankAccountEntity account){
		return reverse(sortByAmountAscending(account));
	}
	
	/*
	 * ===========================================================================
	 * Reverse list
	 * ===========================================================================
	 */
	private List<TransactionEntity> reverse(List<TransactionEntity> transactions){
		List<TransactionEntity> list = transactions;
		Collections.reverse(list);
		return list;
	}
}