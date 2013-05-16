package Class;

import java.io.Serializable;

public class Usuario implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4618750768408643388L;
	private String user;
	private String pass;
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}

}
