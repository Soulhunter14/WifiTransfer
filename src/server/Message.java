package server;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Message implements Serializable{
	
	private String orden;

	public String getOrden() {
		return orden;
	}

	public void setOrden(String orden) {
		this.orden = orden;
	}

}
