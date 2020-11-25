package ca.qc.cvm.dba.persinteret.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;

import ca.qc.cvm.dba.persinteret.app.Facade;
import ca.qc.cvm.dba.persinteret.entity.Person;
import ca.qc.cvm.dba.persinteret.event.SaveEvent;
import ca.qc.cvm.dba.persinteret.view.util.BackgroundPanel;

public class PanelPersonSave extends CommonPanel {
	private static final long serialVersionUID = 1L;
	private JTextField nameField;
	private JTextField codeNameField;
	private JTextField dateOfBirthField;
	private JComboBox<String> statusField;
	private ImageIcon photoImg;
	private JList<String> connections;
	private JButton deleteConnectionBtn;
	
	private Person currentPerson;
	private List<String> connectionNames;
	private JLabel photoImage;

	public PanelPersonSave(int width, int height) throws Exception {
		super(width, height, true, "assets/images/background-log-add.jpg");
	}
	
	@Override
	public void jbInit() throws Exception {
		super.addLabel("Nom : ", 20, 20, 150, 30);
		nameField = new JTextField();
		super.addField(nameField, 200, 20, 250, 30);
		
		super.addLabel("Nom de code : ", 20, 70, 150, 30);
		codeNameField = new JTextField();
		super.addField(codeNameField, 200, 70, 250, 30);

		super.addLabel("Statut : ", 20, 120, 150, 30);
		Vector<String> choices = new Vector<String>();
		choices.add("Libre");
		choices.add("Prison");
		choices.add("Disparu");
		choices.add("Mort");
		statusField = new JComboBox<String>(choices);
		super.addField(statusField, 200, 120, 250, 30);
		
		super.addLabel("Photo : ", 20, 170, 150, 30);
		photoImage = super.addLabel("", 600, 20, 250, 250);
		photoImage.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		super.addButton("Choisir", 200, 170, 100, 30, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
		        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		        fileChooser.setFileFilter(new FileFilter(){

					@Override
					public boolean accept(File f) {
						return f.isDirectory() || f.toString().endsWith(".jpg");
					}

					@Override
					public String getDescription() {
						return null;
					}
		        	
		        });
		        int resultat = fileChooser.showSaveDialog(PanelPersonSave.this);

	            if (resultat != JFileChooser.CANCEL_OPTION) {
	            	try {
	            		BufferedImage inputImage = ImageIO.read(fileChooser.getSelectedFile());
	            		BufferedImage outputImage = new BufferedImage(250, 250, inputImage.getType());
		                Graphics2D g2d = outputImage.createGraphics();
		                g2d.drawImage(inputImage, 0, 0, 250, 250, null);
		                g2d.dispose();
		                
		                
		            	photoImg = new ImageIcon(outputImage);		            	
		            	photoImage.setIcon(photoImg);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
	            }
			}
		});
		
		super.addLabel("Date naiss. : ", 20, 220, 150, 30);
		dateOfBirthField = new JTextField();
		super.addField(dateOfBirthField, 200, 220, 250, 30);
		dateOfBirthField.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent e) {
				if (dateOfBirthField.getText().equals("YYYY-MM-DD")) {
					dateOfBirthField.setText("");
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (dateOfBirthField.getText().equals("")) {
					dateOfBirthField.setText("YYYY-MM-DD");
				}
			}
			
		});
		
		connections = new JList<String>();
		connections.setFont(bigFont);
		super.addField(new JScrollPane(connections), 200, 270, 250, 150);
		connections.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (connections.getSelectedIndex() < 0) {
					deleteConnectionBtn.setEnabled(false);
				}
				else {
					deleteConnectionBtn.setEnabled(true);
				}
			}
		});
		
		super.addButton("Ajouter", 200, 430, 100, 20, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				List<Person> person = Facade.getInstance().getPeopleList("", false, 100);
				final Vector<String> vector = new Vector<String>();
				
				for (Person p : person) {
					if (!connectionNames.contains(p.getName()) && (currentPerson == null || !p.getName().equals(currentPerson.getName()))) {
						vector.add(p.getName());
					}
				}
				
				if (vector.size() > 0) {
			        JButton btn = new JButton("Ajouter");
			        btn.setSize(new Dimension(100, 20));
			        final JComboBox jcd = new JComboBox(vector);
			        final JDialog diag = new JDialog();
			        
			        btn.addActionListener(new ActionListener() {
	
						@Override
						public void actionPerformed(ActionEvent e) {
							connectionNames.add(jcd.getSelectedItem().toString());
							vector.add(jcd.getSelectedItem().toString());
							
							updateConnectionList(connectionNames);
							
							diag.setVisible(false);
						}
			        });
	
			        Object[] options = new Object[] {};
			        JOptionPane jop = new JOptionPane("",
			                                        JOptionPane.QUESTION_MESSAGE,
			                                        JOptionPane.DEFAULT_OPTION,
			                                        null,options, null);
			        jop.setLayout(new BorderLayout());
			        jop.add(jcd, BorderLayout.NORTH);		        
			        jop.add(btn, BorderLayout.SOUTH);
			        
	 
			        diag.setLocationRelativeTo(PanelPersonSave.this);
			        diag.getContentPane().add(jop);
			        diag.pack();
			        diag.setVisible(true);
				}
				else {
					JOptionPane.showMessageDialog(PanelPersonSave.this, "Aucune nouvelle connexion disponible");
				}
			}			
		});
		
		deleteConnectionBtn = super.addButton("Supprimer", 350, 430, 100, 20, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				connectionNames.remove(connections.getSelectedValue());
				
				updateConnectionList(connectionNames);
			}			
		});
		
		super.addButton("Sauvegarder", 200, 500, 100, 30, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String errorMsg = "";
				
				if (nameField.getText().trim().length() == 0) {
					errorMsg += "\n - Le nom ne peut pas être vide";
				}
				
				if (photoImg == null) {
					errorMsg += "\n - La photo ne peut pas être vide";
				}
				
				if (dateOfBirthField.getText() == "YYYY-MM-DD" || dateOfBirthField.getText().trim().length() != 10) {
					errorMsg += "\n - La date de naissance est invalide";
				}
								
				if (errorMsg.length() == 0) {
					Person p = new Person();
					p.setId(currentPerson != null ?currentPerson.getId() : null);
					p.setCodeName(codeNameField.getText());
					p.setName(nameField.getText());
					p.setStatus(statusField.getSelectedItem().toString());
					p.setDateOfBirth(dateOfBirthField.getText());
					p.setConnexions(connectionNames);
					
					BufferedImage bi = new BufferedImage(photoImg.getIconWidth(),photoImg.getIconHeight(), BufferedImage.TYPE_INT_RGB);
					Graphics g = bi.createGraphics();
					photoImg.paintIcon(null, g, 0,0);
					g.dispose();
					
				    try {
				    	ByteArrayOutputStream baos = new ByteArrayOutputStream();
				    	ImageOutputStream ios = ImageIO.createImageOutputStream(baos);
				        ImageIO.write(bi, "png", ios);
				        ios.close();
				        p.setImageData(baos.toByteArray());
				    } catch (Exception ee) {
				    	ee.printStackTrace();
				    }
				    
					Facade.getInstance().processEvent(new SaveEvent(p));
				}
				else {
					JOptionPane.showMessageDialog(PanelPersonSave.this, "Erreur : " + errorMsg);
				}
			}
		});
	}
	
	private void updateConnectionList(List<String> list) {		
		DefaultListModel<String> model = new DefaultListModel<String>();
		
		for (String c : list) {
			model.addElement(c);
		}
		
		connections.setModel(model);
	}
	
	/**
	 * Cette méthode est appelée automatiquement à chaque fois qu'un panel est affiché (lorsqu'on arrive sur la page).
	 * Elle peut donc servir à préparer l'interface graphique (vider les champs, remplir les combobox, etc)
	 */
	@Override
	public void resetUI() {
		connectionNames = new ArrayList<String>();
		deleteConnectionBtn.setEnabled(false);
		
		currentPerson = Facade.getInstance().getCurrentPerson();
		
		if (currentPerson != null) {
			nameField.setText(currentPerson.getName());
			statusField.setSelectedItem(currentPerson.getStatus());
			codeNameField.setText(currentPerson.getCodeName());
			dateOfBirthField.setText(currentPerson.getDateOfBirth());
			
			if (currentPerson.getImageData() != null) {
				photoImg = new ImageIcon(currentPerson.getImageData());
				photoImage.setIcon(photoImg);
				photoImage.repaint();
			}
			else {
				photoImg = null;
				photoImage.setIcon(null);	
			}
			
			if (currentPerson.getConnexions() != null) {
				for (String c : currentPerson.getConnexions()) {
					connectionNames.add(c);
				}
			}
		}
		else {
			statusField.setSelectedIndex(0);
			nameField.setText("");
			codeNameField.setText("");
			dateOfBirthField.setText("YYYY-MM-DD");
			photoImg = null;
			photoImage.setIcon(null);
		}

		updateConnectionList(connectionNames);
	}

}
