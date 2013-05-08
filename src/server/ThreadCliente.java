package server;

import java.io.File;
import java.io.FileInputStream;
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

	@SuppressWarnings({ "null", "resource" })
	private void gestionMessage(Message mensaje) throws IOException {
		// TODO Auto-generated method stub

		if(mensaje.getOrden().equals("getList")){
			if(mensaje.getPath()==null){
				String path = System.getProperty("user.dir");
				File directori = new File(path);
				mensaje = new Message();
				mensaje.setOrden("putList");
				mensaje.setPath(path);
				mensaje.setList(directori.listFiles());			
			}else{
				String path = mensaje.getPath();
				File directori = new File(path);
				mensaje = new Message();
				mensaje.setOrden("putList");
				mensaje.setPath(path);
				mensaje.setList(directori.listFiles());	
			}
		}

		if(mensaje.getOrden().equals("getFile")){
			// Una variable auxiliar para marcar cuando se envía el último mensaje
			boolean enviadoUltimo=false;
			ObjectOutputStream oos = null;
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
				else
					files.setUltimoMensaje(false);

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
		enviaMessage(mensaje);

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

