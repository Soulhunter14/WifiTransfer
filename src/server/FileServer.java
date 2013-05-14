package server;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
			iniciar.setText("Detener Servidor");
			try {
				socketServidor = new ServerSocket(8888);
				this.repaint();
				startServer = new ThreadServer(socketServidor); 
				hilo = new Thread(startServer);
				hilo.start();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
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
}
