package server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import Class.Files;
import Class.Message;


public class ThreadCliente implements Runnable{

	private Socket socket;
	public ThreadCliente(Socket socket)
	{
		this.socket = socket;
	}

	@Override
	public void run() {

		System.out.println("Bienvenido al servidor");

		while (true)
		{
			// Código para atender la peticion
			ObjectInputStream ois;
			try {
				ois = new ObjectInputStream(socket.getInputStream());
				Object mensaje = ois.readObject();

				if (mensaje instanceof Message){
					System.out.println(((Message) mensaje).getOrden());
					gestionMessage((Message)mensaje);
				}
				else
				{
					System.out.print("Error");
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//System.out.println(i);
			break;
		}
	}

	@SuppressWarnings({ })
	private void gestionMessage(Message mensaje) throws IOException {
		// TODO Auto-generated method stub

		if(mensaje.getOrden().equals("CgetList")){
			if(mensaje.getPath()==null){
				String path = System.getProperty("user.dir");
				File directori = new File(path);
				mensaje = new Message();
				mensaje.setOrden("SputList");
				mensaje.setPath(path);
				mensaje.setList(directori.listFiles());			
			}else{
				String path = mensaje.getPath();
				File directori = new File(path);
				mensaje = new Message();
				mensaje.setOrden("SputList");
				mensaje.setPath(path);
				mensaje.setList(directori.listFiles());	
			}
		}

		if(mensaje.getOrden().equals("CgetFile")){
			getFile(mensaje);
		}

		if(mensaje.getOrden().equals("CputFile")){
			putFile(mensaje);
		}


		if(mensaje.getOrden().equals("SputList"))enviaMessage(mensaje);

	}

	private void putFile(Message mensaje) {
		// TODO Auto-generated method stub
		try
		{
			// Se envía un mensaje de petición de fichero.
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			
			mensaje = new Message();
			mensaje.setOrden("SgetFile");
			oos.writeObject(mensaje);
			
			// Se abre un fichero para empezar a copiar lo que se reciba.
			
			// Se crea un ObjectInputStream del socket para leer los mensajes
			// que contienen el fichero.
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			FileOutputStream fos = null;
			Files mensajeRecibido;
			Object mensajeAux;
			do
			{
				// Se lee el mensaje en una variabla auxiliar
				mensajeAux = ois.readObject();

				// Si es del tipo esperado, se trata
				if (mensajeAux instanceof Files)
				{
					fos = new FileOutputStream(((Files) mensajeAux).getNombreFichero()+ "_copia");
					mensajeRecibido = (Files) mensajeAux;
					// Se escribe en el ficher
					fos.write(mensajeRecibido.getContenidoFichero(), 0,
							mensajeRecibido.getBytesValidos());
				} else
				{
					// Si no es del tipo esperado, se marca error y se termina
					// el bucle
					System.err.println("Mensaje no esperado "
							+ mensajeAux.getClass().getName());
					break;
				}
			} while (!mensajeRecibido.isUltimoMensaje());

			// Se cierra socket y fichero
			fos.close();
			ois.close();
			socket.close();

		} catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	@SuppressWarnings("resource")
	private void getFile(Message mensaje) throws IOException {
		// TODO Auto-generated method stub
		// Una variable auxiliar para marcar cuando se envía el último mensaje
		boolean enviadoUltimo=false;
		ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
		// Se abre el fichero.
		FileInputStream fis = new FileInputStream(mensaje.getPath());

		// Se instancia y rellena un mensaje de envio de fichero
		Files files = new Files();
		files.setNombreFichero(mensaje.getPath());

		// Se leen los primeros bytes del fichero en un campo del mensaje
		int leidos = fis.read(files.getContenidoFichero());

		// Bucle mientras se vayan leyendo datos del fichero
		while (leidos > -1)
		{               
			// Se rellena el número de bytes leidos
			files.setBytesValidos(leidos);

			// Si no se han leido el máximo de bytes, es porque el fichero
			// se ha acabado y este es el último mensaje
			if (leidos <Files.LONGITUD_MAXIMA)
			{
				// Se marca que este es el último mensaje
				files.setUltimoMensaje(true);
				enviadoUltimo=true; 
			}
			else files.setUltimoMensaje(false);


			oos.writeObject(files);

			// Si es el último mensaje, salimos del bucle.
			if (files.isUltimoMensaje())
				break;

			// Se crea un nuevo mensaje
			files = new Files();
			files.setNombreFichero(mensaje.getPath());

			// y se leen sus bytes.
			leidos = fis.read(files.getContenidoFichero());
		}

		if (enviadoUltimo==false)
		{
			files.setUltimoMensaje(true);
			files.setBytesValidos(0);
			oos.writeObject(files);
		}
		oos.close();
	}

	private void enviaMessage(Message mensaje) {
		// TODO Auto-generated method stub
		try {
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject(mensaje);
		}
		catch(Exception e) {
			System.out.print("Error al Conectar-se\n");
		}
	} 

}

