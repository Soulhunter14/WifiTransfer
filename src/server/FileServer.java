package server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import Apuntes.MensajeDameFichero;

public class FileServer {

	public static void main(String[] args)
    {
        FileServer fs = new FileServer();
        fs.listen(35557);
    }

	private void listen(int port) {

        try
        {
            // Se abre el socket servidor
            ServerSocket socketServidor = new ServerSocket(port);

            // Se espera un cliente
            

            // Llega un cliente.
            
            while (true)
            {
                // Se espera y acepta un nuevo cliente
            	Socket cliente = socketServidor.accept();
            	
                // Se instancia una clase para atender al cliente y se lanza en
                // un hilo aparte.
                Runnable newclient = new ThreadCliente(); 
                Thread hilo = new Thread(newclient);
                hilo.start();       
            } 
            
        } catch (Exception e)
        {
            e.printStackTrace();
        }
		
	}
	
}
