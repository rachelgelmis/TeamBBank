package com.teambbank.standalonedemo.view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class ImagePanel extends JPanel {

	/**
	 * Auto-generated UID
	 */
	private static final long serialVersionUID = -2557306890615875288L;
	private transient BufferedImage image;
	
	public BufferedImage getBackgroundImage() {
		return image;
	}
	
	protected void setBackgroundImage(URL url) {
		try {
			image = ImageIO.read(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(image.getWidth(), image.getHeight());
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (image != null)
			g.drawImage(image, 0, 0, this);
	}

}
