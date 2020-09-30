package com.teambbank.standalonedemo.view;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.annotation.PostConstruct;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.teambbank.standalonedemo.entity.BankAccountEntity;
import com.teambbank.standalonedemo.entity.CustomerEntity;
import com.teambbank.standalonedemo.model.BankAccountType;
import com.teambbank.standalonedemo.model.TransactionType;
import com.teambbank.standalonedemo.service.BankAccountService;
import com.teambbank.standalonedemo.view.sortedlist.BankAccountSorterService;

@Component
public class TellerCustomerInfoMenu extends JPanel implements ActionListener, UpdateableGUI {

	/**
	 * Auto-generated UID
	 */
	private static final long serialVersionUID = 2467919043320169714L;
	@Autowired
	private transient ViewController viewController;
	@Autowired
	private transient BankAccountService bankService;
	private transient CustomerEntity customer;
	private transient BankAccountEntity bankAccount;
	private transient CustomerChangeListener customerListener;

	// GUI components
	private JLabel usernameLabel;
	private JLabel nameLabel;
	private BankAccountScrollPane bankAccountScrollPane;

	/**
	 * Create the application.
	 */
	public TellerCustomerInfoMenu() {
		// Initialize GUI components
		this.usernameLabel = new JLabel();
		this.nameLabel = new JLabel();

		this.customerListener = new CustomerChangeListener();
	}

	/**
	 * Initialize the contents of the this.
	 */
	@PostConstruct
	private void initialize() {
		viewController.addPropertyChangeListener(customerListener);

		this.setBounds(100, 100, 1280, 1023);
		this.setLayout(null);

		JLabel lblCustomerAccounts = new JLabel("Customer Accounts:");
		lblCustomerAccounts.setFont(new Font("Georgia", Font.PLAIN, 38));
		lblCustomerAccounts.setBounds(10, 150, 388, 65);
		this.add(lblCustomerAccounts);

		// This button creates a checkings or savings account for a customer and stores
		// it in the database.
		JButton depositButton = new JButton("Deposit / Make a Payment");
		depositButton.addActionListener(e -> {
			bankAccount = bankAccountScrollPane.getSelectedValue();
			if (bankAccount == null) {
				JOptionPane.showMessageDialog(new JFrame(), "You must select a bank account.", "Invalid Selection",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			// Check that the teller can deposit from this account
			if (!bankAccount.getAccountType().isTransactionTypeValid(TransactionType.DEPOSIT)) {
				JOptionPane.showMessageDialog(new JFrame(), "Cannot deposit from that type of account.", "Invalid Selection",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			viewController.setMainBankAccount(bankAccount);
			viewController.showDepositMenu(true);

			bankAccountScrollPane.revalidate();
			bankAccountScrollPane.repaint();
			bankAccountScrollPane.setSelectedValue(null);
		});
		depositButton.setFont(new Font("Georgia", Font.PLAIN, 18));
		depositButton.setBounds(97, 883, 250, 72);
		
		this.add(depositButton);
		JButton withdrawalButton = new JButton("Withdrawal");
		withdrawalButton.addActionListener(e -> {
			bankAccount = bankAccountScrollPane.getSelectedValue();
			if (bankAccount == null) {
				JOptionPane.showMessageDialog(new JFrame(), "You must select a bank account.", "Invalid Selection",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			// Check that the teller can withdraw from this account
			if (!bankAccount.getAccountType().isTransactionTypeValid(TransactionType.WITHDRAWAL)) {
				JOptionPane.showMessageDialog(new JFrame(), "Cannot withdraw from that type of account.", "Invalid Selection",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			viewController.setMainBankAccount(bankAccount);
			viewController.showWithdrawalMenu(true);

			bankAccountScrollPane.revalidate();
			bankAccountScrollPane.repaint();
			bankAccountScrollPane.setSelectedValue(null);
		});
		withdrawalButton.setFont(new Font("Georgia", Font.PLAIN, 25));
		withdrawalButton.setBounds(509, 883, 250, 72);
		this.add(withdrawalButton);

		JButton transferButton = new JButton("Transfer");
		transferButton.addActionListener(e -> {
			viewController.showTransferMenu(true);

			bankAccountScrollPane.revalidate();
			bankAccountScrollPane.repaint();
			bankAccountScrollPane.setSelectedValue(null);
		});
		transferButton.setFont(new Font("Georgia", Font.PLAIN, 25));
		transferButton.setBounds(928, 883, 250, 72);
		this.add(transferButton);

		JLabel lblCustomerInformation = new JLabel("Customer Information");
		lblCustomerInformation.setFont(new Font("Georgia", Font.BOLD, 38));
		lblCustomerInformation.setBounds(689, 18, 489, 65);
		this.add(lblCustomerInformation);

		// Go back to the account selection screen to pick a different user
		JButton accountSelectionMenuButton = new JButton("<-- Account Selection");
		accountSelectionMenuButton.addActionListener(e -> {
			bankAccountScrollPane.setSelectedValue(null);
			viewController.showTellerMainMenu(true);
		});
		accountSelectionMenuButton.setFont(new Font("Georgia", Font.PLAIN, 25));
		accountSelectionMenuButton.setBounds(20, 11, 315, 72);
		this.add(accountSelectionMenuButton);

		usernameLabel.setFont(new Font("Georgia", Font.PLAIN, 38));
		usernameLabel.setBounds(689, 120, 388, 65);
		this.add(usernameLabel);

		nameLabel.setFont(new Font("Georgia", Font.PLAIN, 38));
		nameLabel.setBounds(689, 70, 388, 65);
		this.add(nameLabel);

		this.bankAccountScrollPane = new BankAccountScrollPane(this, bankService);
		this.bankAccountScrollPane.addListSelectionListener(e -> {
			if (this.bankAccountScrollPane.getSelectedValue() != null) {
//				viewController.setMainBankAccount(this.bankAccountScrollPane.getSelectedValue());
				bankAccount = bankAccountScrollPane.getSelectedValue();
			}
		});
		this.bankAccountScrollPane.setBounds(10, 202, 1260, 640);
		this.add(bankAccountScrollPane);
	}

	public void updateGUI() {
		if (customer != null) {
			customer = viewController.getMainCustomer();
			usernameLabel.setText("Username: " + this.customer.getUsername());
			nameLabel.setText("Name: " + this.customer.getName());
			// Save the selected account before repainting
			BankAccountEntity selectedEntity = bankAccountScrollPane.getSelectedValue();
			bankAccountScrollPane.setListData(
					BankAccountSorterService.getSortedList(customer, bankAccountScrollPane.getActiveSortingCriteria(),
							bankAccountScrollPane.getActiveSortingStatus(), bankService));
			bankAccountScrollPane.revalidate();
			bankAccountScrollPane.repaint();
			bankAccountScrollPane.setSelectedValue(selectedEntity);
		}
	}

	private class CustomerChangeListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			customer = viewController.getMainCustomer();
			updateGUI();
		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		updateGUI();
	}
}
