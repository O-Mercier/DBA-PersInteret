package ca.qc.cvm.dba.persinteret.event;

/**
 * Super classe pour tous les événements. Vous pouvez ajouter
 * des sous-classes pour vos événements. N'oubliez pas dans ce cas
 * d'ajouter un <<Type>> (voir plus bas)
 *
 */
public abstract class CommonEvent {
	public enum Type {General, UI, GoTo, Back, Save, Delete, DeleteAll, Correction};
	
	private Type type;
	
	public CommonEvent(Type type) {
		this.type = type;
	}
	
	public Type getType() {
		return type;
	}
}
