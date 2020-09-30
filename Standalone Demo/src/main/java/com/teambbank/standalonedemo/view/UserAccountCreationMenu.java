package com.teambbank.standalonedemo.view;

import java.awt.Font;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.teambbank.standalonedemo.model.EmployeeType;
import com.teambbank.standalonedemo.service.CustomerService;
import com.teambbank.standalonedemo.service.EmployeeService;

@Component
public class UserAccountCreationMenu extends JPanel {

	/**
	 * Auto-generated UID
	 */
	private static final long serialVersionUID = 3906623889762162605L;
	@Autowired
	private transient ViewController viewController;
	@Autowired
	private transient CustomerService customerService;
	@Autowired
	private transient EmployeeService employeeService;
	private JTextField firstNameTextBox;
	private JTextField lastNameTextBox;
	private JTextField usernameTextBox;
	private JPasswordField passwordTextBox;
	private JPasswordField confirmPasswordTextBox;
	private JTextField addressTextBox;
	private JTextField phoneNumberTextBox;
	private JPasswordField ssnTextBox1;
	private JPasswordField ssnTextBox2;
	private JPasswordField ssnTextBox3;
	private JComboBox<String> accountTypeComboBox;
	private JComboBox<String> yearComboBox;
	private JComboBox<String> monthComboBox;
	private JComboBox<String> dayComboBox;
	private List<JComponent> componentsToHide;

	/**
	 * Create the application.
	 */
	public UserAccountCreationMenu() {
		this.componentsToHide = new ArrayList<>();
		initialize();
	}

	/**
	 * Initialize the contents of the this.
	 */
	private void initialize() {
		this.setLayout(null);

		// Return to main menu button
		JButton returnToMainMenuButton = new JButton("<-- Return to main menu");
		returnToMainMenuButton.addActionListener(e -> {
			viewController.showAdminMainMenu(true);
		});
		returnToMainMenuButton.setFont(new Font("Georgia", Font.PLAIN, 24));
		returnToMainMenuButton.setBounds(10, 11, 386, 47);
		this.add(returnToMainMenuButton);

		// Menu title
		JLabel lblCreateAccount = new JLabel("Create Account");
		lblCreateAccount.setFont(new Font("Georgia", Font.PLAIN, 25));
		lblCreateAccount.setBounds(482, 56, 472, 57);
		this.add(lblCreateAccount);

		// First Name
		JLabel lblFirstName = new JLabel("First name:");
		lblFirstName.setFont(new Font("Georgia", Font.PLAIN, 22));
		lblFirstName.setBounds(186, 147, 424, 37);
		this.add(lblFirstName);

		this.firstNameTextBox = new JTextField();
		this.firstNameTextBox.setFont(new Font("Georgia", Font.PLAIN, 20));
		this.firstNameTextBox.setColumns(10);
		this.firstNameTextBox.setBounds(656, 150, 268, 37);
		this.add(firstNameTextBox);

		// Last Name
		JLabel lblLastName = new JLabel("Last name:");
		lblLastName.setFont(new Font("Georgia", Font.PLAIN, 22));
		lblLastName.setBounds(186, 209, 424, 37);
		this.add(lblLastName);

		this.lastNameTextBox = new JTextField();
		this.lastNameTextBox.setFont(new Font("Georgia", Font.PLAIN, 20));
		this.lastNameTextBox.setColumns(10);
		this.lastNameTextBox.setBounds(656, 207, 268, 37);
		this.add(lastNameTextBox);

		// Username
		JLabel lblUsername = new JLabel("Username:");
		lblUsername.setFont(new Font("Georgia", Font.PLAIN, 22));
		lblUsername.setBounds(186, 269, 424, 37);
		this.add(lblUsername);

		this.usernameTextBox = new JTextField();
		this.usernameTextBox.setFont(new Font("Georgia", Font.PLAIN, 20));
		this.usernameTextBox.setColumns(10);
		this.usernameTextBox.setBounds(656, 269, 268, 37);
		this.add(usernameTextBox);

		// Password
		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setFont(new Font("Georgia", Font.PLAIN, 22));
		lblPassword.setBounds(186, 328, 424, 37);
		this.add(lblPassword);

		this.passwordTextBox = new JPasswordField();
		this.passwordTextBox.setFont(new Font("Georgia", Font.PLAIN, 20));
		this.passwordTextBox.setColumns(10);
		this.passwordTextBox.setBounds(656, 328, 268, 37);
		this.add(passwordTextBox);

		// Confirm Password
		JLabel lblConfirmPassword = new JLabel("Confirm Password:");
		lblConfirmPassword.setFont(new Font("Georgia", Font.PLAIN, 22));
		lblConfirmPassword.setBounds(186, 390, 424, 37);
		this.add(lblConfirmPassword);
		
		this.confirmPasswordTextBox = new JPasswordField();
		this.confirmPasswordTextBox.setFont(new Font("Georgia", Font.PLAIN, 20));
		this.confirmPasswordTextBox.setColumns(10);
		this.confirmPasswordTextBox.setBounds(656, 390, 268, 37);
		this.add(confirmPasswordTextBox);
		
		// Address
		JLabel lblAddress = new JLabel("Address:");
		lblAddress.setFont(new Font("Georgia", Font.PLAIN, 22));
		lblAddress.setBounds(186, 447, 424, 37);
		this.add(lblAddress);
		this.componentsToHide.add((JComponent) lblAddress);

		this.addressTextBox = new JTextField();
		this.addressTextBox.setFont(new Font("Georgia", Font.PLAIN, 20));
		this.addressTextBox.setColumns(10);
		this.addressTextBox.setBounds(656, 447, 268, 37);
		this.add(addressTextBox);
		this.componentsToHide.add((JComponent) addressTextBox);

		// Date of Birth
		JLabel lblDateOfBirth = new JLabel("Date of Birth:");
		lblDateOfBirth.setFont(new Font("Georgia", Font.PLAIN, 22));
		lblDateOfBirth.setBounds(186, 505, 424, 37);
		this.add(lblDateOfBirth);
		this.componentsToHide.add((JComponent) lblDateOfBirth);

		JLabel lblMonthMM = new JLabel("MM");
		lblMonthMM.setFont(new Font("Georgia", Font.PLAIN, 18));
		lblMonthMM.setBounds(639, 506, 46, 37);
		this.add(lblMonthMM);
		this.componentsToHide.add((JComponent) lblMonthMM);

		this.monthComboBox = new JComboBox<>();
		this.monthComboBox.setFont(new Font("Georgia", Font.PLAIN, 18));
		// TODO there must be a better way than this
		this.monthComboBox.setModel(new DefaultComboBoxModel<String>(
				new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12" }));
		this.monthComboBox.setBounds(678, 506, 86, 37);
		this.add(monthComboBox);
		this.componentsToHide.add((JComponent) monthComboBox);

		JLabel lblDayDD = new JLabel("DD");
		lblDayDD.setFont(new Font("Georgia", Font.PLAIN, 18));
		lblDayDD.setBounds(776, 506, 46, 37);
		this.add(lblDayDD);
		this.componentsToHide.add((JComponent) lblDayDD);

		this.dayComboBox = new JComboBox<>();
		this.dayComboBox.setFont(new Font("Georgia", Font.PLAIN, 18));
		// TODO there must be a better way than this
		this.dayComboBox.setModel(new DefaultComboBoxModel<String>(
				new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16",
						"17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31" }));
		this.dayComboBox.setBounds(807, 506, 80, 37);
		this.add(dayComboBox);
		this.componentsToHide.add((JComponent) dayComboBox);

		JLabel lblYearYYYY = new JLabel("YYYY");
		lblYearYYYY.setFont(new Font("Georgia", Font.PLAIN, 18));
		lblYearYYYY.setBounds(894, 517, 46, 14);
		this.add(lblYearYYYY);
		this.componentsToHide.add((JComponent) lblYearYYYY);

		this.yearComboBox = new JComboBox<>();
		this.yearComboBox.setFont(new Font("Georgia", Font.PLAIN, 18));
		// TODO there must be a better way than this
		this.yearComboBox.setModel(new DefaultComboBoxModel<String>(new String[] { "2020", "2019", "2018", "2017",
				"2016", "2015", "2014", "2013", "2012", "2011", "2010", "2009", "2008", "2007", "2006", "2005", "2004",
				"2003", "2002", "2001", "2000", "1999", "1998", "1997", "1996", "1995", "1994", "1993", "1992", "1991",
				"1990", "1989", "1988", "1987", "1986", "1985", "1984", "1983", "1982", "1981", "1980", "1979", "1978",
				"1977", "1976", "1975", "1974", "1973", "1972", "1971", "1970", "1969", "1968", "1967", "1966", "1965",
				"1964", "1963", "1962", "1961", "1960", "1959", "1958", "1957", "1956", "1955", "1954", "1953", "1952",
				"1951", "1950", "1949", "1948", "1947", "1946", "1945", "1944", "1943", "1942", "1941", "1940", "1939",
				"1938", "1937", "1936", "1935", "1934", "1933", "1932", "1931", "1930", "1929", "1928", "1927", "1926",
				"1925", "1924", "1923", "1922", "1921", "1920", "1919", "1918", "1917", "1916", "1915", "1914", "1913",
				"1912", "1911", "1910", "1909", "1908", "1907", "1906", "1905", "1904", "1903", "1902", "1901",
				"1900" }));
		this.yearComboBox.setBounds(945, 506, 73, 37);
		this.add(yearComboBox);
		this.componentsToHide.add((JComponent) yearComboBox);

		// Phone number
		JLabel lblPhoneNumber = new JLabel("Phone Number:");
		lblPhoneNumber.setFont(new Font("Georgia", Font.PLAIN, 22));
		lblPhoneNumber.setBounds(186, 575, 424, 37);
		this.add(lblPhoneNumber);
		this.componentsToHide.add((JComponent) lblPhoneNumber);

		phoneNumberTextBox = new JTextField();
		phoneNumberTextBox.setFont(new Font("Georgia", Font.PLAIN, 20));
		phoneNumberTextBox.setColumns(10);
		phoneNumberTextBox.setBounds(656, 576, 268, 37);
		this.add(phoneNumberTextBox);
		this.componentsToHide.add((JComponent) phoneNumberTextBox);

		// SSN
		JLabel lblSocialSecurityNumber = new JLabel("Social Security Number:");
		lblSocialSecurityNumber.setFont(new Font("Georgia", Font.PLAIN, 22));
		lblSocialSecurityNumber.setBounds(186, 641, 424, 37);
		this.add(lblSocialSecurityNumber);
		this.componentsToHide.add((JComponent) lblSocialSecurityNumber);

		ssnTextBox1 = new JPasswordField();
		ssnTextBox1.setColumns(10);
		ssnTextBox1.setBounds(659, 641, 73, 48);
		this.add(ssnTextBox1);
		this.componentsToHide.add((JComponent) ssnTextBox1);

		ssnTextBox2 = new JPasswordField();
		ssnTextBox2.setColumns(10);
		ssnTextBox2.setBounds(742, 641, 52, 48);
		this.add(ssnTextBox2);
		this.componentsToHide.add((JComponent) ssnTextBox2);

		ssnTextBox3 = new JPasswordField();
		ssnTextBox3.setColumns(10);
		ssnTextBox3.setBounds(807, 641, 117, 48);
		this.add(ssnTextBox3);
		this.componentsToHide.add((JComponent) ssnTextBox3);

		// Account Type
		JLabel lblAccountType = new JLabel("AccountType:");
		lblAccountType.setBounds(186, 695, 432, 57);
		lblAccountType.setFont(new Font("Georgia", Font.PLAIN, 25));
		this.add(lblAccountType);

		this.accountTypeComboBox = new JComboBox<>();
		String[] accountOptions = new String[] { "Administrator", "Teller", "Customer" };
		DefaultComboBoxModel<String> accountComboBoxModel = new DefaultComboBoxModel<>();
		for (String option : accountOptions)
			accountComboBoxModel.addElement(option);
		this.accountTypeComboBox.setFont(new Font("Georgia", Font.PLAIN, 25));
		this.accountTypeComboBox.setModel(accountComboBoxModel);
		this.accountTypeComboBox.setBounds(656, 700, 231, 47);
		this.accountTypeComboBox.addActionListener(e -> {
			hideInfoIfNeeded();
		});
		this.add(accountTypeComboBox);

		// Create Account Button
		JButton createAccountButton = new JButton("Create Account");
		createAccountButton.addActionListener(e -> {
			try {
					//Attempt to create an account.
					createAccount();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		createAccountButton.setFont(new Font("Georgia", Font.PLAIN, 24));
		createAccountButton.setBounds(378, 790, 386, 47);
		this.add(createAccountButton);

		hideInfoIfNeeded();
		this.setBounds(100, 100, 1280, 1023);
	}
	
	/*===================================================================
	 * Function: createAccount()
	 * Attempts to create the user account of the type the user set
	 * and store it in the database.
	 * params: none
	 * returns: none
	 ==================================================================*/
	private void createAccount() throws Exception {
		String username = usernameTextBox.getText();
		String password = String.valueOf(passwordTextBox.getPassword());
		String confirmPassword = String.valueOf(confirmPasswordTextBox.getPassword());
		String accountType = (String) accountTypeComboBox.getSelectedItem();
		// Add account to database HERE:
		// Set CustomerEntity
		String fName = firstNameTextBox.getText();
		String lName = lastNameTextBox.getText();
		
		if (accountType.equals("Customer")) {
			String ssn = String.valueOf(ssnTextBox1.getPassword()) + String.valueOf(ssnTextBox2.getPassword())
			+ String.valueOf(ssnTextBox3.getPassword());
			String address = addressTextBox.getText();
			
			Calendar tempDate = Calendar.getInstance();
			tempDate.set(Calendar.YEAR, Integer.valueOf((String) yearComboBox.getSelectedItem()));
			tempDate.set(Calendar.MONTH, Integer.valueOf((String) monthComboBox.getSelectedItem()) - 1);
			tempDate.set(Calendar.DAY_OF_MONTH, Integer.valueOf((String) dayComboBox.getSelectedItem()));
			
			//These 3 vars are for testing purposes only.
			int year = Integer.valueOf((String) yearComboBox.getSelectedItem());
			int month = Integer.valueOf((String) monthComboBox.getSelectedItem());
			int day = Integer.valueOf((String) dayComboBox.getSelectedItem());
			
			String phone = phoneNumberTextBox.getText();
			
			//Before we create the customer, we need to do some input checking. Call the function to do all input checking, and we will return a bool "PASS"
			//If that boolean "PASS" = true, then we will go through with creating our customer.
			boolean PASS = errorCheckCustomer(username, password, confirmPassword, fName, lName, ssn, address, day, month, year, phone);
			
			if(PASS == true) {
				customerService.createCustomer(fName, lName, ssn, tempDate, address, username, password, phone);
				// Close this window and go back to the main menu
				viewController.showAdminMainMenu(true);
			}
		} else if (accountType.equals("Administrator")) {
			
			boolean PASS = errorCheckEmployee(username, password, fName, lName, confirmPassword);
			
			if(PASS == true) {
				employeeService.createEmployee(fName, lName, username, password, EmployeeType.ADMIN);
				// Close this window and go back to the main menu
				viewController.showAdminMainMenu(true);
			}
		} else if (accountType.equals("Teller")) {
			boolean PASS = errorCheckEmployee(username, password, fName, lName, confirmPassword);
			
			if(PASS == true) {
				employeeService.createEmployee(fName, lName, username, password, EmployeeType.TELLER);
				// Close this window and go back to the main menu
				viewController.showAdminMainMenu(true);
			}
		} else {
			throw new Exception("Somehow we to tried to create a non-existing account type!");
		}
		

	}
	
	private void hideInfoIfNeeded() {
		if (!((String) accountTypeComboBox.getSelectedItem()).equals("Customer"))
			for (JComponent c : componentsToHide)
				c.setVisible(false);
		else {
			for (JComponent c : componentsToHide)
				c.setVisible(true);
		}
	}
	
	/*===================================================================
	 * Function: errorCheckCustomer()
	 * Determines if the current customer has everything it needs
	 * to be created.
	 * params: String username, String password, 
	 * String confirmPassword, String fName, 
	 * String lName, String ssn, String address, int day, 
	 * int month, int year, String phone
	 * username: the username to be set in the database.
	 * password: the password to be set in the database.
	 * confirmPassword: the password that must match to password
	 * fName: The first name of the customer to be set in the database.
	 * lName: The last name of the customer to be set in the database.
	 * ssn: The customer's social security number to be set in the
	 * database.
	 * address: the customer's address to be set in the database.
	 * day, month, year: integers that will be checked if they are a
	 * real date. if they are, they will be combined to make a birthday
	 * that will be set in the database.
	 * phone: the customer's phone number to be set in the database.
	 * returns: A boolean that is set accordingly as to whether
	 * or not the passed in variables matched the set 
	 * requirements and conditions to pass.
	 ==================================================================*/
	private boolean errorCheckCustomer(String username, String password, String confirmPassword, String fName, String lName, String ssn, String address, int day, int month, int year, String phone) {
		boolean pass = true;
		
		//Check if any of the fields are empty
		if(username == null || username.isEmpty()) {
			pass = false;
			JOptionPane.showMessageDialog(new JFrame(), "Username field is empty.", "ERROR", JOptionPane.ERROR_MESSAGE);
			return pass;
		}
			
		if(password == null || password.isEmpty()) {
			pass = false;
			JOptionPane.showMessageDialog(new JFrame(), "Password field is empty.", "ERROR", JOptionPane.ERROR_MESSAGE);
			return pass;
		}
		
		if(confirmPassword == null || confirmPassword.isEmpty()) {
			pass = false;
			JOptionPane.showMessageDialog(new JFrame(), "Confirm Password field is empty.", "ERROR", JOptionPane.ERROR_MESSAGE);
			return pass;
		}
		
		if(fName == null || fName.isEmpty()) {
			pass = false;
			JOptionPane.showMessageDialog(new JFrame(), "First Name field is empty.", "ERROR", JOptionPane.ERROR_MESSAGE);
			return pass;
		}
		
		if(lName == null || lName.isEmpty()) {
			pass = false;
			JOptionPane.showMessageDialog(new JFrame(), "Last Name field is empty.", "ERROR", JOptionPane.ERROR_MESSAGE);
			return pass;
		}
		
		if(address == null || address.isEmpty()) {
			pass = false;
			JOptionPane.showMessageDialog(new JFrame(), "Address field is empty.", "ERROR", JOptionPane.ERROR_MESSAGE);
			return pass;
		}
		
		if(phone == null || phone.isEmpty()) {
			pass = false;
			JOptionPane.showMessageDialog(new JFrame(), "Phone Number field is empty.", "ERROR", JOptionPane.ERROR_MESSAGE);
			return pass;
		}
		
				
		//Check if username is already in either DB.
		if (customerService.findByUsername(username) != null || employeeService.findByUsername(username) != null) {
			pass = false;
			JOptionPane.showMessageDialog(new JFrame(), "Username already created. Please select a different username.", "ERROR", JOptionPane.ERROR_MESSAGE);
			return pass;
		}
		
		//Check if passwords are equal.
		if(!password.equals(confirmPassword)) {
			pass = false;		
		    JOptionPane.showMessageDialog(new JFrame(), "Passwords are not equal", "ERROR", JOptionPane.ERROR_MESSAGE);
		    return pass;
		}
		
		//Check if ssn is 9 digits and contains only digits.
		if (!(ssn.matches("[0-9]+") && ssn.length() == 9))
		{
			pass = false;
		    JOptionPane.showMessageDialog(new JFrame(), "SSN is either not 9 digits long OR contains a non-numeric value.", "ERROR", JOptionPane.ERROR_MESSAGE);
			return pass;
		}
		
		//Check if phone number is 10 digits and contains only digits.
		if (!(phone.matches("[0-9]+") && phone.length() == 10))
		{
			pass = false;
		    JOptionPane.showMessageDialog(new JFrame(), "Phone number is either not 10 digits long OR contains a non-numeric value. Please enter your phone number in the format of 5555551234", "ERROR", JOptionPane.ERROR_MESSAGE);
			return pass;
		}
		
		//Check if April, June, September, or November have 31 days. Fail if so.
		if(month == 4 || month == 6 || month == 9 || month == 11) {
			if(day == 31) {
				pass = false;
			    JOptionPane.showMessageDialog(new JFrame(), "Invalid date of birth", "ERROR", JOptionPane.ERROR_MESSAGE);
				return pass;
			}
		}
		
		//Check if February has 30 or 31 days. Fail if so. If it's 29, check if it's a leap year.
		if(month == 2) {
			if(day == 30 || day == 31) {
				pass = false;
			    JOptionPane.showMessageDialog(new JFrame(), "Invalid date of birth", "ERROR", JOptionPane.ERROR_MESSAGE);
			    return pass;
			}
			
			if(day == 29) {				
				if(!((year % 4 == 0 && year % 100 != 0) || (year % 4 == 0 && year % 100 == 0 && year % 400 == 0))) {
					pass = false;
				    JOptionPane.showMessageDialog(new JFrame(), "Invalid date of birth", "ERROR", JOptionPane.ERROR_MESSAGE);
				    return pass;
				}
			}			
		}		
		
		return pass;
			
	}
	
	/*===================================================================
	 * Function: errorCheckEmployee()
	 * Determines if the current employee has everything it needs
	 * to be created.
	 * params: String username, String password, 
	 * String confirmPassword, String fName, String lName
	 * username: the username to be set in the database.
	 * password: the password to be set in the database.
	 * confirmPassword: the password that must match to password
	 * fName: The first name of the employee to be set in the database.
	 * lName: The last name of the employee to be set in the database.
	 * returns: A boolean that is set accordingly as to whether
	 * or not the passed in variables matched the set 
	 * requirements and conditions to pass.
	 ==================================================================*/
	private boolean errorCheckEmployee(String username, String password, String fName, String lName,  String confirmPassword) {
		boolean pass = true;
	
		//Check if any of the fields are empty
		if(username == null || username.isEmpty()) {
			pass = false;
			JOptionPane.showMessageDialog(new JFrame(), "Username field is empty.", "ERROR", JOptionPane.ERROR_MESSAGE);
			return pass;
		}
			
		if(password == null || password.isEmpty()) {
			pass = false;
			JOptionPane.showMessageDialog(new JFrame(), "Password field is empty.", "ERROR", JOptionPane.ERROR_MESSAGE);
			return pass;
		}
		
		if(confirmPassword == null || confirmPassword.isEmpty()) {
			pass = false;
			JOptionPane.showMessageDialog(new JFrame(), "Confirm Password field is empty.", "ERROR", JOptionPane.ERROR_MESSAGE);
			return pass;
		}
		
		if(fName == null || fName.isEmpty()) {
			pass = false;
			JOptionPane.showMessageDialog(new JFrame(), "First Name field is empty.", "ERROR", JOptionPane.ERROR_MESSAGE);
			return pass;
		}
		
		if(lName == null || lName.isEmpty()) {
			pass = false;
			JOptionPane.showMessageDialog(new JFrame(), "Last Name field is empty.", "ERROR", JOptionPane.ERROR_MESSAGE);
			return pass;
		}
		
		//Check if username is already in either DB.
		if (customerService.findByUsername(username) != null || employeeService.findByUsername(username) != null) {
			pass = false;
			JOptionPane.showMessageDialog(new JFrame(), "Username already created. Please select a different username.", "ERROR", JOptionPane.ERROR_MESSAGE);
			return pass;
		}
		
		//Check if passwords are equal.
		if(!password.equals(confirmPassword)) {
			pass = false;		
		    JOptionPane.showMessageDialog(new JFrame(), "Passwords are not equal", "ERROR", JOptionPane.ERROR_MESSAGE);
		    return pass;
		}
		
		return pass;
	}
}
