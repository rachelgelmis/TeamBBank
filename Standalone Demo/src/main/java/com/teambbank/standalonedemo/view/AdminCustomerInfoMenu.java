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
import com.teambbank.standalonedemo.service.BankAccountService;
import com.teambbank.standalonedemo.service.CustomerService;
import com.teambbank.standalonedemo.view.sortedlist.BankAccountSorterService;

@Component
public class AdminCustomerInfoMenu extends JPanel implements ActionListener, UpdateableGUI {

	/**
	 * Auto-generated UID
	 */
	private static final long serialVersionUID = -4361134487247433187L;
	@Autowired
	private transient ViewController viewController;
	private transient CustomerEntity customer;
	@Autowired
	private transient CustomerService custService;
	@Autowired
	private transient BankAccountService bankService;
	private transient BankAccountEntity bankAccount;
	private transient CustomerChangeListener customerListener;

	// GUI components
	private JLabel nameLabel;
	private JLabel usernameLabel;
	private BankAccountScrollPane bankAccountScrollPane;

	/**
	 * Create the application.
	 */
	public AdminCustomerInfoMenu() {
		// Initialize GUI components
		this.customerListener = new CustomerChangeListener();
	}

	/**
	 * Initialize the contents of the this.
	 */
	@PostConstruct
	private void initialize() {
		viewController.addPropertyChangeListener(customerListener);

		this.setBounds(100, 100, 1280, 1023);
		setLayout(null);

		JLabel lblCustomerAccounts = new JLabel("Customer Accounts:");
		lblCustomerAccounts.setFont(new Font("Georgia", Font.PLAIN, 38));
		lblCustomerAccounts.setBounds(10, 147, 336, 44);
		this.add(lblCustomerAccounts);

		JButton deleteAccountButton = new JButton("Delete User Account");
		deleteAccountButton.addActionListener(e -> {
			if (!customer.getBankAccounts().isEmpty()) {
				JOptionPane.showMessageDialog(new JFrame(), "Customer must not have remaining bank accounts.", "ERROR",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			int reply = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete the ENTIRE user account?",
					"WARNING", JOptionPane.YES_NO_OPTION);
			if (reply == JOptionPane.YES_OPTION) {
				bankAccount = bankAccountScrollPane.getSelectedValue();
				if (customer != null)
					custService.deleteCustomer(customer);
				JOptionPane.showMessageDialog(null, "Account successfully deleted");
				viewController.showAdminMainMenu(true);
			} else {
				// do nothing
			}

		});
		deleteAccountButton.setFont(new Font("Georgia", Font.PLAIN, 25));
		deleteAccountButton.setBounds(962, 913, 294, 53);
		this.add(deleteAccountButton);

		JButton deleteBankAccountButton = new JButton("Delete Bank Account");
		deleteBankAccountButton.addActionListener(e -> {
			// Check if user even selected a bank account. return if they didnt.
			if (bankAccountScrollPane.getSelectedValue() == null) {
				JOptionPane.showMessageDialog(new JFrame(), "You must select a bank account.", "Invalid Selection",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			// Ask if they want to delete the account.
			int reply = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this bank account?",
					"WARNING", JOptionPane.YES_NO_OPTION);
			if (reply == JOptionPane.YES_OPTION) {
				if (bankAccount != null) {
					if(!isBankAccountEmpty(bankAccount)) return;
					
//					custService.removeBankAccount(customer, bankAccount);
					bankService.deleteBankAccount(bankAccount);
					custService.saveChanges();
					bankService.saveChanges();
					viewController.refreshCustomer();
					bankAccount = null;
					updateGUI();
					JOptionPane.showMessageDialog(null, "Bank account successfully deleted");
				}
			} else {
				// do nothing
			}

		});
		deleteBankAccountButton.setFont(new Font("Georgia", Font.PLAIN, 25));
		deleteBankAccountButton.setBounds(525, 913, 294, 53);
		deleteAccountButton.setBounds(962, 913, 294, 53);
		this.add(deleteBankAccountButton);

		JLabel lblCustomerInformation = new JLabel("Customer Information");
		lblCustomerInformation.setFont(new Font("Georgia", Font.BOLD, 28));
		lblCustomerInformation.setBounds(777, 11, 444, 44);
		this.add(lblCustomerInformation);

		// Go back to the account selection screen to pick a different user
		JButton returnToMainMenuButton = new JButton("<-- Return to main menu");
		returnToMainMenuButton.addActionListener(e -> {
			viewController.showAdminMainMenu(true);
		});
		returnToMainMenuButton.setFont(new Font("Georgia", Font.PLAIN, 25));
		returnToMainMenuButton.setBounds(10, 11, 370, 53);
		this.add(returnToMainMenuButton);

		JButton createBankAccount = new JButton("Create Bank Account");
		createBankAccount.setFont(new Font("Georgia", Font.PLAIN, 25));
		createBankAccount.addActionListener(e -> {
			// Create a new window to show the checking account.
			viewController.showBankAccountCreationMenu(true);
		});
		createBankAccount.setBounds(10, 913, 357, 53);
		this.add(createBankAccount);

		usernameLabel = new JLabel("Username:");
		usernameLabel.setFont(new Font("Georgia", Font.PLAIN, 25));
		usernameLabel.setBounds(777, 120, 444, 44);
		this.add(usernameLabel);

		nameLabel = new JLabel("Name: ");
		nameLabel.setFont(new Font("Georgia", Font.PLAIN, 25));
		nameLabel.setBounds(777, 66, 444, 44);
		this.add(nameLabel);

		this.bankAccountScrollPane = new BankAccountScrollPane(this, bankService);
		this.bankAccountScrollPane.setBounds(10, 202, 1260, 640);
		this.add(bankAccountScrollPane);
		this.bankAccountScrollPane.addListSelectionListener(e -> {
			if (this.bankAccountScrollPane.getSelectedValue() != null) {
				bankAccount = bankAccountScrollPane.getSelectedValue();
			}
		});
	}

	public void updateGUI() {
		if (customer != null) {
			customer = viewController.getMainCustomer();
			nameLabel.setText("Name: " + customer.getName());
			usernameLabel.setText("Username: " + customer.getUsername());
			// Save the selected account before repainting
			BankAccountEntity selectedEntity = bankAccountScrollPane.getSelectedValue();
			bankAccountScrollPane.setListData(BankAccountSorterService.getSortedList(customer,
					bankAccountScrollPane.getActiveSortingCriteria(), bankAccountScrollPane.getActiveSortingStatus(), bankService));
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
	
	/*=============================================================
	 * Function: isBankAccountEmpty()
	 * Determines if the bank account is empty. Called when an
	 * attempt to delete a bank account has occurred.
	 * params: BankAccountEntity bankAccount
	 * bankAccount: the current bankAccount about to be deleted
	 * returns: A boolean that is true if the account has a
	 * balance of 0, false if it is not 0.
	 ============================================================*/
	private boolean isBankAccountEmpty(BankAccountEntity bankAccount)
	{
		long balance = bankAccount.getBalance();
		if(balance != 0) {
			JOptionPane.showMessageDialog(new JFrame(), "The selected bank account's balance MUST be 0 before deleting.", "Cannot Delete",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
		else {			
			return true;
		}
		
			
	}
}
