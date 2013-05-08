package server;

import java.io.PrintWriter;
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
             
             String data = "Toobie ornaught toobie";
             try {

                System.out.print("Server has connected!\n");
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                System.out.print("Sending string: '" + data + "'\n");
                out.print(data);
                out.close();
                socket.close();
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
