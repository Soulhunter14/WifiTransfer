package main;


import java.io.ObjectOutputStream;
import java.net.Socket;

import Class.Message;



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
			socket = new Socket("192.168.2.171", 35557);
			socket.setSoLinger(true, 10);
			// ControlCliente control = new ControlCliente(socket, panel);

			try {
				ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
				Message mensaje = new Message();
				mensaje.setOrden("Esta es la Orden");
				oos.writeObject(mensaje);
				
			}
			catch(Exception e) {
				System.out.print("Whoops! It didn't work!\n");
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}

	}
}
