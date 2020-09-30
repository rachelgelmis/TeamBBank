package com.teambbank.standalonedemo.view;

import java.awt.ComponentOrientation;
import java.io.IOException;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import org.springframework.core.io.ClassPathResource;

import com.teambbank.standalonedemo.model.SortingStatusEnum;

public class SortingButton extends JButton {
	/**
	 * Auto-generated
	 */
	private static final long serialVersionUID = 2660622510319377753L;
	private SortingStatusEnum sortStatus;
	private URL upChevron;
	private URL downChevron;
	private URL offChevron;

	public SortingButton() {
		initialize();
	}

	public SortingButton(String s) {
		super(s);
		initialize();
	}

	private void initialize() {
		try {
			upChevron = new ClassPathResource("images/up-chevron.png").getURL();
			downChevron = new ClassPathResource("images/down-chevron.png").getURL();
			offChevron = new ClassPathResource("images/empty16x16.png").getURL();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.setStatus(SortingStatusEnum.OFF);
		this.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
	}


	public SortingStatusEnum getStatus() {
		return sortStatus;
	}

	public void rotateStatus() {
		switch (this.sortStatus) {
		case ASCENDING:
			this.setStatus(SortingStatusEnum.DESCENDING);
			break;
		case DESCENDING:
			this.setStatus(SortingStatusEnum.ASCENDING);
			break;
		case OFF:
			this.setStatus(SortingStatusEnum.ASCENDING);
			break;
		default:
			break;
		}
	}

	public void setStatus(SortingStatusEnum status) {
		this.sortStatus = status;
		switch (this.sortStatus) {
		case ASCENDING:
				this.setIcon(new ImageIcon(upChevron));
			break;
		case DESCENDING:
			this.setIcon(new ImageIcon(downChevron));
			break;
		case OFF:
			this.setIcon(new ImageIcon(offChevron));
			break;
		default:
			this.setIcon(null);
			break;
		}
	}
}
