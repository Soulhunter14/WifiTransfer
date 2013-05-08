package Class;

import java.io.Serializable;

public class Files implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;

	/** Nombre del fichero que se transmite. Por defecto "" */
	private String nombreFichero="";

	/** Si este es el �ltimo mensaje del fichero en cuesti�n o hay m�s despu�s */
	private boolean ultimoMensaje=true;

	/** Cuantos bytes son v�lidos en el array de bytes */
	private int bytesValidos=0;

	/** Array con bytes leidos del fichero */
	private byte[] contenidoFichero = new byte[LONGITUD_MAXIMA];

	/** N�mero m�ximo de bytes que se envia�n en cada mensaje */
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
