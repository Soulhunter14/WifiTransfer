package server;

import java.net.ServerSocket;
import java.net.Socket;

public class ThreadServer implements Runnable{

	static boolean servInic;
	
	public static void interrupt(){
		servInic = false;
	}
	
	
	@SuppressWarnings("resource")
	@Override
	public void run() {
		try
		{
			// Se abre el socket servidor
			ServerSocket socketServidor = new ServerSocket(8888);
			System.out.println("Servidor Iniciado");
			servInic = true;
			while (servInic)
			{

				// Se espera y acepta un nuevo cliente
				Socket cliente = socketServidor.accept();
				cliente.setSoLinger(true, 10);

				// Se instancia una clase para atender al cliente y se lanza en
				// un hilo aparte.
				
				Runnable newclient = new ThreadCliente(cliente); 
				Thread hilo = new Thread(newclient);
				hilo.start();       
				System.out.println("Cliente Aceptado");
			} 

		} catch (Exception e)
		{
			e.printStackTrace();
		}     
	}

}
