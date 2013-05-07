package server;

import java.net.Socket;

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
            // ControlCliente control = new ControlCliente(socket, panel);
         } catch (Exception e)
         {
             e.printStackTrace();
         }
    	
    }
}
