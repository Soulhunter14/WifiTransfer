package main;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.Vector;

import Class.Files;
import Class.Message;
import Class.Usuario;



public class PruebaCliente {

	/** Socket con el servidor del chat */
	private Socket socket;

	public static void main(String[] args)
	{
		new PruebaCliente();
	}

	public PruebaCliente(){
		try
		{
			socket = new Socket("127.0.0.1", 8888);
			socket.setSoLinger(true, 10);
			System.out.println("Conexion aceptada.");
			int i =0;
			while(i<1){
				validaUsuario("joder","joder");
				i++;
			}
			validaUsuario("xavi","4be9505dcf6a24de2d361a85327b13b7");

			ObjectInputStream ois;
			ois = new ObjectInputStream(socket.getInputStream());
			Object mensaje = ois.readObject();
			if (mensaje instanceof Message){
				System.out.println(((Message) mensaje).getOrden());
				System.out.println(((Message) mensaje).getError());
			}

			int opc = 0;
			Scanner entrada;
			while(opc!=-1)
			{
				System.out.println("\nA l'espera de noves ordres");
				entrada=new Scanner(System.in);	
				opc = entrada.nextInt();		

				switch(opc){
				case(1):

					getList();
				break;

				case(2):
					getList("C:\\Users\\alumne\\");	
				break;

				case(3):
					getFile("C:\\Comp\\prova.txt");
				break;
				case(4):
					putFile("C:\\Comp\\prova.txt");	
				break;
				}
			}
		} catch (Exception e)
		{
			System.out.println("Conexion perdida con el Servidor");
		}    
		//getList();
		//Thread.sleep(300);
		//getList("C:\\Users\\alumne\\");
		//putFile("C:\\Comp\\prova.txt");

	}


	private void getList(String path) throws IOException, ClassNotFoundException {
		ObjectInputStream ois;
		ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
		Message mensaje = new Message();
		mensaje.setOrden("CgetList");
		mensaje.setPath(path);
		oos.writeObject(mensaje);



		ois = new ObjectInputStream(socket.getInputStream());
		Object mensaje2 = ois.readObject();

		if (mensaje2 instanceof Message){

			System.out.println(((Message) mensaje2).getOrden());
			System.out.println(((Message) mensaje2).getPath());

			Vector<String> dire = ((Message) mensaje2).getDire();

			int i=0;

			while(i<dire.size())
			{
				System.out.println(dire.get(i));
				i++;
			}
		}



	}

	private void getList() throws IOException, ClassNotFoundException {
		// TODO Auto-generated method stub
		ObjectInputStream ois;
		ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
		Message mensaje = new Message();
		mensaje.setOrden("CgetList");
		oos.writeObject(mensaje);

		ois = new ObjectInputStream(socket.getInputStream());
		Object mensaje2 = ois.readObject();

		if (mensaje2 instanceof Message){

			System.out.println(((Message) mensaje2).getOrden());
			System.out.println(((Message) mensaje2).getPath());

			Vector<String> dire = ((Message) mensaje2).getDire();
			Vector<String> docs = (((Message) mensaje2).getDocs());

			int i=0;
			System.out.println("Directorios:");
			while(i<dire.size())
			{
				System.out.println(dire.get(i));
				i++;
			}
			i=0;

			System.out.println("Documentos:");
			while(i<docs.size())
			{
				System.out.println(docs.get(i));
				i++;
			}
		}		
	}

	public void getFile(String fichero) throws ClassNotFoundException, IOException{
		try
		{


			// Se envía un mensaje de petición de fichero.
			ObjectOutputStream oos = new ObjectOutputStream(socket
					.getOutputStream());
			Message mensaje = new Message();
			mensaje.setOrden("CgetFile");
			mensaje.setPath(fichero);
			oos.writeObject(mensaje);

			// Se abre un fichero para empezar a copiar lo que se reciba.
			FileOutputStream fos = new FileOutputStream(mensaje.getPath()+ "_copia");

			// Se crea un ObjectInputStream del socket para leer los mensajes
			// que contienen el fichero.
			ObjectInputStream ois = new ObjectInputStream(socket
					.getInputStream());
			Files mensajeRecibido;
			Object mensajeAux;
			do
			{

				// Se lee el mensaje en una variabla auxiliar
				mensajeAux = ois.readObject();

				// Si es del tipo esperado, se trata
				if (mensajeAux instanceof Files)
				{
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

			// Se cierra fichero
			fos.close();

		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@SuppressWarnings("resource")
	public void putFile(String fichero) throws IOException{

		// TODO Auto-generated method stub
		// Una variable auxiliar para marcar cuando se envía el último mensaje
		ObjectOutputStream oos = new ObjectOutputStream(socket
				.getOutputStream());
		Message mensaje = new Message();
		mensaje.setOrden("CputFile");
		mensaje.setPath(fichero);

		oos.writeObject(mensaje);

		mensaje = comprovaMessage();
		if (mensaje!=null)
		{

			boolean enviadoUltimo=false;
			oos = new ObjectOutputStream(socket.getOutputStream());
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

			//oos.close();
		}
	}


	private Message comprovaMessage() {
		// TODO Auto-generated method stub
		ObjectInputStream ois;
		try {
			ois = new ObjectInputStream(socket.getInputStream());
			Object mensaje = ois.readObject();
			if (mensaje instanceof Message){
				if(((Message) mensaje).getOrden().equals("SgetFile"))
					((Message) mensaje).setPath("C:\\Comp\\prova.txt");
				return (Message) mensaje;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}


	private void validaUsuario(String usuario,String password) throws IOException, ClassNotFoundException {
		ObjectInputStream ois;
		ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
		Usuario user = new Usuario();
		user.setUser(usuario);
		user.setPass(password);
		oos.writeObject(user);

		ois = new ObjectInputStream(socket.getInputStream());
		Object mensaje = ois.readObject();

		if (mensaje instanceof Message){
			if(((Message) mensaje).getError()==null)System.out.println("Error al validar el usuario");
		}
		else
		{
			System.out.print("Error");
		}

	}
}
