package server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.Box;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;


public class MenuServer extends JMenuBar implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1132L;
	private FileServer fileserver;
	private JMenu fileMenu;
	private JMenuItem configuracioMenuItem;
	private JMenuItem usuariosMenuItem;
	private JMenuItem exitMenuItem;
	private JMenu ajudaMenu;
	private JMenuItem aboutMenuItem;
	

	public MenuServer(FileServer fileserver) {
		super();
		this.fileserver = fileserver;
		initialize();
	}

	private void initialize() {
		fileMenu = new JMenu("Arxiu");
		fileMenu.setMnemonic(KeyEvent.VK_A);
		fileMenu.addSeparator();
		
		configuracioMenuItem = new JMenuItem("Configuracion", KeyEvent.VK_C);
		configuracioMenuItem.addActionListener(this);
		configuracioMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
		fileMenu.add(configuracioMenuItem);
		
		usuariosMenuItem = new JMenuItem("Usuarios", KeyEvent.VK_U);
		usuariosMenuItem.addActionListener(this);
		usuariosMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U, ActionEvent.CTRL_MASK));
		fileMenu.add(usuariosMenuItem);
		
		exitMenuItem = new JMenuItem("Sortir", KeyEvent.VK_S);
		exitMenuItem.addActionListener(this);
		exitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
		fileMenu.add(exitMenuItem);
		this.add(fileMenu);
		
		
		
		this.add(Box.createHorizontalGlue());
		
		ajudaMenu = new JMenu("Ajuda");
		ajudaMenu.setMnemonic(KeyEvent.VK_J);
		aboutMenuItem = new JMenuItem("About");
		aboutMenuItem.addActionListener(this);
		aboutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
		ajudaMenu.add(aboutMenuItem);
		this.add(ajudaMenu);
	}


	public void actionPerformed(ActionEvent e) {
		if ("Sortir".equals(e.getActionCommand())){
			System.exit(0);
		}
		
		if ("Configuracion".equals(e.getActionCommand())) {
			//pare.canviPane(new CrearConcessionari(pare));
			return;
		}

		if ("About".equals(e.getActionCommand())) {
			JOptionPane.showMessageDialog(fileserver, "Wifi Transfer ", "About", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
	}
}

