package server;

public class ThreadCliente implements Runnable{

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		
		
		System.out.println("Aceptado cliente");
		
		
		
		 while (true)
	      {
	         // Código para atender la peticion
			 
			 break;
	      }
		 
		 System.out.println("Hilo acabado");
		
/*		// Cuando se cierre el socket, esta opción hara que el cierre se
        // retarde automáticamente hasta 10 segundos dando tiempo al cliente
        // a leer los datos.
        cliente.setSoLinger(true, 10);

        // Se lee el mensaje de petición de fichero del cliente.
        ObjectInputStream ois = new ObjectInputStream(cliente
                .getInputStream());
        Object mensaje = ois.readObject();
        
        // Si el mensaje es de petición de fichero
        if (mensaje instanceof MensajeDameFichero)
        {
            // Se muestra en pantalla el fichero pedido y se envia
            System.out.println("Me piden: "
                    + ((MensajeDameFichero) mensaje).nombreFichero);
            enviaFichero(((MensajeDameFichero) mensaje).nombreFichero,
                    new ObjectOutputStream(cliente.getOutputStream()));
        }
        else
        {
            // Si no es el mensaje esperado, se avisa y se sale todo.
            System.err.println (
                    "Mensaje no esperado "+mensaje.getClass().getName());
        }
        
        // Cierre de sockets 
        cliente.close();
        socketServidor.close();*/
		
	}

}
