package server;

import java.io.Serializable;


@SuppressWarnings("serial")
public class OutputMessage implements Serializable
{
	/** Nombre del fichero que se transmite. Por defecto "" */
	public String nombreFichero="";

	/** Si este es el �ltimo mensaje del fichero*/
	public boolean ultimoMensaje=true;

	/** Cuantos bytes son v�lidos en el array de bytes */
	public int bytesValidos=0;

	/** Array con bytes leidos del fichero */
	public byte[] contenidoFichero = new byte[LONGITUD_MAXIMA];

	/** N�mero m�ximo de bytes que se envia�n en cada mensaje */
	public final static int LONGITUD_MAXIMA=10;
}