package server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

import Class.Files;
import Class.Message;


public class ThreadCliente implements Runnable{

	private Socket socket;
	FileWriter log;
	String resume;
	public ThreadCliente(Socket socket)
	{
		this.socket = socket;
	}

	@Override
	public void run() {
		try {
			File f = new File(getPath(),getFechaActual()+"_"+socket.getInetAddress().toString().substring(1, socket.getInetAddress().toString().length())+".txt");
			log = new FileWriter(f);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		resume = log.toString();
		resume =getHoraActual()+" Conexión Aceptada\r\n";

		while (true)
		{
			// Código para atender la peticion
			ObjectInputStream ois;
			try {

				resume+="\r\n"+getHoraActual()+" A la espera de mensaje\r\n";
				ois = new ObjectInputStream(socket.getInputStream());
				Object mensaje = ois.readObject();

				if (mensaje instanceof Message){
					gestionMessage((Message)mensaje);
				}
				else
				{
					System.out.print("Error");
				}
			} catch (IOException e) {

				resume+="\r\n"+getHoraActual()+" Conexión cerrada por el servidor.";
				try {
					log.write(resume);
					log.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				break;
			} catch (ClassNotFoundException e) {
				System.out.println("Class no encontrada");
				break;
			}
		}
	}

	@SuppressWarnings({ })
	private void gestionMessage(Message mensaje) throws IOException {
		File[] listfiles;
		int i = 0;


		if(mensaje.getOrden().equals("CgetList")){
			if(mensaje.getPath()==null){
				resume+="\r\n"+getHoraActual()+" "+mensaje.getOrden()+" sin path\r\n";
				System.out.println(mensaje.getOrden()+" sin path\n");
				String path = System.getProperty("user.dir");
				File directori = new File(path);
				mensaje = new Message();
				mensaje.setOrden("SputList");
				mensaje.setSo(System.getProperty("os.name"));
				mensaje.setPath(System.getProperty("user.dir"));
				listfiles = directori.listFiles();
				i = 0;

				while(i<listfiles.length)
				{
					if(!listfiles[i].isHidden()){
						if(listfiles[i].isDirectory()){
							mensaje.addDire(listfiles[i].getName());
						}
						else{
							mensaje.addDocs(listfiles[i].getName());
						}
					}
					i++;
				}		
			}else{
				resume+="\r\n"+getHoraActual()+" "+mensaje.getOrden()+" "+mensaje.getPath()+"\r\n";
				System.out.println(mensaje.getOrden()+" "+mensaje.getPath()+"\n");
				String path = mensaje.getPath();
				File directori = new File(path);
				mensaje = new Message();
				mensaje.setOrden("SputList");
				mensaje.setSo(System.getProperty("os.name"));


				listfiles = directori.listFiles();
				while(i<listfiles.length)
				{
					System.out.println(listfiles[i].isDirectory());
					if(listfiles[i].isDirectory()){
						mensaje.addDire(listfiles[i].getName());
					}
					else
					{
						mensaje.addDocs(listfiles[i].getName());
					}
					i++;
				}		
			}
		}

		if(mensaje.getOrden().equals("CgetFile")){
			resume +="\r\n"+getHoraActual()+" "+mensaje.getOrden()+" "+mensaje.getPath()+"\r\n";
			System.out.println(mensaje.getOrden()+" "+mensaje.getPath()+"\n");
			getFile(mensaje);
		}

		if(mensaje.getOrden().equals("CputFile")){
			resume +="\r\n"+getHoraActual()+" "+mensaje.getOrden()+" "+mensaje.getPath()+"\r\n";
			System.out.println(mensaje.getOrden()+" "+mensaje.getPath()+"\n");
			putFile(mensaje);
		}


		if(mensaje.getOrden().equals("SputList"))enviaMessage(mensaje);

	}

	private void putFile(Message mensaje) {
		try
		{
			// Se envía un mensaje de petición de fichero.
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

			mensaje = new Message();
			mensaje.setOrden("SgetFile");
			mensaje.setSo(System.getProperty("os.name"));
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
			//socket.close();

		} catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	@SuppressWarnings("resource")
	private void getFile(Message mensaje) throws IOException {
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
		try {
			System.out.println("Mensaje enviador correctamente.");
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject(mensaje);
		}
		catch(Exception e) {
			System.out.print("Error al Conectar-se\n");
		}
	} 

	public static String getHoraActual() {
		Date ahora = new Date();
		SimpleDateFormat formateador = new SimpleDateFormat("kk:mm:ss ");
		return formateador.format(ahora);
	}

	public static String getFechaActual() {
		Date ahora = new Date();
		SimpleDateFormat formateador = new SimpleDateFormat("yyyy-M-dd");
		return formateador.format(ahora);
	}

	public static String getFechaHoraActual() {
		Date ahora = new Date();
		SimpleDateFormat formateador = new SimpleDateFormat("yyyy-M-dd kk:mm:ss ");
		return formateador.format(ahora);
	}

	private String getPath() {
		File f = new File(System.getProperty("user.dir"));
		File[] dire = f.listFiles();
		int i = 0;
		while(i<dire.length)
		{
			if(dire[i].getName().equals("log"))
			{
				return dire[i].getAbsolutePath();
			}
			i++;
		}
		return null;

	}

}

