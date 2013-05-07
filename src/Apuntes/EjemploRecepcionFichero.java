package Apuntes;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;


public class EjemploRecepcionFichero {
	
	
	 public static void main(String[] args)
	    {
	        EjemploRecepcionFichero sf = new EjemploRecepcionFichero();
	        try {
				sf.listen(8888);
			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }

	@SuppressWarnings("resource")
	 public void listen(int puerto) throws IOException, ClassNotFoundException{
		ServerSocket socketServidor = new ServerSocket(puerto);
		Socket cliente = socketServidor.accept();
		cliente.setSoLinger(true, 10);
		ObjectInputStream ois = new ObjectInputStream(cliente.getInputStream());
		Object mensaje = ois.readObject();

		if (mensaje instanceof GetMessage)
		{
			enviaFichero(
					((GetMessage) mensaje).nombreFichero,
					new ObjectOutputStream(cliente.getOutputStream()));
		}
	}

	@SuppressWarnings("resource")
	private static void enviaFichero(String fichero,java.io.ObjectOutputStream oos) throws IOException {
		// Una variable auxiliar para marcar cuando se envía el último mensaje
		boolean enviadoUltimo=false;

		// Se abre el fichero.
		FileInputStream fis = new FileInputStream(fichero);

		// Se instancia y rellena un mensaje de envio de fichero
		GetMessage mensaje = new GetMessage();
		mensaje.nombreFichero = fichero;

		// Se leen los primeros bytes del fichero en un campo del mensaje
		int leidos = fis.read(mensaje.contenidoFichero);

		// Bucle mientras se vayan leyendo datos del fichero
		while (leidos > -1)
		{               
			// Se rellena el número de bytes leidos
			mensaje.bytesValidos = leidos;

			// Si no se han leido el máximo de bytes, es porque el fichero
			// se ha acabado y este es el último mensaje
			if (leidos <GetMessage.LONGITUD_MAXIMA)
			{
				// Se marca que este es el último mensaje
				mensaje.ultimoMensaje = true;
				enviadoUltimo=true; 
			}
			else
				mensaje.ultimoMensaje = false;

			// Se envía por el socket   
			oos.writeObject(mensaje);

			// Si es el último mensaje, salimos del bucle.
			if (mensaje.ultimoMensaje)
				break;

			// Se crea un nuevo mensaje
			mensaje = new GetMessage();
			mensaje.nombreFichero = fichero;

			// y se leen sus bytes.
			leidos = fis.read(mensaje.contenidoFichero);
		}

		// En caso de que el fichero tenga justo un múltiplo de bytes de MensajeTomaFichero.LONGITUD_MAXIMA,
		// no se habrá enviado el mensaje marcado como último. Lo hacemos ahora.
		if (enviadoUltimo==false)
		{
			mensaje.ultimoMensaje=true;
			mensaje.bytesValidos=0;
			oos.writeObject(mensaje);
		}
		// Se cierra el ObjectOutputStream
		oos.close();

		// TODO Auto-generated method stub

	}



}
