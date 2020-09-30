package com.teambbank.standalonedemo;

import java.util.Calendar;

import javax.annotation.PostConstruct;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import com.teambbank.standalonedemo.entity.CustomerEntity;
import com.teambbank.standalonedemo.entity.EmployeeEntity;
import com.teambbank.standalonedemo.model.BankAccountType;
import com.teambbank.standalonedemo.model.EmployeeType;
import com.teambbank.standalonedemo.service.BankAccountService;
import com.teambbank.standalonedemo.service.CustomerService;
import com.teambbank.standalonedemo.service.EmployeeService;
import com.teambbank.standalonedemo.view.ViewController;

@SpringBootConfiguration
@ComponentScan(basePackages = "com.teambbank.standalonedemo")
@EnableAutoConfiguration
public class StandaloneDemoApplication extends JFrame {

	/**
	 * Auto-generated UID
	 */
	private static final long serialVersionUID = 4859794790895737144L;
	@Autowired
	private transient CustomerService customerService;
	@Autowired
	private transient EmployeeService employeeService;
	@Autowired
	private transient ViewController viewController;
	@Autowired
	private transient BankAccountService bankService;

	protected StandaloneDemoApplication() {
		super("standalonedemoapp");
	}

	@PostConstruct
	private void prepareGUI() {
		viewController.showLoginMenu(true);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);

		// TODO remove before release
		// customerService.deleteAll();
		
		if (testCustomerExists()) {
//			deleteTestCustomer();
		}
		
		if (!testCustomerExists()) {
			//createTestCustomer();
			EmployeeEntity admin = employeeService.createEmployee("admin", "test", "admin", "p", EmployeeType.ADMIN);
			employeeService.saveChanges();
		}
	}

	public boolean testCustomerExists() {
		return (employeeService.findByUsername("admin") != null) ? true : false;
	}

	public void createTestCustomer() {
		CustomerEntity foo = customerService.createCustomer("Alex", "Barr", "111-11-1111", Calendar.getInstance(),
				"Memory Lane", "foobarr", "foobarr", "256-555-5555");
		for (int i = 0; i < 100; i++) {
			bankService.createAccount("test " + i, foo, BankAccountType.HOME_MORTGAGE, i + 11, 0, 0);
			bankService.saveChanges();
			customerService.saveChanges();
		}
		bankService.saveChanges();
		customerService.saveChanges();
		
		EmployeeEntity admin = employeeService.createEmployee("admin", "test", "admin", "p", EmployeeType.ADMIN);
		employeeService.saveChanges();
	}

	public void deleteTestCustomer() {
		customerService.deleteCustomer(customerService.findByUsername("foobarr"));
	}

	public static void main(String[] args) {
		ConfigurableApplicationContext ctx = new SpringApplicationBuilder(StandaloneDemoApplication.class)
				.headless(false).run(args);

		SwingUtilities.invokeLater(() -> {

			StandaloneDemoApplication ex = ctx.getBean(StandaloneDemoApplication.class);
			ex.setVisible(true);
		});
	}
}
