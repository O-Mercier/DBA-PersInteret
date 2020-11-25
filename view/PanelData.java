package ca.qc.cvm.dba.persinteret.view;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

import ca.qc.cvm.dba.persinteret.app.Facade;
import ca.qc.cvm.dba.persinteret.view.util.BackgroundPanel;

public class PanelData extends CommonPanel {
	private static final long serialVersionUID = 1L;
	
	private JLabel freeRatio; 
	private JLabel peopleCount;
	private JLabel photoCount;
	private JLabel youngestPerson;
	private JLabel nextTarget;
	private JLabel avgAge;

	public PanelData(int width, int height) throws Exception {
		super(width, height, true, "assets/images/background-data-menu.jpg");
		jbInit();
	}
	
	@Override
	protected void jbInit() throws Exception {		
		int y = 20;
		super.addLabel("Nombre de personnes sauvegardées", 20, y, 400, 30);
		peopleCount = super.addLabel("", 500, y, 100, 30);
		peopleCount.setHorizontalAlignment(JLabel.CENTER);
		peopleCount.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		
		y += 40;
		super.addLabel("Nombre de photos sauvegardées", 20, y, 400, 30);
		photoCount = super.addLabel("", 500, y, 100, 30);
		photoCount.setHorizontalAlignment(JLabel.CENTER);
		photoCount.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		
		y += 40;
		super.addLabel("Pourcentage de personnes en liberté", 20, y, 400, 30);
		freeRatio = super.addLabel("", 500, y,100, 30);
		freeRatio.setHorizontalAlignment(JLabel.CENTER);
		freeRatio.setBorder(BorderFactory.createLineBorder(Color.WHITE));

		y += 40;
		super.addLabel("Âge moyen des personnes", 20, y, 400, 30);
		avgAge= super.addLabel("", 500, y, 100, 30);
		avgAge.setHorizontalAlignment(JLabel.CENTER);
		avgAge.setBorder(BorderFactory.createLineBorder(Color.WHITE));

		y += 80;
		super.addLabel("La personne la plus jeune", 20, y, 400, 30);
		youngestPerson = super.addLabel("", 500, y, 250, 30);
		youngestPerson.setHorizontalAlignment(JLabel.CENTER);
		youngestPerson.setBorder(BorderFactory.createLineBorder(Color.WHITE));

		y += 40;
		super.addLabel("La prochaine personne cible", 20, y, 400, 30);
		nextTarget= super.addLabel("", 500, y, 250, 30);
		nextTarget.setHorizontalAlignment(JLabel.CENTER);
		nextTarget.setBorder(BorderFactory.createLineBorder(Color.WHITE));
	}
	
	/**
	 * Cette méthode est appelée automatiquement à chaque fois qu'un panel est affiché (lorsqu'on arrive sur la page)
	 */
	@Override
	public void resetUI() {
		freeRatio.setText(Facade.getInstance().getFreeRatio() + "%");
		photoCount.setText(Facade.getInstance().getPhotoCount() + "");
		peopleCount.setText(Facade.getInstance().getPeopleCount() + "");
		youngestPerson.setText(Facade.getInstance().getYoungestPerson());
		nextTarget.setText(Facade.getInstance().getNextTargetName());
		avgAge.setText(Facade.getInstance().getAverageAge() + "");
		
	}

}
