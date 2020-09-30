package com.teambbank.standalonedemo.view;

import java.awt.Dimension;
import java.awt.Insets;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.teambbank.standalonedemo.StandaloneDemoApplication;
import com.teambbank.standalonedemo.entity.BankAccountEntity;
import com.teambbank.standalonedemo.entity.CustomerEntity;
import com.teambbank.standalonedemo.entity.EmployeeEntity;
import com.teambbank.standalonedemo.service.CustomerService;

@Service
public class ViewController {

	// Spring-handled GUI components
	@Autowired
	private StandaloneDemoApplication mainWindow;
	@Autowired
	private LoginMenu loginMenu;
	@Autowired
	private UserAccountCreationMenu userAccountCreationMenu;
	@Autowired
	private AdminMainMenu adminMainMenu;
	@Autowired
	private TellerMainMenu tellerMainMenu;
	@Autowired
	private TellerCustomerInfoMenu tellerCustomerInfoMenu;
	@Autowired
	private DepositMenu depositMenu;
	@Autowired
	private TransferMenu transferMenu;
	@Autowired
	private WithdrawMenu withdrawalMenu;
	@Autowired
	private EmployeeInfoMenu employeeInfoMenu;
	@Autowired
	private AdminCustomerInfoMenu adminCustomerInfoMenu;
	@Autowired
	private BankAccountCreationMenu bankAccountCreationMenu;
	
	@Autowired
	private CustomerService customerService;

	// Main Customer being presented/edited
	private CustomerEntity mainCustomer;
	// Main Employee's attributes being analyzed
	private EmployeeEntity mainEmployee;
	// Main Bank Account
	private BankAccountEntity mainBankAccount;
	// Secondary bank account; used exclusively when transferring balances between
	// two accounts.
	private BankAccountEntity secondaryBankAccount;
	// Current panel being displayed
	private JComponent currentComponent;
	// List of panels to swap out of
	private List<JPanel> swappablePanels;
	// To enable listening for Customer changes
	private PropertyChangeSupport pcs;

	private static final Dimension DEFAULT_WINDOW_SIZE = new Dimension(1280, 1023);

	public ViewController() {
		this.swappablePanels = new ArrayList<>();
		this.pcs = new PropertyChangeSupport(this);
	}

	@PostConstruct
	private void initialize() {
		// Add the swappable panels here
		swappablePanels.addAll(Arrays.asList(loginMenu, userAccountCreationMenu, adminMainMenu, tellerMainMenu,
				tellerCustomerInfoMenu, depositMenu, transferMenu, withdrawalMenu, employeeInfoMenu,
				adminCustomerInfoMenu, bankAccountCreationMenu));
	}

	public CustomerEntity getMainCustomer() {
		this.refreshCustomer();
		return mainCustomer;
	}

	public void setMainCustomer(CustomerEntity customer) {
		CustomerEntity oldCustomer = this.mainCustomer;
		this.mainCustomer = customer;
		pcs.firePropertyChange("mainCustomer", oldCustomer, customer);
	}

	public EmployeeEntity getMainEmployee() {
		return mainEmployee;
	}

	public void setMainEmployee(EmployeeEntity employee) {
		EmployeeEntity oldEmployee = this.mainEmployee;
		this.mainEmployee = employee;
		pcs.firePropertyChange("mainEmployee", oldEmployee, employee);
	}

	public BankAccountEntity getMainBankAccount() {
		return mainBankAccount;
	}

	public void setMainBankAccount(BankAccountEntity mainBankAccount) {
		BankAccountEntity oldBankAccount = this.mainBankAccount;
		this.mainBankAccount = mainBankAccount;
		pcs.firePropertyChange("mainBankAccount", oldBankAccount, mainBankAccount);
	}

	public BankAccountEntity getSecondaryBankAccount() {
		return secondaryBankAccount;
	}

	public void setSecondaryBankAccount(BankAccountEntity secondaryBankAccount) {
		BankAccountEntity oldSecondaryBankAccount = this.secondaryBankAccount;
		this.secondaryBankAccount = secondaryBankAccount;
		pcs.firePropertyChange("secondaryBankAccount", oldSecondaryBankAccount, secondaryBankAccount);
	}

	public void showUserAccountCreationMenu(boolean toShow) {
		showComponent(userAccountCreationMenu, toShow);
	}

	public void showAdminMainMenu(boolean toShow) {
		showComponent(adminMainMenu, toShow);
	}

	public void showTellerMainMenu(boolean toShow) {
		showComponent(tellerMainMenu, toShow);
	}

	public void showLoginMenu(boolean toShow) {
		showComponent(loginMenu, toShow,
				new Dimension(loginMenu.getBackgroundImage().getWidth(), loginMenu.getBackgroundImage().getHeight()));
		// Set the mainWindow size to the image size of the login menu
//		if (toShow) {
//			mainWindow.setSize(loginMenu.getBackgroundImage().getWidth(), loginMenu.getBackgroundImage().getHeight());
//		}
	}

	public void showBankAccountCreationMenu(boolean toShow) {
		showComponent(bankAccountCreationMenu, toShow);
	}

	public void showTellerCustomerInfoMenu(boolean toShow) {
		showComponent(tellerCustomerInfoMenu, toShow);
	}

	public void showDepositMenu(boolean toShow) {
		showComponent(depositMenu, toShow);
	}

	public void showTransferMenu(boolean toShow) {
		showComponent(transferMenu, toShow, transferMenu.getPreferredSize());
	}

	public void showWithdrawalMenu(boolean toShow) {
		showComponent(withdrawalMenu, toShow);
	}

	public void showEmployeeInfoMenu(boolean toShow) {
		showComponent(employeeInfoMenu, toShow);
	}

	public void showAdminCustomerInfoMenu(boolean toShow) {
		showComponent(adminCustomerInfoMenu, toShow);
	}

	private void showComponent(JComponent component, boolean toShow) {
		showComponent(component, toShow, null);
	}

	/**
	 * Sets visible the specified component and updates the mainWindow
	 * 
	 * @param component - the component to set visibility
	 * @param toShow    - whether or not to show/hide
	 */
	private void showComponent(JComponent component, boolean toShow, Dimension size) {
		if (toShow) {
			for (JComponent c : swappablePanels) {
				// Displayable components at least have a parent container (like mainWindow for
				// example)
				if (c.isDisplayable()) {
					try {
						mainWindow.remove(c);
					} catch (NullPointerException e) {
						// Do nothing. This shouldn't happen but it's for precaution.
					}
				}
			}
			mainWindow.add(component);
			if (size != null) {
				mainWindow.setSize(size);
			}
		} else {
			mainWindow.remove(component);
		}

		component.setVisible(toShow);
		// If the component is an update-able GUI, update it
		if (component instanceof UpdateableGUI) {
			((UpdateableGUI) component).updateGUI();
		}
		currentComponent = component;
		mainWindow.revalidate();
		mainWindow.repaint();
		this.refreshCustomer();
	}

	public void resizeMainWindow(Dimension size) {
		Insets insets = mainWindow.getInsets();
		mainWindow.setSize(
				new Dimension(size.width + insets.left + insets.right, size.height + insets.top + insets.bottom));
	}

	public void refreshCustomer() {
		if (mainCustomer != null) {
			mainCustomer = customerService.findById(mainCustomer.getId());
		}
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		pcs.addPropertyChangeListener(listener);
	}
}
