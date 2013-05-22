package server;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;


import Class.EncryptMD5;
import Class.Usuario;
import Class.WriteOnFile;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.config.EmbeddedConfiguration;

/**
 * AX Team. 
 * Classe servidor que permite las conexiones entrantes via Sockets.
 * 
 */
public class FileServer extends JFrame implements ActionListener {
	
	private static final long serialVersionUID = 56L;
	static FileServer fs;
	ServerSocket socketServidor;
	int puerto;
	Thread hilo;
	JLabel title;
	JLabel home;
	JLabel ip;
	JLabel jlpuerto;
	ObjectContainer db;
	JButton iniciar;
	Runnable startServer;
	File wifitransfer_conf;
	Long id;
	File logsistema;
	FileWriter conf;
	File log;
	File usuarios;
	String rlogsistema;

	File archivo = null;
	FileReader fr = null;
	BufferedReader br = null;
	
	public static void main(String[] args)
	{
		fs = new FileServer();
	}

	public FileServer() {
		super();
		initialize();
	}

	private void initialize() {

		this.setTitle("WifiTransfer");
		this.setSize(new Dimension(500,350)); 
		this.setMinimumSize(new Dimension(500,350));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setContentPane(crearPanel());
		this.setVisible(true);
	}

	private JPanel crearPanel(){
		JPanel jp = new JPanel();
		SpringLayout sl = new SpringLayout();
		jp.setLayout(sl);

		this.setJMenuBar(new MenuServer(this));

		logsistema = new File(System.getProperty("user.dir"),"logsistema");
		wifitransfer_conf = new File(System.getProperty("user.dir"),"wifitransfer.conf");
		try {
			if(!wifitransfer_conf.exists())verificaArchivos();
			else{
				verificaPuerto();
			}
		} catch (IOException e2) {
			e2.printStackTrace();
		}

		title = new JLabel("WIFI-TRANSFER");
		title.setForeground(Color.RED);
		title.setFont(new java.awt.Font("Tahoma", 0, 36));

		home = new JLabel("",SwingConstants.CENTER);
		home.setIcon(new  ImageIcon(getClass().getClassLoader().getResource("images/logo.gif")));
		home.setLayout( new BorderLayout() );

		try {
			ip = new JLabel("Dirección IP: "+InetAddress.getLocalHost().getHostAddress());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		jlpuerto = new JLabel("La aplicación funciona a través de el puerto: "+puerto);

		iniciar = new JButton("Iniciar Servidor");
		iniciar.addActionListener(this);

		//Constraints//
		sl.putConstraint(SpringLayout.NORTH, home, 125, SpringLayout.NORTH, this);
		sl.putConstraint(SpringLayout.WEST, home, 100, SpringLayout.WEST, this);

		sl.putConstraint(SpringLayout.NORTH, title, 70, SpringLayout.NORTH, this);
		sl.putConstraint(SpringLayout.WEST, title, 125, SpringLayout.WEST, this);

		sl.putConstraint(SpringLayout.NORTH, ip, 150, SpringLayout.NORTH, this);
		sl.putConstraint(SpringLayout.WEST, ip, 125, SpringLayout.WEST, this);

		sl.putConstraint(SpringLayout.NORTH, jlpuerto, 15, SpringLayout.SOUTH, ip);
		sl.putConstraint(SpringLayout.WEST, jlpuerto, 0, SpringLayout.WEST, ip);

		sl.putConstraint(SpringLayout.NORTH, iniciar, 15, SpringLayout.SOUTH, jlpuerto);
		sl.putConstraint(SpringLayout.EAST, iniciar, 0, SpringLayout.EAST, jlpuerto);

		//jp.add(home);
		jp.add(title);
		jp.add(ip);
		jp.add(jlpuerto);
		jp.add(iniciar);
		return jp;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if ("Iniciar Servidor".equals(e.getActionCommand())){
			try {
				if(verificaArchivos()){
					socketServidor = new ServerSocket(puerto);
					startServer = new ThreadServer(socketServidor); 
					hilo = new Thread(startServer);
					hilo.start();
					new WriteOnFile(logsistema.getAbsolutePath(),"Servidor iniciado \n\r");
					iniciar.setText("Detener Servidor");
					this.repaint();
				}
			} catch (IOException e1) {
				try {
					new WriteOnFile(rlogsistema, "El puerto especificado ya esta ocupado, revisa si la aplicación ya esta en marcha o otra aplicación esta utilizando este.\r\n");
					error();
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}	
			}
		}
		if ("Detener Servidor".equals(e.getActionCommand())){
			if(JOptionPane.showConfirmDialog(this, "Realmente desea cerrar el servidor?", "Si", 
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)==0)
			{
				iniciar.setText("Iniciar Servidor");

				this.repaint();
				try {
					socketServidor.close();
					hilo.interrupt();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	private boolean verificaArchivos() throws IOException {

		if(!wifitransfer_conf.exists()){

			File wifitransfer_conf = new File(System.getProperty("user.dir"),"wifitransfer.conf");
			if(!wifitransfer_conf.exists()){

				log = new File(System.getProperty("user.dir"),"log");
				if(!log.exists())log.mkdir();

				logsistema = new File(System.getProperty("user.dir"),"logsistema");
				if(!logsistema.exists())logsistema.createNewFile();

				usuarios = new File(System.getProperty("user.dir"),"usuarios");
				if(!usuarios.exists())
					try {
						usuarios.createNewFile();

						EmbeddedConfiguration config = Db4oEmbedded.newConfiguration();
						config.common().objectClass(Usuario.class)
						.cascadeOnUpdate(true);
						config.common().objectClass(Usuario.class)
						.cascadeOnDelete(true);
						db = Db4oEmbedded.openFile(config, usuarios.getAbsolutePath());

						Usuario u1 = new Usuario();
						Usuario u2 = new Usuario();
						Usuario u3 = new Usuario();

						u1.setUser("eloy");
						u2.setUser("alex");
						u3.setUser("xavi");

						u1.setPass(EncryptMD5.encriptaEnMD5("eloy"));
						u2.setPass(EncryptMD5.encriptaEnMD5("alex"));
						u3.setPass(EncryptMD5.encriptaEnMD5("xavi"));

						db.store(u1);
						db.store(u2);
						db.store(u3);

						db.close();

						wifitransfer_conf.createNewFile();
						conf = new FileWriter(wifitransfer_conf);

						String datos =
								"		//Puerto definido por defecto \r\n" +
										"puerto:8888\r\n" +
										"\r\n" +
										"\r\n" +
										"		//Ruta definida por defecto:\r\n" +
										"rorigen:"+System.getProperty("user.dir")+"\r\n" +
										"\r\n" +
										"\r\n" +
										"		//Ruta de los logs\r\n" +
										"rlog:"+log.getAbsolutePath()+"\r\n" +"\r\n" +
										"\r\n" +
										"		//Ruta del log del sistema\r\n" +
										"rlogsistema:"+logsistema.getAbsolutePath()+"\r\n" +
										"\r\n" +
										"\r\n" +
										"		//Ruta de la base de datos de los usuarios\r\n" +
										"rusuarios:"+usuarios.getAbsolutePath()+"\r\n";

						conf.write(datos);
						conf.close();


						archivo = null;
						fr = null;
						br = null;
						// Apertura del fichero y creacion de BufferedReader para poder
						// hacer una lectura comoda (disponer del metodo readLine()).
						archivo = new File (System.getProperty("user.dir"),"wifitransfer.conf");
						fr = new FileReader (archivo);
						br = new BufferedReader(fr);

						// Lectura del fichero
						String linea;
						while((linea=br.readLine())!=null){
							if(linea.startsWith("puerto:")){
								puerto=Integer.valueOf(linea.substring(7));
								break;
							}
						}
					}
				catch(Exception e){
					e.printStackTrace();
				}finally{
					// En el finally cerramos el fichero.
					if( null != fr ){   
						fr.close();  
					}
				}
			}
		}
		else{
			archivo = null;
			fr = null;
			br = null;
			try {
				// Apertura del fichero y creacion de BufferedReader para poder
				// hacer una lectura comoda (disponer del metodo readLine()).
				archivo = new File (System.getProperty("user.dir"),"wifitransfer.conf");
				fr = new FileReader (archivo);
				br = new BufferedReader(fr);

				String rlog = "";
				String rusuarios = "";
				rlogsistema="";

				// Lectura del fichero
				String linea;
				while((linea=br.readLine())!=null){
					if(linea.startsWith("rlog:"))rlog=linea.substring(5);
					if(linea.startsWith("rusuarios:"))rusuarios=linea.substring(10);
					if(linea.startsWith("puerto:"))puerto=Integer.valueOf(linea.substring(7));
					if(linea.startsWith("rlogsistema:"))rlogsistema=linea.substring(12);
				}

				logsistema = new File(rlogsistema);
				if(!logsistema.exists()){
					logsistema.createNewFile();
					new WriteOnFile(rlogsistema, "log del sistema no encontrado se procedera a " +
							"redireccionar los errores a la ruta por defecto: "+System.getProperty("user.dir")+"logsistema\r\n");
				}


				usuarios = new File(rusuarios);
				if(!usuarios.exists())
				{
					new WriteOnFile(rlogsistema, "BBDD Usuarios no encontrada en la ruta especificada: " +
							""+usuarios.getAbsolutePath()+"\r\n");	
					error();
					return false;
				}

				log = new File(rlog);
				if(!log.exists())
				{
					new WriteOnFile(rlogsistema, "Directorio log no encontrado en:" +
							""+log.getAbsolutePath()+"\r\n");	
					error();
					return false;
				}
				return true;

			}
			catch(Exception e){
				e.printStackTrace();
			}finally{
				// En el finally cerramos el fichero.
				if( null != fr ){   
					fr.close();  
				}
			}
		}
		return false;
	}
	
	public void error(){
		JOptionPane.showMessageDialog(this,
				"Error al iniciar el Sistema, " +
				"\n Revisa el Log para mas información. " +
				"\n"+logsistema.getAbsolutePath(),
				"Wifi Transfer",
				JOptionPane.INFORMATION_MESSAGE);		
	}
	
	private void verificaPuerto() {
		archivo = new File (System.getProperty("user.dir"),"wifitransfer.conf");
		try {
			fr = new FileReader (archivo);
			br = new BufferedReader(fr);
			
			rlogsistema = "";
			
			String linea;
			while((linea=br.readLine())!=null){
				if(linea.startsWith("rlogsistema:"))rlogsistema=linea.substring(12);
				if(linea.startsWith("puerto:"))puerto=Integer.valueOf(linea.substring(7));
			}
			
			logsistema = new File(rlogsistema);
			if(!logsistema.exists()){
				logsistema.createNewFile();
				new WriteOnFile(rlogsistema, "log del sistema no encontrado se procedera a " +
						"redireccionar los errores a la ruta por defecto: "+System.getProperty("user.dir")+"logsistema\r\n");
			}

			System.out.print(puerto==0);
			if(puerto == 0){
				new WriteOnFile(rlogsistema, "Puerto no encontrado en el archivo de configuracion: " +
						""+rlogsistema+"\r\n");	
				error();
				
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		
	}
}

