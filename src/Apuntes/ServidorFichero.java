/**
 * Javier Abellán, 18 Mar 2006
 * 
 * Programa de ejemplo de como transmitir un fichero por un socket.
 * Esta es la parte del servidor.
 */
package Apuntes;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Clases servidora que envía un fichero al primer cliente que se lo pida.
 * 
 * @author Javier Abellán
 */
public class ServidorFichero
{
    /**
     * Instancia la clase servidora y la pone a la escucha del puerto 35557
     * 
     * @param args
     *            de la línea de comandos.
     */
    public static void main(String[] args)
    {
        ServidorFichero sf = new ServidorFichero();
        sf.escucha(35557);
    }

    /**
     * Se escucha el puerto indicado en espera de clientes a los que enviar
     * el fichero.
     * 
     * @param puerto El puerto de escucha
     */
    public void escucha(int puerto)
    {
        try
        {
            // Se abre el socket servidor
            ServerSocket socketServidor = new ServerSocket(puerto);

            // Se espera un cliente
            Socket cliente = socketServidor.accept();

            // Llega un cliente.
            System.out.println("Aceptado cliente");

            // Cuando se cierre el socket, esta opción hara que el cierre se
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
            socketServidor.close();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Envía el fichero indicado a través del ObjectOutputStream indicado.
     * @param fichero Nombre de fichero
     * @param oos ObjectOutputStream por el que enviar el fichero
     */
    private void enviaFichero(String fichero, ObjectOutputStream oos)
    {
        try
        {
            boolean enviadoUltimo=false;
            // Se abre el fichero.
            FileInputStream fis = new FileInputStream(fichero);
            
            // Se instancia y rellena un mensaje de envio de fichero
            MensajeTomaFichero mensaje = new MensajeTomaFichero();
            mensaje.nombreFichero = fichero;
            
            // Se leen los primeros bytes del fichero en un campo del mensaje
            int leidos = fis.read(mensaje.contenidoFichero);
            
            // Bucle mientras se vayan leyendo datos del fichero
            while (leidos > -1)
            {
                
                // Se rellena el número de bytes leidos
                mensaje.bytesValidos = leidos;
                
                // Si no se han leido el máximo de bytes, es porque el fichero
                // se ha acabado y este es el último mensaje
                if (leidos < MensajeTomaFichero.LONGITUD_MAXIMA)
                {
                    mensaje.ultimoMensaje = true;
                    enviadoUltimo=true;
                }
                else
                    mensaje.ultimoMensaje = false;
                
                // Se envía por el socket
                oos.writeObject(mensaje);
                
                // Si es el último mensaje, salimos del bucle.
                if (mensaje.ultimoMensaje)
                    break;
                
                // Se crea un nuevo mensaje
                mensaje = new MensajeTomaFichero();
                mensaje.nombreFichero = fichero;
                
                // y se leen sus bytes.
                leidos = fis.read(mensaje.contenidoFichero);
            }
            
            if (enviadoUltimo==false)
            {
                mensaje.ultimoMensaje=true;
                mensaje.bytesValidos=0;
                oos.writeObject(mensaje);
            }
            // Se cierra el ObjectOutputStream
            oos.close();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
