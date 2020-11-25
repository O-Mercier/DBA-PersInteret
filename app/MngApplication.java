package ca.qc.cvm.dba.persinteret.app;

import java.util.ArrayList;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

import ca.qc.cvm.dba.persinteret.dao.BerkeleyConnection;
import ca.qc.cvm.dba.persinteret.dao.MongoConnection;
import ca.qc.cvm.dba.persinteret.dao.PersonDAO;
import ca.qc.cvm.dba.persinteret.entity.Person;
import ca.qc.cvm.dba.persinteret.event.SaveEvent;
import ca.qc.cvm.dba.persinteret.event.CommonEvent;
import ca.qc.cvm.dba.persinteret.event.CorrectionEvent;
import ca.qc.cvm.dba.persinteret.event.DeleteEvent;
import ca.qc.cvm.dba.persinteret.event.GoToEvent;
import ca.qc.cvm.dba.persinteret.event.UIEvent;
import ca.qc.cvm.dba.persinteret.view.FrameMain.Views;

public class MngApplication implements Runnable {
    private List<CommonEvent> eventQueue;
    private Person currentPerson;

    private PropertyChangeSupport support;
    
    public MngApplication() {
    	eventQueue = new ArrayList<CommonEvent>();
    	support = new PropertyChangeSupport(this);
    }
    
    public void addEvent(CommonEvent event) {
    	synchronized(MngApplication.class) {
    		eventQueue.add(event);
    	}
    }

	public void run() {
        synchronized (MngApplication.class) {
            CommonEvent event = eventQueue.remove(0);
            processEvent(event);
        }
    }
	
	public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }
	
	/**
	 * Méthode appelée par la Facade. Elle permet de traiter l'événement.
	 * 
	 * @param event
	 */
	private void processEvent(CommonEvent event) {
		if (event.getType() == CommonEvent.Type.GoTo) {
			currentPerson = ((GoToEvent)event).getPerson();
			support.firePropertyChange(UIEvent.UIType.GoTo.toString(), null, ((GoToEvent)event).getDestination().toString());
		}
		else if (event.getType() == CommonEvent.Type.Back) {
			support.firePropertyChange( UIEvent.UIType.Back.toString(), null, "back");
		}
		else if (event.getType() == CommonEvent.Type.Save) {
			save(((SaveEvent)event).getPerson());
		}
		else if (event.getType() == CommonEvent.Type.Delete) {
			delete(((DeleteEvent)event).getPerson());
		}
		else if (event.getType() == CommonEvent.Type.DeleteAll) {
			deleteAll();
		}
		else if (event.getType() == CommonEvent.Type.Correction) {
			CorrectionEvent evt = (CorrectionEvent)event;
			CorClient c = new CorClient();
			c.start(evt.getIp(), evt.getName(), evt.getPwd());
		}
		else {
			support.firePropertyChange(UIEvent.UIType.ShowMessage.toString(), null, "Événement inconnu...");
		}
	}
	
	private void delete(Person person) {
		boolean success = false;
		
		success = PersonDAO.delete(person);
		
		if (success) {
			support.firePropertyChange(UIEvent.UIType.ShowMessage.toString(), null, "Personne supprimée");
			support.firePropertyChange( UIEvent.UIType.Refresh.toString(), null, "Refresh");
		}
		else {
			support.firePropertyChange(UIEvent.UIType.ShowMessage.toString(), null, "Désolé, il semble y avoir eu une erreur lors de la suppression");
		}
	}
	
	private void deleteAll() {
		if (PersonDAO.deleteAll()) {
			support.firePropertyChange(UIEvent.UIType.ShowMessage.toString(), null, "Base de données supprimées");
		}
		else {
			support.firePropertyChange(UIEvent.UIType.ShowMessage.toString(), null, "Une erreur est survenue lors de la suppression totale");
		}
	}
	
	private void save(Person person) {
		boolean success = false;
		
		success = PersonDAO.save(person);
		
		if (success) {
			support.firePropertyChange(UIEvent.UIType.ShowMessage.toString(), null, "Sauvegarde effectuée");
			support.firePropertyChange(UIEvent.UIType.GoTo.toString(), null, Views.List.toString());
		}
		else {
			support.firePropertyChange(UIEvent.UIType.ShowMessage.toString(), null, "Désolé, veuillez vérifier vos données");
		}
	}
	
	public List<Person> getPeopleList(String filterText, boolean withImage, int limit) {
		List<Person> peopleList = PersonDAO.getPeopleList(filterText, withImage, limit);
				
		return peopleList;
	}
	
	public int getFreeRatio() {
		return PersonDAO.getFreeRatio();
	}
	
	public long getPhotoCount() {
		return PersonDAO.getPhotoCount();
	}
	
	public long getPeopleCount() {
		return PersonDAO.getPeopleCount();
	}
	
	public String getYoungestPerson() {
		return PersonDAO.getYoungestPerson();
	}
	
	public String getNextTargetName() {
		return PersonDAO.getNextTargetName();
	}
	
	public int getAverageAge() {
		return PersonDAO.getAverageAge();
	}
	
	public Person getCurrentPerson() {
		return currentPerson;
	}
	
	public void exit() {
		MongoConnection.releaseConnection();
		BerkeleyConnection.releaseConnection();
		System.exit(0);
	}
}
