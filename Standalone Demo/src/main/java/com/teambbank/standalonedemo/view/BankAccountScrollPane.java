package com.teambbank.standalonedemo.view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.event.ListSelectionListener;

import com.teambbank.standalonedemo.entity.BankAccountEntity;
import com.teambbank.standalonedemo.entity.CustomerEntity;
import com.teambbank.standalonedemo.model.BankAccountListColumnEnum;
import com.teambbank.standalonedemo.model.SortingStatusEnum;
import com.teambbank.standalonedemo.service.BankAccountService;

import net.miginfocom.swing.MigLayout;

public class BankAccountScrollPane extends JScrollPane {

	/**
	 * Auto-generated UID
	 */
	private transient CustomerEntity customer;
	private transient BankAccountService bankService;
	private static final long serialVersionUID = -8405861311522025665L;
	private final Dimension defaultSize = new Dimension(1260, 640);
	private List<BankAccountListColumnEnum> columnEnums = Arrays.asList(BankAccountListColumnEnum.values());
	private final Font defaultFont = new Font("Georgia", Font.PLAIN, 22);
	private JList<BankAccountEntity> viewportComponent;
	private SortedMap<BankAccountListColumnEnum, SortingButton> colHeaderButtonMap;
	private SortingStatusEnum activeSortingStatus;
	private BankAccountListColumnEnum activeSortingCriteria;
	// For parties interested on the event we sort
	private transient ActionListener sortListener;

	public BankAccountScrollPane(ActionListener listener, BankAccountService bankService) {
		this.bankService = bankService;
		this.viewportComponent = new JList<>();
		this.colHeaderButtonMap = new TreeMap<>();
		this.setViewportView(this.viewportComponent);
		this.activeSortingStatus = SortingStatusEnum.OFF;
		this.activeSortingCriteria = BankAccountListColumnEnum.values()[0];
		this.sortListener = listener;
		initialize();
	}

	private void initialize() {
		viewportComponent.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		viewportComponent.setLayoutOrientation(JList.VERTICAL);
		viewportComponent.setFont(defaultFont);
		viewportComponent.setCellRenderer(new BankAccListCellRenderer());
		this.setSize(defaultSize);
		for (BankAccountListColumnEnum col : columnEnums) {
			SortingButton btn = new SortingButton(col.getName());
			btn.addActionListener(e -> {
				btn.rotateStatus();
				// Turn all sorting off
				SortingStatusEnum statusEnum = btn.getStatus();
				for (SortingButton b : this.colHeaderButtonMap.values()) {
					b.setStatus(SortingStatusEnum.OFF);
				}
				btn.setStatus(statusEnum);
				this.activeSortingStatus = statusEnum;
				this.activeSortingCriteria = col;
				// Notify whoever needs to know that we sorted
				this.sortListener.actionPerformed(null);
			});
			colHeaderButtonMap.put(col, btn);
		}

		List<String> colNames = new ArrayList<>();
		List<JButton> colButtons = new ArrayList<>();
		for (BankAccountListColumnEnum col : columnEnums) {
			colNames.add(col.getName());
			colButtons.add(colHeaderButtonMap.get(col));
		}
		BankAccountRow<SortingButton> header = new BankAccountRow<>(colNames,
				new ArrayList<>(this.colHeaderButtonMap.values()));
		header.setBorder(new BevelBorder(BevelBorder.RAISED));
		this.setColumnHeaderView(header);
	}

	public SortingStatusEnum getActiveSortingStatus() {
		if (this.activeSortingStatus == SortingStatusEnum.OFF)
			return SortingStatusEnum.ASCENDING;
		return this.activeSortingStatus;
	}

	public BankAccountListColumnEnum getActiveSortingCriteria() {
		return this.activeSortingCriteria;
	}

	public BankAccountEntity getSelectedValue() {
		return viewportComponent.getSelectedValue();
	}
	
	public void setSelectedValue(BankAccountEntity account) {
		viewportComponent.setSelectedValue(account, false);
	}

	public void setListData(List<BankAccountEntity> list) {
		viewportComponent.setListData(list.toArray(new BankAccountEntity[0]));
	}


	public void addListSelectionListener(ListSelectionListener l) {
		this.viewportComponent.getSelectionModel().addListSelectionListener(l);
	}

	@Override
	public void setFont(Font font) {
		super.setFont(font);
		if (colHeaderButtonMap != null) {
			for (JButton b : colHeaderButtonMap.values()) {
				b.setFont(font);
			}
		}
	}

	private class BankAccListCellRenderer extends DefaultListCellRenderer {
		/**
		 * Auto-generated UID
		 */
		private static final long serialVersionUID = 4790884975051067258L;

		@Override
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
			BankAccountEntity accModel = (BankAccountEntity) list.getModel().getElementAt(index);
			List<String> bankData = bankService.getColumnRep(accModel);
			BankAccountRow<JLabel> row = new BankAccountRow<>(bankData);
			if (isSelected) {
				row.setBackground(UIManager.getColor("List.selectionBackground"));
				row.setForeground(UIManager.getColor("List.selectionForeground"));
			}
			return row;
		}
	}

	private class BankAccountRow<T extends JComponent> extends JPanel {
		/**
		 * Auto-generated UID
		 */
		private static final long serialVersionUID = 2364355510345666332L;
		private List<T> colComponents;

		public BankAccountRow(List<String> columnNames) {
			this(columnNames, Collections.emptyList());
		}

		@SuppressWarnings("unchecked")
		public BankAccountRow(List<String> columnNames, List<T> components) {
			this.colComponents = new ArrayList<>();
			for (T comp : components) {
				this.colComponents.add(comp);
			}
			if (components.isEmpty()) {
				for (String s : columnNames) {
					colComponents.add((T) new JLabel(s));
				}
			}
			this.initialize();
		}

		public void initialize() {
			// setting the MigLayout columns
			StringBuilder layoutConstraints = new StringBuilder();
			for (int i = 0; i < colComponents.size(); i++) {
				// Set the columns under the same sizegroup so that they will be the same size
				layoutConstraints.append("[sizegroup col]");
			}
			if (colComponents.get(0) instanceof JButton) {
				this.setLayout(new MigLayout("fillx, insets 0", layoutConstraints.toString()));
			} else {
				this.setLayout(new MigLayout("fillx", layoutConstraints.toString()));
			}
			this.setSize(BankAccountScrollPane.this.getSize());
			for (JComponent c : colComponents) {
				this.add(c, "growx");
			}
			for (Component c : this.getComponents()) {
				c.setFont(defaultFont);

			}
		}
	}
}
