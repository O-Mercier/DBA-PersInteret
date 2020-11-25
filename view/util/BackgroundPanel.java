package ca.qc.cvm.dba.persinteret.view.util;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class BackgroundPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private Image backgroundImage;
	
	public BackgroundPanel(String fileName, int width, int height) throws IOException {
		backgroundImage = ImageIO.read(new File(fileName));
		this.setSize(width, height);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	
		g.drawImage(backgroundImage, 0, 0, this);
	}
}