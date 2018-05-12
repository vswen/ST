package entity;

public class Subscriber {
	private int id;
	private String userName;
	private String domain;
	private String password;
	private String emailAddress;
	private String ha1;
	private String ha1b;
	private String rpid;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public String getHa1() {
		return ha1;
	}
	public void setHa1(String ha1) {
		this.ha1 = ha1;
	}
	public String getHa1b() {
		return ha1b;
	}
	public void setHa1b(String ha1b) {
		this.ha1b = ha1b;
	}
	public String getRpid() {
		return rpid;
	}
	public void setRpid(String rpid) {
		this.rpid = rpid;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPassword() {
		return password;
	}

}
