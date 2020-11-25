package ca.qc.cvm.dba.persinteret.app;

import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Observer;

import ca.qc.cvm.dba.persinteret.dao.PersonDAO;
import ca.qc.cvm.dba.persinteret.entity.Person;
import ca.qc.cvm.dba.persinteret.event.CommonEvent;

/**
 * Cette classe est l'intermédiaire entre la logique et la vue
 * Entre les panel et le MngApplication. C'est le point d'entrée de la vue
 * vers la logique
 */
public class Facade {
	private static Facade instance;
	
	private MngApplication app;
	
	private Facade() {
		app = new MngApplication();
	}
	
	public static Facade getInstance() {
		if (instance == null) {
			instance = new Facade();
		}
		
		return instance;
	}
	
	public void processEvent(CommonEvent event) {
		app.addEvent(event);
        new Thread(app).start();
	}
	
	public void addObserverClass( PropertyChangeListener pcl) {
		app.addPropertyChangeListener(pcl);
	}
	
	public List<Person> getPeopleList(String filterText, boolean withImage, int limit) {
		return app.getPeopleList(filterText, withImage, limit);
	}
	
	public Person getCurrentPerson() {
		return app.getCurrentPerson();
	}
	
	public int getFreeRatio() {
		return app.getFreeRatio();
	}
	
	public long getPhotoCount() {
		return app.getPhotoCount();
	}
	
	public long getPeopleCount() {
		return app.getPeopleCount();
	}
		
	public String getYoungestPerson() {
		return app.getYoungestPerson();
	}	

	public String getNextTargetName() {
		return app.getNextTargetName();
	}
	
	public int getAverageAge() {
		return app.getAverageAge();
	}
		
	public void exit() {
		app.exit();
	}
}
