package ca.qc.cvm.dba.persinteret.event;

import ca.qc.cvm.dba.persinteret.entity.Person;

/**
 * Événement utilisé lorsque l'on veut sauvegarder une nouvelle
 * entrée 
 */
public class SaveEvent extends CommonEvent {
	private Person person;
	
	public SaveEvent(Person person) {
		super(CommonEvent.Type.Save);
		
		this.person = person;
	}
	
	public Person getPerson() {
		return person;
	}
}
