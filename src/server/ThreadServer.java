package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import Class.WriteOnFile;

public class ThreadServer implements Runnable{

	ServerSocket socketServidor;
	ThreadGroup tg;
	Socket cliente;
	Vector<Socket> VecSocket = new Vector<Socket>(); 
	
	public ThreadServer(ServerSocket socketServidor){
		this.socketServidor = socketServidor;
	}
	
	@Override
	public void run() {
		try
		{			
			while (true)
			{

				// Se espera y acepta un nuevo cliente
				cliente = socketServidor.accept();
				cliente.setSoLinger(true, 10);
				VecSocket.add(cliente);

				// Se instancia una clase para atender al cliente y se lanza en
				// un hilo aparte.
				
				Runnable newclient = new ThreadCliente(cliente); 
				tg = new ThreadGroup("Servers Threads");
				Thread hilo = new Thread(tg, newclient);
				hilo.start();       
				System.out.println("Cliente Aceptado");
				if(socketServidor.isClosed())break;
			} 

		} catch (Exception e)
		{
			try {
				int i  = 0;
				while(i<VecSocket.size()){
					VecSocket.get(i).close();
					i++;
				}
				if(tg!=null)tg.interrupt();
				new WriteOnFile(System.getProperty("user.dir"),"Servidor Cerrado \n\r");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}     
	}
	public static String getFechaActual() {
	    Date ahora = new Date();
	    SimpleDateFormat formateador = new SimpleDateFormat("dd-MM-yyyy");
	    return formateador.format(ahora);
	}

}
