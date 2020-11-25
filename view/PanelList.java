package ca.qc.cvm.dba.persinteret.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import ca.qc.cvm.dba.persinteret.app.Facade;
import ca.qc.cvm.dba.persinteret.entity.Person;
import ca.qc.cvm.dba.persinteret.event.DeleteEvent;
import ca.qc.cvm.dba.persinteret.event.GoToEvent;
import ca.qc.cvm.dba.persinteret.view.util.BackgroundPanel;

public class PanelList extends CommonPanel {
	private static final long serialVersionUID = 1L;

	private JTextField searchField;
	private JList<String> peopleList;
	private JButton editButton;
	private JButton deleteButton;
	
	private List<Person> people;

	public PanelList(int width, int height) throws Exception {
		super(width, height, true, "assets/images/background-log-menu.jpg");
	}
	
	@Override
	public void jbInit() throws Exception {				
		editButton = this.addButton("Modifier", 600, 20, 100, 20, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Facade.getInstance().processEvent(new GoToEvent(FrameMain.Views.PersonSave, people.get(peopleList.getSelectedIndex())));
			}
		});
		
		deleteButton = this.addButton("Supprimer", 750, 20, 100, 20, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (JOptionPane.showConfirmDialog(PanelList.this, "Supprimer l'entrée?") == JOptionPane.OK_OPTION) {
					Facade.getInstance().processEvent(new DeleteEvent(people.get(peopleList.getSelectedIndex())));
				}
			}
		});
		
		// Search button
		searchField = new JTextField();
		this.addField(searchField, 20, 20, 150, 25);
		searchField.setFont(new Font("Arial", 0, 14));
		this.addButton("Rechercher", 200, 20, 100, 25, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				PanelList.this.resetView();
			}
		});

		this.addButton("Ajouter une entrée", 20, 510, 150, 25, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Facade.getInstance().processEvent(new GoToEvent(FrameMain.Views.PersonSave));
			}
		});
		

		peopleList = new JList<String>();
		peopleList.setFont(new Font("Courier New", 0, 18));
		super.addField(new JScrollPane(peopleList), 20, 60, 830, 440);
		peopleList.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (peopleList.getSelectedIndex() < 0) {
					deleteButton.setVisible(false);
					editButton.setVisible(false);
				}
				else {
					deleteButton.setVisible(true);
					editButton.setVisible(true);
				}
			}
		});
	}
	
	/**
	 * Cette méthode est appelée automatiquement à chaque fois qu'un panel est affiché (lorsqu'on arrive sur la page)
	 */
	@Override
	public void resetUI() {
		deleteButton.setVisible(false);
		editButton.setVisible(false);
		peopleList.removeAll();
		people = Facade.getInstance().getPeopleList(searchField.getText(), true, 50);

		DefaultListModel<String> model = new DefaultListModel<String>();
		
		for (Person p : people) {
			model.addElement(p.toString());
		}
		
		peopleList.setModel(model);
	}

}
