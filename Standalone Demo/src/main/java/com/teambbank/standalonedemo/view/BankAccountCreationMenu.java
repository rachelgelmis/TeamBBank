package com.teambbank.standalonedemo.view;

import java.awt.Font;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.teambbank.standalonedemo.entity.BankAccountEntity;
import com.teambbank.standalonedemo.entity.CustomerEntity;
import com.teambbank.standalonedemo.model.BankAccountType;
import com.teambbank.standalonedemo.service.BankAccountService;

@Component
public class BankAccountCreationMenu extends JPanel {

	/**
	 * Auto-generated UID
	 */
	private static final long serialVersionUID = 3571598314371168518L;
	@Autowired
	private transient ViewController viewController;
	private transient JTextField interestRateTextBox;
	private transient JTextField bankAccountNameTextBox;
	@Autowired
	private transient BankAccountService bankService;
	private JTextField balanceTextBox;
	private JTextField minBalanceTextBox;

	/**
	 * Create the application.
	 */
	public BankAccountCreationMenu() {
		bankService = new BankAccountService();
		initialize();
	}

	/**
	 * Initialize the contents of the this.
	 */
	private void initialize() {
		this.setLayout(null);

		JLabel lblNewLabel = new JLabel("Bank account name:");
		lblNewLabel.setFont(new Font("Georgia", Font.PLAIN, 25));
		lblNewLabel.setBounds(185, 150, 460, 37);
		this.add(lblNewLabel);

		JLabel lblInterestRate = new JLabel("Interest rate (If applicable):");
		lblInterestRate.setFont(new Font("Georgia", Font.PLAIN, 25));
		lblInterestRate.setBounds(185, 375, 472, 57);
		this.add(lblInterestRate);

		JLabel lblAccountType = new JLabel("Account type:");
		lblAccountType.setBounds(185, 455, 472, 57);
		lblAccountType.setFont(new Font("Georgia", Font.PLAIN, 25));
		this.add(lblAccountType);

		bankAccountNameTextBox = new JTextField();
		bankAccountNameTextBox.setFont(new Font("Georgia", Font.PLAIN, 21));
		bankAccountNameTextBox.setColumns(10);
		bankAccountNameTextBox.setBounds(655, 150, 231, 37);
		this.add(bankAccountNameTextBox);

		interestRateTextBox = new JTextField("0");
		interestRateTextBox.setFont(new Font("Georgia", Font.PLAIN, 21));
		interestRateTextBox.setBounds(655, 381, 168, 37);
		this.add(interestRateTextBox);
		interestRateTextBox.setColumns(10);

		JComboBox<BankAccountType> accountTypeComboBox = new JComboBox<>();
		accountTypeComboBox.setFont(new Font("Georgia", Font.PLAIN, 20));
		accountTypeComboBox.setModel(new DefaultComboBoxModel<BankAccountType>(BankAccountType.values()));
		accountTypeComboBox.setBounds(655, 470, 231, 34);
		this.add(accountTypeComboBox);

		JButton createAccountButton = new JButton("Create Account");
		createAccountButton.addActionListener(e -> {
			String name = bankAccountNameTextBox.getText();	
			CustomerEntity cust = viewController.getMainCustomer();
			
			// Strip the name of trailing/leading whitespace
			name = name.trim();

			//If any of the following fields are empty, then we just want to return and do nothing else.
			if(name.isEmpty() || name == null) {
				JOptionPane.showMessageDialog(new JFrame(), "Account Name field is empty.", "ERROR", JOptionPane.ERROR_MESSAGE);
				return;
			}
			// Check that the name doesn't exist among the customer bank accounts already
			if (bankService.customerHasAccountOfName(cust, name)) {
				JOptionPane.showMessageDialog(new JFrame(), "Account with identical name already exists.", "ERROR", JOptionPane.ERROR_MESSAGE);
				return;
			}
			//If the bank account name is empty, then we just want to return and do nothing else.
			if(balanceTextBox.getText().isEmpty() || balanceTextBox.getText() == null) {
				JOptionPane.showMessageDialog(new JFrame(), "Begining balance field is empty. If you do not want a beginning balance, please enter \"0\" in the text box above.", "ERROR", JOptionPane.ERROR_MESSAGE);
				return;
			}
			//If the bank account name is empty, then we just want to return and do nothing else.
			if(minBalanceTextBox.getText().isEmpty() || minBalanceTextBox.getText() == null) {
				JOptionPane.showMessageDialog(new JFrame(), "Minimum Balance field is empty. If you do not want a minimum balance, please enter \"0\" in the text box above.", "ERROR", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			String interestRate = interestRateTextBox.getText();
			int balance = 0;
			long minBalance = 0;
			try {
				balance = 100 * Integer.parseInt(balanceTextBox.getText());
			}	catch(NumberFormatException ex) {
				JOptionPane.showMessageDialog(new JFrame(), "Beginning Balance must be an integer value in the format of \"1000\"", "Invalid Input", JOptionPane.ERROR_MESSAGE);
				return;
			}
			try {
				minBalance = 100 * Long.parseLong(minBalanceTextBox.getText());
			}	catch(NumberFormatException ex) {
				JOptionPane.showMessageDialog(new JFrame(), "Minimum Balance must be an integer value in the format of \"1000\"", "Invalid Input", JOptionPane.ERROR_MESSAGE);
				return;
			}
			BankAccountType type = (BankAccountType)accountTypeComboBox.getSelectedItem();

			
			
			if(balance < minBalance ) {
				JOptionPane.showMessageDialog(new JFrame(), "The beginning balance cannot be less than the minimum balance.", "ERROR", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			BankAccountEntity temp = null;
			
			// If the account is given an interest rate
			if (interestRate != null && !interestRate.isEmpty()) {
				try {
					float interestRateFloat = Float.parseFloat(interestRate);
					temp = bankService.createAccount(name, cust, type, balance, interestRateFloat, minBalance);
				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(new JFrame(), "Invalid interest rate input.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
					return;
				}
			} else {
				temp = bankService.createAccount(name, cust, type, balance, 0, minBalance);
			}

			viewController.showAdminCustomerInfoMenu(true);

			viewController.setMainBankAccount(temp);

		});
		createAccountButton.setFont(new Font("Georgia", Font.PLAIN, 24));
		createAccountButton.setBounds(377, 660, 386, 47);
		this.add(createAccountButton);

		// Go back to the account selection screen to pick a different user
		JButton accountSelectionMenuButton = new JButton("<-- Account Selection");
		accountSelectionMenuButton.addActionListener(e -> {
			viewController.showAdminCustomerInfoMenu(true);
		});
		accountSelectionMenuButton.setFont(new Font("Georgia", Font.PLAIN, 25));
		accountSelectionMenuButton.setBounds(20, 11, 315, 72);
		this.add(accountSelectionMenuButton);

		this.setBounds(100, 100, 1280, 1023);
		
		JLabel lblBeginningBalanceif = new JLabel("Beginning Balance (If applicable):");
		lblBeginningBalanceif.setFont(new Font("Georgia", Font.PLAIN, 25));
		lblBeginningBalanceif.setBounds(185, 294, 460, 57);
		add(lblBeginningBalanceif);
		
		balanceTextBox = new JTextField("0");
		balanceTextBox.setFont(new Font("Georgia", Font.PLAIN, 21));
		balanceTextBox.setColumns(10);
		balanceTextBox.setBounds(655, 300, 168, 37);
		add(balanceTextBox);
		
		JLabel lblMinimumBalance = new JLabel("Minimum Balance (If applicable):");
		lblMinimumBalance.setFont(new Font("Georgia", Font.PLAIN, 25));
		lblMinimumBalance.setBounds(185, 218, 460, 57);
		add(lblMinimumBalance);
		
		minBalanceTextBox = new JTextField("0");
		minBalanceTextBox.setFont(new Font("Georgia", Font.PLAIN, 21));
		minBalanceTextBox.setColumns(10);
		minBalanceTextBox.setBounds(655, 224, 168, 37);
		add(minBalanceTextBox);
	}
}
