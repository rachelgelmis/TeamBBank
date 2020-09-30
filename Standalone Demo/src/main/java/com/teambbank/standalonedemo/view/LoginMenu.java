package com.teambbank.standalonedemo.view;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.Border;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.teambbank.standalonedemo.entity.EmployeeEntity;
import com.teambbank.standalonedemo.model.EmployeeType;
import com.teambbank.standalonedemo.service.EmployeeService;

@Component
public class LoginMenu extends ImagePanel {

	/**
	 * Auto-generated UID
	 */
	private static final long serialVersionUID = 3938860765714695700L;
	@Autowired
	private transient ViewController viewController;
	@Autowired
	private transient EmployeeService employeeService;
	private transient EmployeeEntity employee;
	private JTextField usernameTextBox;
	private JPasswordField passwordTextBox;

	/**
	 * Create the application.
	 */
	public LoginMenu() {
		initialize();
	}

	/**
	 * Initialize the contents of the this.
	 */
	private void initialize() {
		this.setBounds(100, 100, 1280, 1023);
		this.setLayout(null);

		usernameTextBox = new JTextField() {
			/**
			 * Auto-generated UID
			 */
			private static final long serialVersionUID = -6188340821775517756L;

			@Override
			public void setBorder(Border border) {
				// This is intentionally doing nothing so we have no border.
			}
		};
		usernameTextBox.setFont(new Font("Georgia", Font.PLAIN, 36));
		usernameTextBox.setOpaque(false);
		usernameTextBox.setBounds(866, 374, 360, 75);
		this.add(usernameTextBox);
		usernameTextBox.setColumns(10);

		JButton loginButton = new JButton((String) null);
		loginButton.setOpaque(false);
		loginButton.setContentAreaFilled(false);
		loginButton.setBorderPainted(false);
		loginButton.addActionListener(e -> {
			String username = usernameTextBox.getText();
			String password = String.valueOf(passwordTextBox.getPassword());

			if (employeeService.findByUsername(username) != null) {
				viewController.setMainEmployee(employeeService.findByUsername(username));
				employee = viewController.getMainEmployee();
				if(password.equals(employee.getPassword())) {		
					passwordTextBox.setText("");
					usernameTextBox.setText("");

					if(employee.getEmployeeType() == EmployeeType.ADMIN) {					
						viewController.showAdminMainMenu(true);
					}
					else if(employee.getEmployeeType() == EmployeeType.TELLER) {				
						viewController.showTellerMainMenu(true);
					}
				}
				else {
				//Notify the user that they have entered an incorrect password.
				    JOptionPane.showMessageDialog(new JFrame(), "INCORRECT PASSWORD", "ERROR", JOptionPane.ERROR_MESSAGE);
				}
			} else {
			    JOptionPane.showMessageDialog(new JFrame(), "USERNAME NOT FOUND", "ERROR", JOptionPane.ERROR_MESSAGE);
			}
		});
		loginButton.setBounds(849, 612, 377, 81);
		this.add(loginButton);

		passwordTextBox = new JPasswordField() {
			/**
			 * Auto-generated UID
			 */
			private static final long serialVersionUID = -3932449485014198701L;

			@Override
			public void setBorder(Border border) {
				// This is intentionally doing nothing so we have no border.
			}
		};
		passwordTextBox.setFont(new Font("Georgia", Font.PLAIN, 36));
		passwordTextBox.setOpaque(false);
		passwordTextBox.setBackground(Color.WHITE);
		passwordTextBox.setBounds(866, 485, 360, 75);
		this.add(passwordTextBox);

		URL url = null;
		try {
			url = new ClassPathResource("images/signInPage.png").getURL();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		this.setBackgroundImage(url);
	}

}
