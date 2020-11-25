package ca.qc.cvm.dba.persinteret.event;

public class CorrectionEvent extends CommonEvent {
	private String name;
	private String pwd;
	private String ip;
	
	public CorrectionEvent(String ip, String name, String pwd) {
		super(CommonEvent.Type.Correction);
		
		this.name = name;
		this.ip = ip;
		this.pwd = pwd;
	}
	
	public String getName() {
		return name;
	}
	
	public String getIp() {
		return ip;
	}

	public String getPwd() {
		return pwd;
	}
}
