package com.teambbank.standalonedemo.view;

import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.annotation.PostConstruct;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.teambbank.standalonedemo.entity.BankAccountEntity;
import com.teambbank.standalonedemo.entity.CustomerEntity;
import com.teambbank.standalonedemo.exception.InvalidTransactionAmountException;
import com.teambbank.standalonedemo.model.TransactionType;
import com.teambbank.standalonedemo.service.BankAccountService;
import com.teambbank.standalonedemo.service.TransactionService;

@Component
public class DepositMenu extends JPanel {

	/**
	 * Auto-generated UID
	 */
	private static final long serialVersionUID = 5149772636037054629L;
	@Autowired
	private transient ViewController viewController;
	private transient CustomerEntity customer;
	private transient CustomerChangeListener customerListener;
	private transient BankAccountEntity bankAccount;
	private transient BankAccountChangeListener bankAccountListener;
	@Autowired
	private transient TransactionService transactionService;
	@Autowired
	private transient BankAccountService bankService;

	private JLabel lblBankAccount;

	@PostConstruct
	private void initializeController() {
		viewController.addPropertyChangeListener(customerListener);
		viewController.addPropertyChangeListener(bankAccountListener);
	}

	/**
	 * Create the application.
	 */
	public DepositMenu() {
		initialize();
		this.bankAccountListener = new BankAccountChangeListener();
		this.customerListener = new CustomerChangeListener();
	}

	/**
	 * Initialize the contents of the this.
	 */
	private void initialize() {
		this.setBounds(100, 100, 1280, 1023);
		this.setLayout(null);

		JLabel lblNewLabel = new JLabel("Deposit");
		lblNewLabel.setFont(new Font("Georgia", Font.PLAIN, 38));
		lblNewLabel.setBounds(604, 39, 250, 65);
		this.add(lblNewLabel);

		JLabel lblUsername = new JLabel("Deposit Amount:");
		lblUsername.setFont(new Font("Georgia", Font.PLAIN, 38));
		lblUsername.setBounds(136, 228, 388, 65);
		this.add(lblUsername);

		this.lblBankAccount = new JLabel();
		lblBankAccount.setFont(new Font("Georgia", Font.PLAIN, 38));
		lblBankAccount.setBounds(501, 147, 353, 70);
		this.add(lblBankAccount);

		JLabel lblDollarSign = new JLabel("$");
		lblDollarSign.setFont(new Font("Georgia", Font.PLAIN, 38));
		lblDollarSign.setBounds(94, 311, 75, 65);
		this.add(lblDollarSign);

		JTextField dollarAmountTextBox = new JTextField();
		dollarAmountTextBox.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
				if (((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE)) {
					e.consume(); // ignore event
				}
			}
		});
		dollarAmountTextBox.setHorizontalAlignment(SwingConstants.RIGHT);
		dollarAmountTextBox.setFont(new Font("Georgia", Font.PLAIN, 27));
		dollarAmountTextBox.setBounds(136, 304, 488, 72);
		this.add(dollarAmountTextBox);
		dollarAmountTextBox.setColumns(10);

		// TODO add constraints so that the spinner can't go negative
		JSpinner centsSpinner = new JSpinner();
		centsSpinner.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
				if (((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE)) {
					e.consume(); // ignore event
				}
			}
		});
		centsSpinner.setFont(new Font("Georgia", Font.PLAIN, 27));
		centsSpinner.setModel(new SpinnerNumberModel(0, 0, 99, 1));
		centsSpinner.setBounds(639, 304, 82, 72);
		this.add(centsSpinner);

		JLabel lblDecimal = new JLabel(".");
		lblDecimal.setFont(new Font("Georgia", Font.PLAIN, 38));
		lblDecimal.setBounds(626, 323, 75, 65);
		this.add(lblDecimal);

		// This button creates a checkings or savings account for a customer and stores
		// it in the database.
		JButton depositButton = new JButton("Deposit");
		depositButton.setFont(new Font("Georgia", Font.PLAIN, 25));
		depositButton.addActionListener(e -> {

			
			if(dollarAmountTextBox.getText().equals(""))
			{
			    JOptionPane.showMessageDialog(new JFrame(), "Dollar amount field must not be empty.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
			    return;
			}
			if(!(dollarAmountTextBox.getText().matches("[0-9]+"))) {
			    JOptionPane.showMessageDialog(new JFrame(), "Dollar amount field must only contains digits.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
			    return;
			}
			// Get the values needed to make a deposit
			int dollarValue = Integer.parseInt(dollarAmountTextBox.getText());
			// Check for a valid number
			try {
				dollarValue = Integer.parseInt(dollarAmountTextBox.getText());
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(new JFrame(), "Dollar amount field has an invalid amount.", "Invalid Input",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			int centsValue = Integer.parseInt(centsSpinner.getValue().toString());
			long total = (long) dollarValue * 100 + (long) centsValue;
			
			try {
				transactionService.isValidTransaction(bankAccount, total, TransactionType.DEPOSIT);
			} catch (InvalidTransactionAmountException ex) {
				JOptionPane.showMessageDialog(new JFrame(), "Cannot deposit that amount into the account.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			transactionService.createTransaction(bankAccount, total, TransactionType.DEPOSIT);
			bankService.saveChanges();
			transactionService.saveChanges();
			updateGUI();
			viewController.showTellerCustomerInfoMenu(true);

		});
		depositButton.setBounds(757, 304, 250, 72);
		this.add(depositButton);

		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(e -> {
			// do nothing but close the deposit menu and open the account page
			viewController.showTellerCustomerInfoMenu(true);
		});
		cancelButton.setFont(new Font("Georgia", Font.PLAIN, 25));
		cancelButton.setBounds(1004, 901, 250, 72);
		this.add(cancelButton);
	}

	private void updateGUI() {
		lblBankAccount.setText(bankAccount.getName() + " - " + bankAccount.getAccountType() + " - "
				+ BankAccountService.getMoneyFormat(bankAccount.getBalance()));
		lblBankAccount.setSize(lblBankAccount.getPreferredSize());
	}

	private class CustomerChangeListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			customer = viewController.getMainCustomer();
			if (customer != null && bankAccount != null) {
				updateGUI();
			}
		}

	}

	private class BankAccountChangeListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			bankAccount = viewController.getMainBankAccount();
			if (customer != null && bankAccount != null) {
				updateGUI();
			}
		}
	}
}
