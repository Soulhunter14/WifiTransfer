package server;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * AX Team. 
 * Classe servidor que permite las conexiones entrantes via Sockets.
 * 
 */
public class FileServer {

	public static void main(String[] args)
    {
        FileServer fs = new FileServer();
        fs.listen(35557);
    }

	@SuppressWarnings({ "resource" })
	private void listen(int port) {

        try
        {
            // Se abre el socket servidor
        	ServerSocket socketServidor = new ServerSocket(port);
            
            while (true)
            {
            	
                // Se espera y acepta un nuevo cliente
            	Socket cliente = socketServidor.accept();
            	cliente.setSoLinger(true, 10);
            	
                // Se instancia una clase para atender al cliente y se lanza en
                // un hilo aparte.
                Runnable newclient = new ThreadCliente(cliente); 
                Thread hilo = new Thread(newclient);
                hilo.start();       
            } 
            
        } catch (Exception e)
        {
            e.printStackTrace();
        }
		
	}
	
}
