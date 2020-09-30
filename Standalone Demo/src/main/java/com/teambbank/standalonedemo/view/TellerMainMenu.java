package com.teambbank.standalonedemo.view;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.teambbank.standalonedemo.service.CustomerService;

@Component
public class TellerMainMenu extends JPanel {

	/**
	 * Auto-generated UID
	 */
	private static final long serialVersionUID = -1978396237260889940L;
	@Autowired
	private transient ViewController viewController;
	@Autowired
	private transient CustomerService customerService;
	private JTextField usernameTextBox;
	private JTextField nameTextField;

	/**
	 * Create the application.
	 */
	public TellerMainMenu() {
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
		JButton searchUsernameButton = new JButton("Search");
		searchUsernameButton.setFont(new Font("Georgia", Font.PLAIN, 25));
		searchUsernameButton.addActionListener(e -> {

			// Search through database and see if this customer ID matches anything in the
			// database
			String username = usernameTextBox.getText();

			// Look through database. If we found a match, then open up the new dialog and
			// make sure we know the username.
			if (customerService.findByUsername(username) != null) {
				viewController.setMainCustomer(customerService.findByUsername(username));
				viewController.showTellerCustomerInfoMenu(true);
			}
			// If a username couldn't be found then pull up an error message to alert the
			// user
			else {
				JOptionPane.showMessageDialog(null, "Username not found", "ERROR", JOptionPane.ERROR_MESSAGE);
			}
		});
		searchUsernameButton.setBounds(858, 304, 250, 72);
		this.add(searchUsernameButton);

		JButton searchFirstLastNameButton = new JButton("Search");
		searchFirstLastNameButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String name = nameTextField.getText();
				
				// Look through database. If we found a match, then open up the new dialog and
				// make sure we know the username.
				if (customerService.findByName(name) != null) {
					viewController.setMainCustomer(customerService.findByName(name));
					viewController.showTellerCustomerInfoMenu(true);
				}
				// If a username couldn't be found then pull up an error message to alert the
				// user
				else {
					JOptionPane.showMessageDialog(null, "Name not found", "ERROR", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		searchFirstLastNameButton.setFont(new Font("Georgia", Font.PLAIN, 25));
		searchFirstLastNameButton.setBounds(858, 635, 250, 72);
		this.add(searchFirstLastNameButton);
		
		this.usernameTextBox = new JTextField();
		this.usernameTextBox.setFont(new Font("Georgia", Font.PLAIN, 27));
		this.usernameTextBox.setBounds(136, 304, 712, 72);
		this.add(this.usernameTextBox);
		this.usernameTextBox.setColumns(10);

		JLabel lblNewLabel = new JLabel("Account Lookup");
		lblNewLabel.setFont(new Font("Georgia", Font.PLAIN, 38));
		lblNewLabel.setBounds(466, 39, 388, 65);
		this.add(lblNewLabel);

		JLabel lblUsername = new JLabel("Search by Username:");
		lblUsername.setFont(new Font("Georgia", Font.PLAIN, 38));
		lblUsername.setBounds(136, 228, 441, 65);
		this.add(lblUsername);

		JLabel lblSearchByLast = new JLabel("Search by full name: (e.g. John Doe)");
		lblSearchByLast.setFont(new Font("Georgia", Font.PLAIN, 38));
		lblSearchByLast.setBounds(136, 559, 686, 65);
		this.add(lblSearchByLast);

		this.nameTextField = new JTextField();
		this.nameTextField.setFont(new Font("Georgia", Font.PLAIN, 27));
		this.nameTextField.setColumns(10);
		this.nameTextField.setBounds(136, 635, 712, 72);
		this.add(this.nameTextField);



		JLabel lblOr = new JLabel("OR");
		lblOr.setFont(new Font("Georgia", Font.PLAIN, 38));
		lblOr.setBounds(136, 441, 441, 65);
		this.add(lblOr);
		
		JButton logoutButton = new JButton("Logout");
		logoutButton.addActionListener(e -> {
			viewController.showLoginMenu(true);
		});
		logoutButton.setFont(new Font("Georgia", Font.PLAIN, 25));
		logoutButton.setBounds(980, 11, 290, 72);
		add(logoutButton);
	}
}
