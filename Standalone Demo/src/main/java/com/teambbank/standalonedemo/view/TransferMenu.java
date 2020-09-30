package com.teambbank.standalonedemo.view;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

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
import javax.transaction.Transactional;

import org.jboss.jandex.Main;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.teambbank.standalonedemo.entity.BankAccountEntity;
import com.teambbank.standalonedemo.entity.CustomerEntity;
import com.teambbank.standalonedemo.exception.InvalidTransactionAmountException;
import com.teambbank.standalonedemo.exception.InvalidTransactionTypeException;
import com.teambbank.standalonedemo.model.TransactionType;
import com.teambbank.standalonedemo.service.BankAccountService;
import com.teambbank.standalonedemo.service.TransactionService;
import com.teambbank.standalonedemo.view.sortedlist.BankAccountSorterService;

import net.miginfocom.swing.MigLayout;

@Component
public class TransferMenu extends JPanel implements ActionListener, UpdateableGUI {

	/**
	 * Auto-generated UID
	 */
	private static final long serialVersionUID = -4089242413882969196L;
	@Autowired
	private transient ViewController viewController;
	@Autowired
	private transient BankAccountService bankService;
	private JTextField dollarAmountTextBox;
	@Autowired
	private transient TransactionService transactionService;

	private CustomerEntity customer;
	private CustomerChangeListener customerListener;
	private BankAccountScrollPane bankAccountFromPane;
	private BankAccountScrollPane bankAccountToPane;
	private JLabel fromLabel;
	private JLabel toLabel;
	private JSpinner centsSpinner;

	/**
	 * Create the application.
	 */
	public TransferMenu() {
		this.customerListener = new CustomerChangeListener();
	}

	/**
	 * Initialize the contents of the this.
	 */
	@PostConstruct
	private void initialize() {
		viewController.addPropertyChangeListener(customerListener);

		this.setPreferredSize(new Dimension(1280, 1023));
		this.setLayout(new MigLayout("fill", "[]", "[grow 10][grow 60][grow 30]"));

		JLabel lblNewLabel = new JLabel("Transfer");
		add(lblNewLabel, "cell 0 0,alignx center");
		lblNewLabel.setFont(new Font("Georgia", Font.PLAIN, 38));

		// First Panel
		JPanel panel = new JPanel();
		this.add(panel, "cell 0 1,grow");
		panel.setLayout(new MigLayout("fill", "[]", "[grow,sizegroup col][grow,sizegroup col]"));

		this.bankAccountFromPane = new BankAccountScrollPane(this, bankService);
		this.bankAccountFromPane.setFont(new Font("Georgia", Font.PLAIN, 22));
		panel.add(bankAccountFromPane, "cell 0 0,grow");

		this.bankAccountToPane = new BankAccountScrollPane(this, bankService);
		this.bankAccountToPane.setFont(new Font("Georgia", Font.PLAIN, 22));
		panel.add(bankAccountToPane, "cell 0 1,grow");

		// Second Panel
		JPanel secondPanel = new JPanel();
		this.add(secondPanel, "cell 0 2, grow");
		secondPanel.setLayout(new MigLayout("align center, fill"));

		// First Row
		JPanel firstRow = new JPanel(new MigLayout("fill", "[sizegroup cell, grow][sizegroup cell, grow]", "[]"));
		secondPanel.add(firstRow, "grow, span, wrap");

		this.fromLabel = new JLabel("From:");
		this.fromLabel.setFont(new Font("Georgia", Font.PLAIN, 38));
		firstRow.add(this.fromLabel, "align left");
		this.bankAccountFromPane.addListSelectionListener(e -> {
			if (this.bankAccountFromPane.getSelectedValue() != null) {
				this.fromLabel.setText("From: " + this.bankAccountFromPane.getSelectedValue().getName());
			}
		});

		this.toLabel = new JLabel("To:");
		this.toLabel.setFont(new Font("Georgia", Font.PLAIN, 38));
		firstRow.add(this.toLabel, "align left");
		this.bankAccountToPane.addListSelectionListener(e -> {
			if (this.bankAccountToPane.getSelectedValue() != null) {
				this.toLabel.setText("To: " + this.bankAccountToPane.getSelectedValue().getName());
			}
		});

		// Second Row
		JPanel amountCell = new JPanel(new MigLayout());
		secondPanel.add(amountCell);
		JLabel lblTransferAmount = new JLabel("Transfer Amount: $");
		lblTransferAmount.setFont(new Font("Georgia", Font.PLAIN, 38));
		amountCell.add(lblTransferAmount, "align right");

		this.dollarAmountTextBox = new JTextField();
		this.dollarAmountTextBox.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
				if (((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE)) {
					e.consume(); // ignore event
				}
			}
		});
		this.dollarAmountTextBox.setFont(new Font("Georgia", Font.PLAIN, 27));
		this.dollarAmountTextBox.setHorizontalAlignment(SwingConstants.RIGHT);
		this.dollarAmountTextBox.setColumns(10);
		amountCell.add(dollarAmountTextBox);

		JLabel lblDecimal = new JLabel(".");
		lblDecimal.setFont(new Font("Georgia", Font.PLAIN, 38));
		amountCell.add(lblDecimal);

		this.centsSpinner = new JSpinner();
		this.centsSpinner.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
				if (((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE)) {
					e.consume(); // ignore event
				}
			}
		});
		this.centsSpinner.setFont(new Font("Georgia", Font.PLAIN, 27));
		this.centsSpinner.setModel(new SpinnerNumberModel(0, null, 99, 1));
		amountCell.add(this.centsSpinner, "align left");

		JPanel buttonCell = new JPanel(new MigLayout());
		secondPanel.add(buttonCell);
		// This button creates a checkings or savings account for a customer and stores
		// it in the database.
		JButton transferButton = new JButton("Transfer");
		transferButton.setFont(new Font("Georgia", Font.PLAIN, 25));
		transferButton.addActionListener(e -> {
			makeTransfer();
		});
		buttonCell.add(transferButton, "gapafter 30");

		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(e -> {
			// do nothing but close the deposit menu and open the account page
			viewController.showTellerCustomerInfoMenu(true);
		});
		cancelButton.setFont(new Font("Georgia", Font.PLAIN, 25));
		buttonCell.add(cancelButton);

	}

	/*=============================================================
	 * Function: isBankAccountEmpty()
	 * Uses the public variables in the current class to complete
	 * a transfer from one bank account to another.
	 * params: none
	 * returns: none
	 ============================================================*/
	@Transactional
	private void makeTransfer() {
		BankAccountEntity fromBankAccount = bankAccountFromPane.getSelectedValue();
		BankAccountEntity toBankAccount = bankAccountToPane.getSelectedValue();

		// Check for single/no selections
		if (fromBankAccount == null || toBankAccount == null) {
			JOptionPane.showMessageDialog(new JFrame(), "You must select two bank accounts.", "Invalid Selection",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		// Check for identical accounts
		if (fromBankAccount.getId() == toBankAccount.getId()) {
			JOptionPane.showMessageDialog(new JFrame(), "You must select two different bank accounts.",
					"Invalid Selection", JOptionPane.ERROR_MESSAGE);
			return;
		}
		// Check for empty dollar string
		if (dollarAmountTextBox.getText().equals("")) {
			JOptionPane.showMessageDialog(new JFrame(), "Dollar amount field must not be empty.", "Invalid Input",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		// Check for invalid dollar string input
		if (!(dollarAmountTextBox.getText().matches("[0-9]+"))) {
			JOptionPane.showMessageDialog(new JFrame(), "Dollar amount field must only contains digits.",
					"Invalid Input", JOptionPane.ERROR_MESSAGE);
			return;
		}
		// Get the values needed to make a transfer
		int dollarValue = Integer.parseInt(dollarAmountTextBox.getText());
		
		// Check for a valid number
		try {
			dollarValue = Integer.parseInt(dollarAmountTextBox.getText());
		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(new JFrame(), "Dollar amount field has an invalid amount.", "Invalid Input",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		// Check for zero dollar amount
		if (dollarValue == 0) {
			JOptionPane.showMessageDialog(new JFrame(), "Dollar amount field must not be zero.", "Invalid Input",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		int centsValue = Integer.parseInt(centsSpinner.getValue().toString());
		long total = (long) dollarValue * 100 + (long) centsValue;
		
		// Catch any business logic errors
		try {
			transactionService.isValidTransfer(fromBankAccount, toBankAccount, total);
		} catch (InvalidTransactionAmountException ex) {
			JOptionPane.showMessageDialog(new JFrame(),
					"Insufficient Funds in Bank Account: \"" + fromBankAccount.getName() + ".\"", "Insufficient Funds",
					JOptionPane.ERROR_MESSAGE);
			return;
		} catch (InvalidTransactionTypeException ex) {
			JOptionPane.showMessageDialog(new JFrame(), "Cannot transfer between the two account types.", "ERROR",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		transactionService.createTransaction(fromBankAccount, total * -1, TransactionType.TRANSFER);
		transactionService.createTransaction(toBankAccount, total, TransactionType.TRANSFER);

		this.fromLabel.setText("");
		this.toLabel.setText("");
		updateGUI();
		viewController.showTellerCustomerInfoMenu(true);
	}

	public void updateGUI() {
		if (customer != null) {
			// Save the selected account before repainting
			BankAccountEntity selectedEntity = bankAccountFromPane.getSelectedValue();
			bankAccountFromPane.setListData(
					BankAccountSorterService.getSortedList(customer, bankAccountFromPane.getActiveSortingCriteria(),
							bankAccountFromPane.getActiveSortingStatus(), bankService));
			bankAccountFromPane.revalidate();
			bankAccountFromPane.repaint();
			bankAccountFromPane.setSelectedValue(selectedEntity);
			// Save the selected account before repainting
			selectedEntity = bankAccountToPane.getSelectedValue();
			bankAccountToPane.setListData(
					BankAccountSorterService.getSortedList(customer, bankAccountToPane.getActiveSortingCriteria(),
							bankAccountToPane.getActiveSortingStatus(), bankService));
			bankAccountToPane.revalidate();
			bankAccountToPane.repaint();
			bankAccountToPane.setSelectedValue(selectedEntity);
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
