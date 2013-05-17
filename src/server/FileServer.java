package server;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
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

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.config.EmbeddedConfiguration;

/**
 * AX Team. 
 * Classe servidor que permite las conexiones entrantes via Sockets.
 * 
 */
public class FileServer extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 56L;
	static FileServer fs;
	ServerSocket socketServidor;
	Thread hilo;
	JLabel title;
	JLabel home;
	JLabel ip;
	JLabel puerto;
	ObjectContainer db;
	JButton iniciar;
	Runnable startServer;
	Long id;

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
		puerto = new JLabel("La aplicación funciona a través de el puerto: 8888");

		iniciar = new JButton("Iniciar Servidor");
		iniciar.addActionListener(this);

		//Constraints//
		sl.putConstraint(SpringLayout.NORTH, home, 125, SpringLayout.NORTH, this);
		sl.putConstraint(SpringLayout.WEST, home, 100, SpringLayout.WEST, this);

		sl.putConstraint(SpringLayout.NORTH, title, 70, SpringLayout.NORTH, this);
		sl.putConstraint(SpringLayout.WEST, title, 125, SpringLayout.WEST, this);

		sl.putConstraint(SpringLayout.NORTH, ip, 150, SpringLayout.NORTH, this);
		sl.putConstraint(SpringLayout.WEST, ip, 125, SpringLayout.WEST, this);

		sl.putConstraint(SpringLayout.NORTH, puerto, 15, SpringLayout.SOUTH, ip);
		sl.putConstraint(SpringLayout.WEST, puerto, 0, SpringLayout.WEST, ip);

		sl.putConstraint(SpringLayout.NORTH, iniciar, 15, SpringLayout.SOUTH, puerto);
		sl.putConstraint(SpringLayout.EAST, iniciar, 0, SpringLayout.EAST, puerto);

		//jp.add(home);
		jp.add(title);
		jp.add(ip);
		jp.add(puerto);
		jp.add(iniciar);
		return jp;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if ("Iniciar Servidor".equals(e.getActionCommand())){
			
			verificaUsuario();

			File log = new File(System.getProperty("user.dir"),"log");
			if(!log.exists())log.mkdir();

			iniciar.setText("Detener Servidor");
			try {
				socketServidor = new ServerSocket(8888);
				this.repaint();
				startServer = new ThreadServer(socketServidor); 
				hilo = new Thread(startServer);
				hilo.start();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		if ("Detener Servidor".equals(e.getActionCommand())){
			if(JOptionPane.showConfirmDialog(this, "Realmente desea cerrar el servidor?", "Si", 
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)==0){
				iniciar.setText("Iniciar Servidor");

				this.repaint();
				try {
					socketServidor.close();
					hilo.interrupt();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}

	private void verificaUsuario() {
		System.out.println("Hoola");
		File usuarios = new File(System.getProperty("user.dir"),"usuarios");
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
System.out.println("Hoola");
				u1.setPass(EncryptMD5.encriptaEnMD5("eloy"));
				u2.setPass(EncryptMD5.encriptaEnMD5("alex"));
				u3.setPass(EncryptMD5.encriptaEnMD5("xavi"));
				
				db.store(u1);
				db.store(u2);
				db.store(u3);
				
				db.close();
				
			} catch (IOException e) {
				e.printStackTrace();
			}

	}

}
