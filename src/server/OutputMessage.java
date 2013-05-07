package server;

import java.io.Serializable;


@SuppressWarnings("serial")
public class OutputMessage implements Serializable
{
	/** Nombre del fichero que se transmite. Por defecto "" */
	public String nombreFichero="";

	/** Si este es el último mensaje del fichero*/
	public boolean ultimoMensaje=true;

	/** Cuantos bytes son válidos en el array de bytes */
	public int bytesValidos=0;

	/** Array con bytes leidos del fichero */
	public byte[] contenidoFichero = new byte[LONGITUD_MAXIMA];

	/** Número máximo de bytes que se enviaán en cada mensaje */
	public final static int LONGITUD_MAXIMA=10;
}