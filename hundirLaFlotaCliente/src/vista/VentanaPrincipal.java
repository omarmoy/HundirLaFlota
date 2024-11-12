package vista;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;

import cliente.ConexionConServidor;
import vista.seccion_partidas_en_juego.PanelPartidas;

import javax.swing.SwingConstants;

public class VentanaPrincipal {

	public JFrame ventana;
	public ConexionConServidor conexion;
	public Cabecera cabecera;

	public VentanaPrincipal(ConexionConServidor conexion) {
		this.conexion = conexion;
		this.cabecera = new Cabecera(this); 
		mostrarIU();
		conexion.getGJ().setVistaPrincipal(this);
	}

	private void mostrarIU() {
			// Marco y diseño
			ventana = new JFrame();
			ventana.getContentPane().setLayout(new BorderLayout());

			// Agregar las áreas correspondientes del BorderLayout
			ventana.getContentPane().add(this.cabecera, BorderLayout.NORTH);
			ventana.getContentPane().add(new MenuLateral(this), BorderLayout.WEST);
			ventana.getContentPane().add(new PanelPartidas(this), BorderLayout.CENTER);
			JLabel sur = new JLabel("Óscar Márquez Moya\u00AE");
			sur.setHorizontalAlignment(SwingConstants.CENTER);
			ventana.getContentPane().add(sur, BorderLayout.SOUTH);

			// Configurar el tamaño del marco y hacerlo visible
			ventana.setSize(1080, 720);
			ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			ventana.setVisible(true);		
	}

}
