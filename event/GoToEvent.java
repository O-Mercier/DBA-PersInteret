package ca.qc.cvm.dba.persinteret.event;

import ca.qc.cvm.dba.persinteret.entity.Person;
import ca.qc.cvm.dba.persinteret.view.FrameMain.Views;

/**
 * Événement pour passer d'un panel à un autre
 */
public class GoToEvent extends CommonEvent {
	private Views destination;
	private Person currentPerson;
	
	public GoToEvent(Views destination) {
		super(CommonEvent.Type.GoTo);
		
		this.destination = destination;
	}
	
	public GoToEvent(Views destination, Person person) {
		super(CommonEvent.Type.GoTo);
		
		this.destination = destination;
		this.currentPerson = person;
	}
	
	public Person getPerson() {
		return currentPerson;
	}
	
	public Views getDestination() {
		return destination;
	}
}
