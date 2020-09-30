package com.teambbank.standalonedemo.view.sortedlist;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.teambbank.standalonedemo.entity.BankAccountEntity;
import com.teambbank.standalonedemo.entity.CustomerEntity;
import com.teambbank.standalonedemo.model.BankAccountListColumnEnum;
import com.teambbank.standalonedemo.model.SortingStatusEnum;
import com.teambbank.standalonedemo.service.BankAccountService;

public class BankAccountSorterService {

	private BankAccountSorterService() {
	}

	public static List<BankAccountEntity> getSortedList(CustomerEntity customer, BankAccountListColumnEnum criteria,
			SortingStatusEnum sortStatus, BankAccountService accountService) {
		if (customer != null) {
			if (sortStatus == SortingStatusEnum.OFF) {
				return customer.getBankAccounts();
			}
			switch (criteria) {
			case BALANCE:
				if (sortStatus == SortingStatusEnum.ASCENDING) {
					return accountService.sortByBalanceAscending(customer);
				} else {
					return accountService.sortByBalanceDescending(customer);
				}
			case BANK_ACCOUNT_NAME:
				if (sortStatus == SortingStatusEnum.ASCENDING) {
					return accountService.sortByNameAscending(customer);
				} else {
					return accountService.sortByNameDescending(customer);
				}
			case INTEREST_RATE:
				if (sortStatus == SortingStatusEnum.ASCENDING) {
					return accountService.sortByInterestRateAscending(customer);
				} else {
					return accountService.sortByInterestRateDescending(customer);
				}
			default:
				return customer.getBankAccounts();
			}
		}
		throw new IllegalStateException("How did we get here??");
	}

}
