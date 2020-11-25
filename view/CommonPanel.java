package ca.qc.cvm.dba.persinteret.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import ca.qc.cvm.dba.persinteret.app.Facade;
import ca.qc.cvm.dba.persinteret.event.BackEvent;
import ca.qc.cvm.dba.persinteret.event.GoToEvent;
import ca.qc.cvm.dba.persinteret.view.util.BackgroundPanel;

public abstract class CommonPanel extends JPanel{
	private static final long serialVersionUID = 1L;
	protected static final int LINE_HEIGHT = 25;
	protected int width;
	protected int height;
	private boolean addBackButton;
	public static final Font bigFont = new Font("Arial", 1, 20);
	private static Color bg = new Color(0, 0, 0, 150);
	private static Border border = BorderFactory.createLineBorder(Color.WHITE, 2);
	private String backgroundPath;
	private JPanel shadedPanel;
	private JPanel backgroundPanel;

	public CommonPanel(int width, int height, boolean addBackButton, String backgroundPath) throws Exception {
		this.width = width;
		this.height = height;
		this.addBackButton = addBackButton;
		this.backgroundPath = backgroundPath;
		initUI();
	}
	
	private void initUI() throws Exception {
		this.setPreferredSize(new Dimension(width, height));
		this.setBackground(Color.WHITE);
		
		this.setLayout(null);
		
		shadedPanel = new JPanel(){
		    protected void paintComponent(Graphics g)
		    {
		        g.setColor( getBackground() );
		        g.fillRect(0, 0, getWidth(), getHeight());
		        super.paintComponent(g);
		    }
		};
		shadedPanel.setLayout(null);
		shadedPanel.setBackground(bg);
		shadedPanel.setBorder(border);
		shadedPanel.setOpaque(false);
		shadedPanel.setBounds(10, 10, this.width - 30, this.height - 50);

		if (addBackButton) {
			this.addButton("Retour", 750, 510, 100, 20, new ActionListener() {
	
				@Override
				public void actionPerformed(ActionEvent e) {
					Facade.getInstance().processEvent(new BackEvent());
				}
			});
		}
		
		jbInit();
		
		this.add(shadedPanel);
		
		backgroundPanel = new BackgroundPanel(backgroundPath, width, height);
		this.add(backgroundPanel, new Dimension(0, 0));
	}
	
	protected abstract void jbInit() throws Exception;
	
	protected Component addField(Component component, int x, int y, int w, int h) {
		component.setFont(bigFont);
		shadedPanel.add(component);
		component.setBounds(x, y, w, h);
		
		return component;
	}
	
	protected JLabel addLabel(String text, int x, int y, int w, int h) {
		JLabel label = new JLabel(text);
		label.setFont(bigFont);
		label.setForeground(Color.WHITE);
		shadedPanel.add(label);
		label.setBounds(x, y, w, h);
		
		return label;
	}
	
	protected JButton addButton(String text, int x, int y, int w, int h, ActionListener actionListener) {
		JButton btn = new JButton(text);
		shadedPanel.add(btn);
		btn.setBounds(x, y, w, h);
		btn.addActionListener(actionListener);
		
		return btn;
	}
	
	protected abstract void resetUI();
	
	public void resetView() {		
		resetUI();
	}
}
