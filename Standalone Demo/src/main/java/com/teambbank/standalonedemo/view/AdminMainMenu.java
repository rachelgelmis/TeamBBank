package com.teambbank.standalonedemo.view;

import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.teambbank.standalonedemo.service.CustomerService;
import com.teambbank.standalonedemo.service.EmployeeService;

@Component
public class AdminMainMenu extends JPanel {

	/**
	 * Auto-generated UID
	 */
	private static final long serialVersionUID = 9201529998281345618L;
	@Autowired
	private transient ViewController viewController;
	@Autowired
	private transient CustomerService customerService;
	@Autowired
	private transient EmployeeService employeeService;

	// GUI components
	private JTextField usernameTextBox;
	
	/**
	 * Create the application.
	 */
	public AdminMainMenu() {
		initialize();
	}

	/**
	 * Initialize the contents of the this.
	 */
	private void initialize() {
		this.setBounds(100, 100, 1280, 1023);
		this.setLayout(null);

		// This button creates a checkings or savings account for a customer and stores
		// it in the database.
		JButton createCustomerButton = new JButton("Create Customer / Teller / Admin");
		createCustomerButton.setFont(new Font("Georgia", Font.PLAIN, 25));
		createCustomerButton.addActionListener(e -> {
			// Create a new window to show the checking account.
			viewController.showUserAccountCreationMenu(true);
		});
		createCustomerButton.setBounds(713, 836, 464, 72);
		this.add(createCustomerButton);

		JButton accountLookupButton = new JButton("Account Lookup");
		accountLookupButton.addActionListener(e -> {
			// Search through database and see if this customer ID matches anything in the
			// database
			String username = usernameTextBox.getText();

			// Look through database. If we found a match, then open up the new dialog and
			// make sure we know the username.
			if (customerService.findByUsername(username) != null) {
				viewController.setMainCustomer(customerService.findByUsername(username));
				viewController.showAdminCustomerInfoMenu(true);

			} else if (employeeService.findByUsername(username) != null) {
				viewController.setMainEmployee(employeeService.findByUsername(username));
				viewController.showEmployeeInfoMenu(true);
			}
			// If a username couldn't be found then pull up an error message to alert the
			// user
			else {
				JOptionPane.showMessageDialog(null, "Username not found", "ERROR", JOptionPane.ERROR_MESSAGE);
			}
		});
		accountLookupButton.setFont(new Font("Georgia", Font.PLAIN, 25));
		accountLookupButton.setBounds(464, 418, 290, 72);
		this.add(accountLookupButton);

		usernameTextBox = new JTextField();
		usernameTextBox.setFont(new Font("Georgia", Font.PLAIN, 30));
		usernameTextBox.setBounds(354, 327, 518, 64);
		this.add(usernameTextBox);
		usernameTextBox.setColumns(10);

		JLabel lblNewLabel = new JLabel("Username:");
		lblNewLabel.setFont(new Font("Georgia", Font.PLAIN, 25));
		lblNewLabel.setBounds(354, 275, 290, 41);
		this.add(lblNewLabel);
		
		JButton logoutButton = new JButton("Logout");
		logoutButton.addActionListener(e -> {
			viewController.showLoginMenu(true);
		});
		logoutButton.setFont(new Font("Georgia", Font.PLAIN, 25));
		logoutButton.setBounds(887, 11, 290, 72);
		add(logoutButton);
	}
}
