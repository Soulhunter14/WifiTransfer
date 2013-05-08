package server;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

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

	private void gestionMessage(Message mensaje) {
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

