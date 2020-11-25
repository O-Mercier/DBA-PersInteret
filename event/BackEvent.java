package ca.qc.cvm.dba.persinteret.event;

/**
 * Événement permettant de retourner à un panel parent 
 * (C'est pour le bouton retour)
 */
public class BackEvent extends CommonEvent {
	
	public BackEvent() {
		super(CommonEvent.Type.Back);
	}
}
