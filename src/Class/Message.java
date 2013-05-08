package Class;

import java.io.File;
import java.io.Serializable;


public class Message implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String orden;
	private String path;
	private File[] list;

	public String getOrden() {
		return orden;
	}

	public void setOrden(String orden) {
		this.orden = orden;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public File[] getList() {
		return list;
	}

	public void setList(File[] list) {
		this.list = list;
	}

}
