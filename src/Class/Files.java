package Class;

import java.io.Serializable;

public class Files implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;

	/** Nombre del fichero que se transmite. Por defecto "" */
	private String nombreFichero="";

	/** Si este es el último mensaje del fichero en cuestión o hay más después */
	private boolean ultimoMensaje=true;

	/** Cuantos bytes son válidos en el array de bytes */
	private int bytesValidos=0;

	/** Array con bytes leidos del fichero */
	private byte[] contenidoFichero = new byte[LONGITUD_MAXIMA];

	/** Número máximo de bytes que se enviaán en cada mensaje */
	public final static int LONGITUD_MAXIMA=10;

	public String getNombreFichero() {
		return nombreFichero;
	}

	public void setNombreFichero(String nombreFichero) {
		this.nombreFichero = nombreFichero;
	}

	public boolean isUltimoMensaje() {
		return ultimoMensaje;
	}

	public void setUltimoMensaje(boolean ultimoMensaje) {
		this.ultimoMensaje = ultimoMensaje;
	}

	public int getBytesValidos() {
		return bytesValidos;
	}

	public void setBytesValidos(int bytesValidos) {
		this.bytesValidos = bytesValidos;
	}

	public byte[] getContenidoFichero() {
		return contenidoFichero;
	}

	public void setContenidoFichero(byte[] contenidoFichero) {
		this.contenidoFichero = contenidoFichero;
	}
}
