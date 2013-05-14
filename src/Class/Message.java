package Class;

import java.io.Serializable;
import java.util.Vector;


public class Message implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String orden;
	private String path;
	private String so;
	private Vector<String> dire = new Vector<String>();
	private Vector<String> docs = new Vector<String>();
	private String error;

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

	public Vector<String> getDire() {
		return dire;
	}

	public void setDire(Vector<String> dire) {
		this.dire = dire;
	}

	public Vector<String> getDocs() {
		return docs;
	}

	public void setDocs(Vector<String> docs) {
		this.docs = docs;
	}
	
	public void addDire(String dire)
	{
		this.dire.add(dire);
	}

	public void addDocs(String docs)
	{
		this.docs.add(docs);
	}

	public String getSo() {
		return so;
	}

	public void setSo(String so) {
		this.so = so;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
}
