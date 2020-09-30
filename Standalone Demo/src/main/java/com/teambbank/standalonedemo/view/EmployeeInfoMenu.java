package com.teambbank.standalonedemo.view;

import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.annotation.PostConstruct;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.teambbank.standalonedemo.entity.EmployeeEntity;
import com.teambbank.standalonedemo.service.EmployeeService;

@Component
public class EmployeeInfoMenu extends JPanel implements UpdateableGUI {

	/**
	 * Auto-generated UID
	 */
	private static final long serialVersionUID = 4709482923642555105L;
	@Autowired
	private transient ViewController viewController;
	private transient EmployeeEntity employee;
	@Autowired
	private transient EmployeeService employeeService;
	private transient EmployeeChangeListener employeeListener;
	
	// GUI components
	private JLabel lblJobPosition;
	private JLabel lblName;
	private JLabel lblUsername;	

	@PostConstruct
	private void initializeController() {
		viewController.addPropertyChangeListener(employeeListener);
	}
	
	/**
	 * Create the application.
	 */
	public EmployeeInfoMenu() {
		this.employeeListener = new EmployeeChangeListener();
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
		JButton deleteAccountButton = new JButton("Delete Account");
		deleteAccountButton.setFont(new Font("Georgia", Font.PLAIN, 25));
		deleteAccountButton.addActionListener(e -> {
			int reply = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this account?", "WARNING", JOptionPane.YES_NO_OPTION);
			if (reply == JOptionPane.YES_OPTION) {
				if (employee != null)
				employeeService.deleteEmployee(employee);
			    JOptionPane.showMessageDialog(null, "Account successfully deleted");
			    viewController.showAdminMainMenu(true);
			} else {
				//do nothing
			}
			
			viewController.showAdminMainMenu(true);
		});
		deleteAccountButton.setBounds(459, 619, 290, 72);
		this.add(deleteAccountButton);

		JLabel lblAccountInformation = new JLabel("Account Information");
		lblAccountInformation.setFont(new Font("Georgia", Font.BOLD, 30));
		lblAccountInformation.setBounds(105, 152, 384, 41);
		this.add(lblAccountInformation);

		lblUsername = new JLabel("Username:");
		lblUsername.setFont(new Font("Georgia", Font.PLAIN, 25));
		lblUsername.setBounds(105, 328, 461, 41);
		this.add(lblUsername);

		lblName = new JLabel("Name:");
		lblName.setFont(new Font("Georgia", Font.PLAIN, 25));
		lblName.setBounds(105, 242, 461, 41);
		this.add(lblName);

		lblJobPosition = new JLabel("Job position:");
		lblJobPosition.setFont(new Font("Georgia", Font.PLAIN, 25));
		lblJobPosition.setBounds(105, 417, 461, 41);
		this.add(lblJobPosition);

		JButton returnToMainMenuButton = new JButton("<-- Return to main menu");
		returnToMainMenuButton.addActionListener(e -> {
			viewController.showAdminMainMenu(true);
		});
		returnToMainMenuButton.setFont(new Font("Georgia", Font.PLAIN, 25));
		returnToMainMenuButton.setBounds(10, 11, 409, 72);
		this.add(returnToMainMenuButton);
	}
	
	public void updateGUI() {
		// For all fields realted to customer, reset the info
		if (employee != null) {
			lblName.setText("Name: " + employee.getName());
			lblUsername.setText("Username: " + employee.getUsername());
			lblJobPosition.setText("Job position: " + employee.getEmployeeType());
		}
	}

	private class EmployeeChangeListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			employee = viewController.getMainEmployee();
			updateGUI();
		}

	}
}
