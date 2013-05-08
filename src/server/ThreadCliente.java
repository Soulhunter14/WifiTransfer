package server;

import java.io.IOException;
import java.io.ObjectInputStream;
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

		int i =0;

		while (true)
		{
			// Código para atender la peticion
			 ObjectInputStream ois;
			try {
				ois = new ObjectInputStream(socket.getInputStream());
				Object mensaje = ois.readObject();
				
				if (mensaje instanceof Message)
				
				System.out.println(((Message) mensaje).getOrden());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			i++;
			System.out.println(i);
			break;
		}
	}
}
